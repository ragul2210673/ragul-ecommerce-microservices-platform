package com.ecommerce.orderservice.config;

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
    public OpenAPI orderServiceAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:8084");
        server.setDescription("Order Service - Development");

        Contact contact = new Contact();
        contact.setName("Development Team");
        contact.setEmail("dev@ecommerce.com");

        Info info = new Info()
                .title("Order Service API")
                .version("1.0.0")
                .description("Microservice for order processing and tracking")
                .contact(contact);

        return new OpenAPI().info(info).servers(List.of(server));
    }
}