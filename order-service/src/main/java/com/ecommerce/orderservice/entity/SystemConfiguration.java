package com.ecommerce.orderservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "system_configuration")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfiguration {
    
    @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "config_seq")
   @SequenceGenerator(name = "config_seq", sequenceName = "CONFIG_SEQ", allocationSize = 1)
   private Long id;
    
    @Column(name = "config_key", unique = true, nullable = false)
    private String configKey;
    
    @Column(name = "config_value", nullable = false)
    private String configValue;
    
    @Column(name = "config_type", nullable = false)
    private String configType;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "module")
    private String module;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "updated_by")
    private String updatedBy;
    
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}