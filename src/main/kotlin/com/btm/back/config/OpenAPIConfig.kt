package com.btm.back.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * SpringDoc OpenAPI 配置类
 * 用于配置Swagger UI文档
 * @author Trae AI
 * @date 2023-08-01
 */
@Configuration
class OpenAPIConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            // 正确设置OpenAPI版本 - 不需要显式设置，springdoc会自动处理
            .info(apiInfo())
            .components(
                Components()
                    .addSecuritySchemes(
                        "bearerAuth",
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            )
    }

    private fun apiInfo(): Info {
        return Info()
            .title("API文档")
            .description("API接口文档")
            .version("1.0.0")
            .contact(
                Contact()
                    .name("API Support")
                    .email("support@example.com")
            )
            .license(
                License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html")
            )
    }
}