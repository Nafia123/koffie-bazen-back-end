package com.backend.koffiechefs.filter

import com.backend.koffiechefs.service.TokenService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class SecurityFilter(private val tokenService: TokenService) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")

        if (authorizationHeaderIsInvalid(authorizationHeader)) {
            filterChain.doFilter(request, response)
        } else {
            val token: UsernamePasswordAuthenticationToken = createToken(authorizationHeader)

            SecurityContextHolder.getContext().authentication = token
            filterChain.doFilter(request, response)
        }
    }

    private fun authorizationHeaderIsInvalid(authorizationHeader: String?): Boolean {
        return (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ") || !tokenService.isTokenValid(
            authorizationHeader
        ))
    }

    private fun createToken(authorizationHeader: String?): UsernamePasswordAuthenticationToken {
        val token = authorizationHeader?.replace("Bearer ", "")
        val user = tokenService.parseToken(token)

        val authorities: MutableList<GrantedAuthority> = ArrayList()

        when (user.role) {
            "0" -> {
                authorities.add(SimpleGrantedAuthority("ADMIN"))
            }
            "1" -> {
                authorities.add(SimpleGrantedAuthority("ROASTER"))
            }
            "2" -> {
                authorities.add(SimpleGrantedAuthority("USER"))
            }
        }

        return UsernamePasswordAuthenticationToken(user, null, authorities)
    }
}
