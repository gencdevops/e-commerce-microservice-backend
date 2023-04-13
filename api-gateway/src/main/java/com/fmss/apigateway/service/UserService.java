package com.fmss.apigateway.service;

import com.turkcell.dcs.corelib.constants.HeaderConstants;
import com.turkcell.dcs.corelib.utils.CryptUtil;
import com.turkcell.dcs.corelib.utils.DateUtils;
import com.turkcell.dcs.gncgateway.clientdtos.request.user.UpdateUserProfilePhotoRequestDto;
import com.turkcell.dcs.gncgateway.clientdtos.request.user.UpdateUserRequestDto;
import com.turkcell.dcs.gncgateway.clientdtos.response.user.UpdateUserProfilePhotoResponseDto;
import com.turkcell.dcs.gncgateway.clientdtos.response.user.UserInfoDto;
import com.turkcell.dcs.gncgateway.configuration.HeadersThreadLocal;
import com.turkcell.dcs.gncgateway.constants.SessionKeys;
import com.turkcell.dcs.gncgateway.mapper.UserMapper;
import com.turkcell.dcs.gncgateway.model.user.GncUsers;
import com.turkcell.dcs.gncgateway.model.user.UserDto;
import com.turkcell.dcs.gncgateway.redis.usersession.RedisCacheService;
import com.turkcell.dcs.gncgateway.servicecaller.gncservicecaller.ProfileServiceCaller;
import com.turkcell.dcs.gncgateway.servicecaller.usermanagament.UserServiceCaller;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserServiceCaller userServiceCaller;
    private final ProfileServiceCaller profileServiceCaller;
    private final UserMapper userMapper;
    private final RedisCacheService redisCacheService;

    public Mono<UserInfoDto> userInfoWithFastLoginInnerCall(String token, String loginType, String msisdn) {
        return generateMonoCoordinatorResult(token, loginType, msisdn);
    }

    public Mono<UserInfoDto> updateGncUserInfo(String redisId, UpdateUserRequestDto updateUserRequestDto) {
        return generateMonoCoordinatorResult(redisId, updateUserRequestDto);
    }

    public Mono<UpdateUserProfilePhotoResponseDto> uploadPhoto(Flux<FilePart> profilePhoto, Map<String, String> header) {

        Flux<UpdateUserProfilePhotoResponseDto> profilePhotoFlux = profilePhoto
                .flatMap(photo -> getPhotoUrl(photo, header));

        return Mono.from(profilePhotoFlux);
    }

    private Mono<UpdateUserProfilePhotoResponseDto> getPhotoUrl(FilePart filePart, Map<String, String> header) {


        Mono<DataBuffer> from = Mono.from(filePart.content());
        return from.flatMap(dataBuffer -> {
            String name = getFileNameWithExtension(filePart);
            byte[] bytes = getBytes(dataBuffer);
            UpdateUserProfilePhotoRequestDto updateUserProfilePhotoRequestDto = new UpdateUserProfilePhotoRequestDto(bytes, name);
            return profileServiceCaller.uploadPhoto(updateUserProfilePhotoRequestDto, header);

        });
    }

    private static byte[] getBytes(DataBuffer dataBuffer) {
        byte[] bytes = new byte[dataBuffer.readableByteCount()];
        dataBuffer.read(bytes);
        DataBufferUtils.release(dataBuffer);
        return bytes;
    }

    private static String getFileNameWithExtension(Part filePart) {
        List<String> contentDisposition = filePart.headers().get("Content-Disposition");
        if(!CollectionUtils.isEmpty(contentDisposition)){
            String headerContentPosition = contentDisposition.get(0);
            String[] splitDispoitonValues = headerContentPosition.split(";");
            for (String dispoitonValues : splitDispoitonValues) {
                boolean filename = dispoitonValues.contains("filename");
                if (filename) {
                    String[] splitFileName = dispoitonValues.split("=");
                    return splitFileName[1].replace("\"", "");
                }
            }

        }
        return null;
    }

    private Mono<UserInfoDto> generateMonoCoordinatorResult(String fastLoginToken, String loginType, String msisdn) {
        String redisId = createRedisId();
        Mono<UserDto> userDtoMono = userServiceCaller.getUserInfoWithFastLoginInnerCall(fastLoginToken, loginType, redisId, msisdn);

        return userDtoMono.flatMap(userDto -> {
            UserInfoDto userInfoResponseDto = generateResponse(userDto, redisId);
            return Mono.just(userInfoResponseDto);
        });

    }

    private Mono<UserInfoDto> generateMonoCoordinatorResult(String redisId, UpdateUserRequestDto updateUserRequestDto) {
        UserDto userDTO = getUser(redisId);
        Mono<GncUsers> userDtoMono = userServiceCaller.updateGncUser(updateUserRequestDto, userDTO.getMsisdn());

        return userDtoMono.flatMap(gncUser -> {
            UserInfoDto userInfoResponseDto = generateResponse(userDTO, redisId, gncUser);
            return Mono.just(userInfoResponseDto);
        });

    }

    private UserInfoDto generateResponse(UserDto userDto, String redisId) {
        addRedis(redisId, userDto);
        UserInfoDto userInfoResponseDto = userMapper.convertToUserInfoResponseDtoFromUserDto(userDto);
        userInfoResponseDto = userInfoResponseDto.withToken(redisId);
        return userInfoResponseDto;
    }

    private UserInfoDto generateResponse(UserDto userDto, String redisId, GncUsers gncUser) {
        userDto.setName(gncUser.name());
        userDto.setLastName(gncUser.surname());
        userDto.setNickname(gncUser.nickname());
        userDto.setEmail(gncUser.email());
        userDto.setEducationStatus(gncUser.educationStatus());
        userDto.setUniversityName(gncUser.universityName());
        userDto.setBirthDate(gncUser.birthDate());
        userDto.setNewUser(false);
        UserInfoDto userInfoResponseDto = userMapper.convertToUserInfoResponseDtoFromUserDto(userDto);
        addRedis(redisId, userDto);
        userInfoResponseDto = userInfoResponseDto.withToken(redisId);
        return userInfoResponseDto;
    }

    public String createRedisId() {
        String clientToken = UUID.randomUUID().toString();
        clientToken = getClientTokenHashed(clientToken);
        String dateToGMT3String = DateUtils.dateToGMT3String(new Date(), DateUtils.DATE_FORMAT_YMD_HMS);
        return dateToGMT3String.concat(clientToken);
    }
    private static String getClientTokenHashed(String clientToken) {
        return CryptUtil.hashTextSHA256(clientToken);
    }
    public void addRedis(String redisTokenId, UserDto user) {
        redisCacheService.writeUserToLocalCache(redisTokenId, SessionKeys.USER_CONTAINER, user);
    }
    @SneakyThrows
    protected UserDto getUser(String userToken) {
        if (userToken == null) {
            return null;
        }

        return redisCacheService.getPartialSessionData(userToken, "user", UserDto.class);
    }

    public Mono<Void> logOut() {
        Map<String, String> header = HeadersThreadLocal.get();
        String userToken = header.get(HeaderConstants.USER_TOKEN);
        redisCacheService.removeUserTokenFromRedis(userToken);
        return Mono.empty();
    }
}