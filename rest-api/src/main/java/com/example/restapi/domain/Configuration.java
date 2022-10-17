package com.example.restapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {
    public enum ConfigurationKey {
        CSV_LOADED_SUCCESSFULLY
    }
    @Id
    private String confKey;
    private String confValue;
}
