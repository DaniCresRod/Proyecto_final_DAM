package com.example.mySchedule.config;

/**
 * Esta clase se usa para ubicar las variables globales del sistema:
 * La SECRET_KEY es la semilla para la encriptacion de contraseñas (clave privada)
 */

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/variables.properties")
@Getter
public class EnvVariablesConfig {
    @Value("${SECRET_KEY}")
    private String S_KEY;
}
