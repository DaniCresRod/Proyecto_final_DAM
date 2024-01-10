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

    @Value("${ENTERPRISE_NAME}")
    private String E_NAME;

    @Value("${ENTERPRISE_ADDRESS}")
    private String E_ADDRESS;

    @Value("${ENTERPISE_TOWN}")
    private String E_TOWN;

    @Value("${ENTERPRISE_POSTAL_CODE}")
    private String E_POSTAL;

    @Value("${ENTERPRISE_TELEPHONE}")
    private String E_PHONE;

    @Value("${ENTERPRISE_EMAIL}")
    private String E_EMAIL;

    @Value("${ENTERPRISE_CIF}")
    private String E_CIF;
}
