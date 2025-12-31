package com.ecommerce.paymentservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI paymentServiceAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:8085");
        server.setDescription("Payment Service - Development");

        Contact contact = new Contact();
        contact.setName("Development Team");
        contact.setEmail("dev@ecommerce.com");

        Info info = new Info()
                .title("Payment Service API")
                .version("1.0.0")
                .description("Microservice for payment processing and refunds")
                .contact(contact);

        return new OpenAPI().info(info).servers(List.of(server));
    }
}