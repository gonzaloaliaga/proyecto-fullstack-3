package cl.donaton.donaton.config

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.ShallowEtagHeaderFilter

@Configuration
class EtagConfig {

    @Bean
    fun shallowEtagHeaderFilterRegistration(): FilterRegistrationBean<ShallowEtagHeaderFilter> {
        val registration = FilterRegistrationBean<ShallowEtagHeaderFilter>()
        
        // 1. Usamos el método setFilter nativo en lugar de asignación directa
        registration.setFilter(ShallowEtagHeaderFilter())
        
        // Aplica a todas las rutas de la API normal
        registration.addUrlPatterns("/*")
        
        // Excluimos explícitamente las rutas de Swagger para evitar el Error 500
        registration.addInitParameter("exclusions", "/v3/api-docs, /v3/api-docs/*, /swagger-ui/*, /swagger-ui.html")
        
        // 2. Usamos el método setName nativo para evitar conflictos de visibilidad
        registration.setName("shallowEtagHeaderFilter")
        registration.order = 1
        
        return registration
    }
}