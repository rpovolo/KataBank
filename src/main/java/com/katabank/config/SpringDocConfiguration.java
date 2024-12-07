package com.katabank.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

    @Bean(name = "config.com.katabank.SpringDocConfiguration.apiInfo")
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Katabank API - Account and Transaction Management System")
                                .description("The Katabank API enables the management of financial operations related to bank accounts. It provides functionalities for performing account transfers, querying historical transactions, and managing credits and debits securely and efficiently.")
                                .contact(new Contact()
                                        .name("Ricardo Ariel Povolo")
                                        .email("rpovolo@gmail.com")
                                )
                                .version("1.0.0")
                );
    }
}
