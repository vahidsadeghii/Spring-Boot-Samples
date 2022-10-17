package com.example.restapi.service.impl;

import com.example.restapi.domain.Configuration;
import com.example.restapi.repository.ConfigurationRepository;
import com.example.restapi.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {
    private final ConfigurationRepository configurationRepository;

    @Override
    public boolean isDataLoadedSuccessfully() {
        return configurationRepository.findById(Configuration.ConfigurationKey.CSV_LOADED_SUCCESSFULLY.name()).map(
                configuration -> Boolean.valueOf(configuration.getConfValue())
        ).orElse(false);
    }

    @Override
    public void dataLoadedSuccessfully() {
        configurationRepository.findById(Configuration.ConfigurationKey.CSV_LOADED_SUCCESSFULLY.name()).ifPresentOrElse(
                configuration -> {
                    configuration.setConfValue(Boolean.TRUE.toString());
                    configurationRepository.save(configuration);
                },
                () -> {
                    configurationRepository.save(
                            new Configuration(Configuration.ConfigurationKey.CSV_LOADED_SUCCESSFULLY.name(),
                                    Boolean.TRUE.toString())
                    );
                }
        );
    }


}
