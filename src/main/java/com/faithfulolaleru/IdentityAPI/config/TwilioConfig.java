package com.faithfulolaleru.IdentityAPI.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("twilio")   // from application.properties
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwilioConfig {

    private String accountSid;

    private String authToken;

    private String sender;
}
