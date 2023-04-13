package com.fmss.apigateway.mapper;

import com.turkcell.dcs.corelib.managers.RedisPmManager;
import com.turkcell.dcs.gncgateway.clientdtos.response.user.UserInfoDto;
import com.turkcell.dcs.gncgateway.constants.CommonConstants;
import com.turkcell.dcs.gncgateway.model.user.UserDto;
import com.turkcell.dcs.gncgateway.util.DateUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {DateUtils.class, CommonConstants.class})
public abstract class UserMapper {
    @Autowired
    protected RedisPmManager redisPmManager;

    private static final String PHOTO_EXTENSION = ".jpg";

    @Mapping(target = "isTurkcell", source = "turkcell")
    @Mapping(target = "isTurkcellEmployee", source = "turkcellEmployee")
    @Mapping(target = "userId", source = "accountId")
    @Mapping(target="profileImageUrl", source = "accountId", qualifiedByName = "profileImageLink")
    public abstract UserInfoDto convertToUserInfoResponseDtoFromUserDto(UserDto userDto);

    @Named("profileImageLink")
    protected String profileImageLink(String accountId){
        String baseLink = redisPmManager.getLabel(CommonConstants.GENEREAL_PAGE_MANAGER, "profile.photo.baselink","https://nra5lo4zbr0d.merlincdn.net/");
        StringBuilder profileLink = new StringBuilder(baseLink);
        profileLink.append(accountId).append(PHOTO_EXTENSION);
        return profileLink.toString();

    }

}
