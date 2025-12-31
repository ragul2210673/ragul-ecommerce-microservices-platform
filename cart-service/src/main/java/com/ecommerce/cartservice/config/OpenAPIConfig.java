package com.ecommerce.cartservice.config;

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
    public OpenAPI cartServiceAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:8083");
        server.setDescription("Cart Service - Development");

        Contact contact = new Contact();
        contact.setName("Development Team");
        contact.setEmail("dev@ecommerce.com");

        Info info = new Info()
                .title("Cart Service API")
                .version("1.0.0")
                .description("Microservice for shopping cart operations")
                .contact(contact);

        return new OpenAPI().info(info).servers(List.of(server));
    }
}