package vn.io.nguyen32.crm.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

  @Bean
  public OpenAPI customizeOpenAPI() {
    final String schemeName = "bearer-jwt";
    return new OpenAPI()
        .info(new Info()
            .title("CRM API")
            .version("v1.0.0")
            .description("API documentation for CRM application"))
        .components(new Components()
            .addSecuritySchemes(schemeName, new SecurityScheme()
                .name(schemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .description("Enter JWT token with prefix 'Bearer '")))
        .addSecurityItem(new SecurityRequirement().addList(schemeName));
  }
}
