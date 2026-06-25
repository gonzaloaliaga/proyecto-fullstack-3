package cl.donaton.donaton.security

import org.springframework.stereotype.Component
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtValidationFilter(private val jwtService: JwtService) : OncePerRequestFilter() {

    private val publicPaths = listOf(
        "/api/auth/login",
        "/api/swagger-ui",
        "/api/v3/api-docs",
        "/swagger-ui",
        "/v3/api-docs"
    )

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val path = request.servletPath

        if (request.method.equals("OPTIONS", ignoreCase = true)) {
            filterChain.doFilter(request, response)
            return
        }

        if (publicPaths.any { path.startsWith(it) }) {
            filterChain.doFilter(request, response)
            return
        }

        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acceso denegado: Token inexistente")
            return
        }

        val token = authHeader.substring(7)
        val userId = jwtService.validateAndExtractId(token)

        if (userId == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acceso denegado: Token inválido")
            return
        }

        request.setAttribute("authenticatedUserId", userId)
        filterChain.doFilter(request, response)
    }
}
