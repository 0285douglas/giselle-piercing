package com.gisellepiercing.application.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(
                        new Info()
                                .title("Giselle Piercing API")
                                .version("1.0.0")
                                .description("API REST para E-commerce de Piercings de Luxo - Giselle Piercing Store\n\n" +
                                        "Backend desenvolvido com Spring Boot 3.2.5 utilizando arquitetura em camadas.\n\n" +
                                        "**Funcionalidades Principais:**\n" +
                                        "- Autenticação e autorização com JWT\n" +
                                        "- Gerenciamento de produtos com filtros\n" +
                                        "- Carrinho de compras\n" +
                                        "- Integração com Mercado Pago para pagamentos (PIX, Boleto, Cartão de Crédito)\n" +
                                        "- Webhooks para notificação de pagamentos\n" +
                                        "- Controle de estoque\n" +
                                        "- Alertas de estoque baixo por Email e WhatsApp\n\n" +
                                        "**Segurança:**\n" +
                                        "- Autenticação via JWT Bearer Token\n" +
                                        "- Criptografia de senhas com BCrypt\n" +
                                        "- Controle de acesso por roles (ADMIN, CUSTOMER)\n" +
                                        "- Validação de entrada com Jakarta Bean Validation"
                                )
                                .contact(
                                        new Contact()
                                                .name("Douglas Farias")
                                                .email("0285douglas@gmail.com")
                                                .url("https://www.linkedin.com/in/dev-douglas-alves/")
                                )
                                .license(
                                        new License()
                                                .name("Apache 2.0")
                                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")
                                )
                )
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .description("JWT token obtido no endpoint /auth/login\n\n" +
                                                        "Formato: Authorization: Bearer {token}")
                                )
                )
                .tags(Arrays.asList(
                        new Tag().name("Auth").description("Autenticação e Registro de Usuários"),
                        new Tag().name("Products").description("Gerenciamento de Produtos"),
                        new Tag().name("Cart").description("Operações de Carrinho"),
                        new Tag().name("Checkout").description("Processamento de Pedidos"),
                        new Tag().name("Webhook").description("Webhooks para Integração com Mercado Pago")
                ));
    }
}

