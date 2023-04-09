package com.fmss.userservice.configuration;

import com.advicemybackend.model.entity.AppSetting;
import com.advicemybackend.repository.AppSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigurationHolder {
    private final AppSettingRepository appSettingRepository;

    public String getStringProperty(String key) {
        return appSettingRepository.findByName(key)
                .map(AppSetting::getValue)
                .map(String::valueOf)
                .orElse(null);
    }
}
