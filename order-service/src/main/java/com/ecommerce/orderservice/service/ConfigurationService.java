package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.entity.SystemConfiguration;
import com.ecommerce.orderservice.repository.SystemConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfigurationService {
    
    private final SystemConfigurationRepository configRepository;
    
    public List<SystemConfiguration> getAllConfigurations() {
        return configRepository.findAll();
    }
    
    public List<SystemConfiguration> getConfigurationsByModule(String module) {
        return configRepository.findByModule(module);
    }
    
    public String getConfigValue(String key, String defaultValue) {
        return configRepository.findByConfigKey(key)
                .map(SystemConfiguration::getConfigValue)
                .orElse(defaultValue);
    }
    
    @Transactional
    public SystemConfiguration createConfiguration(SystemConfiguration config) {
        return configRepository.save(config);
    }
    
    @Transactional
    public SystemConfiguration updateConfiguration(String key, String value, String updatedBy) {
        SystemConfiguration config = configRepository.findByConfigKey(key)
                .orElseThrow(() -> new RuntimeException("Configuration not found: " + key));
        config.setConfigValue(value);
        config.setUpdatedBy(updatedBy);
        return configRepository.save(config);
    }
    
    @Transactional
    public void deleteConfiguration(String key) {
        SystemConfiguration config = configRepository.findByConfigKey(key)
                .orElseThrow(() -> new RuntimeException("Configuration not found: " + key));
        configRepository.delete(config);
    }
}