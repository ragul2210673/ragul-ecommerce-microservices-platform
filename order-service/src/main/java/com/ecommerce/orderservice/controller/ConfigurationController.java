package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.entity.SystemConfiguration;
import com.ecommerce.orderservice.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/config")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ConfigurationController {
    
    private final ConfigurationService configService;
    
    @GetMapping
    public ResponseEntity<List<SystemConfiguration>> getAllConfigurations() {
        return ResponseEntity.ok(configService.getAllConfigurations());
    }
    
    @GetMapping("/module/{module}")
    public ResponseEntity<List<SystemConfiguration>> getConfigsByModule(@PathVariable String module) {
        return ResponseEntity.ok(configService.getConfigurationsByModule(module));
    }
    
    @PostMapping
    public ResponseEntity<SystemConfiguration> createConfiguration(@RequestBody SystemConfiguration config) {
        SystemConfiguration created = configService.createConfiguration(config);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{key}")
    public ResponseEntity<SystemConfiguration> updateConfiguration(
            @PathVariable String key,
            @RequestBody Map<String, String> request) {
        String value = request.get("value");
        String updatedBy = request.getOrDefault("updatedBy", "admin");
        SystemConfiguration updated = configService.updateConfiguration(key, value, updatedBy);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{key}")
    public ResponseEntity<Void> deleteConfiguration(@PathVariable String key) {
        configService.deleteConfiguration(key);
        return ResponseEntity.noContent().build();
    }
}