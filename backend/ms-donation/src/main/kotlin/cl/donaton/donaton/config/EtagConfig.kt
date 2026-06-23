package cl.donaton.donaton.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.ShallowEtagHeaderFilter

@Configuration
class EtagConfig {

    @Bean
    fun shallowEtagHeaderFilter(): ShallowEtagHeaderFilter {
        // Este filtro intercepta la respuesta del BFF, calcula su tamaño en bytes,
        // inyecta la cabecera 'Content-Length' fija y ELIMINA el 'Transfer-Encoding: chunked'
        return ShallowEtagHeaderFilter()
    }
}