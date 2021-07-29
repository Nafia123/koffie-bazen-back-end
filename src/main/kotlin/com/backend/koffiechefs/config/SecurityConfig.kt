package com.backend.koffiechefs.config

import com.backend.koffiechefs.filter.SecurityFilter
import com.backend.koffiechefs.service.TokenService
import com.braintreegateway.util.Http
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import com.google.common.collect.ImmutableList
import org.springframework.context.annotation.Bean

import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(private val tokenService: TokenService) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http?.csrf()?.disable()?.cors()
                ?.and()?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ?.and()
                ?.authorizeRequests()
                ?.anyRequest()?.authenticated()
                ?.and()
                ?.addFilterBefore(
                        SecurityFilter(tokenService),
                        UsernamePasswordAuthenticationFilter::class.java
                )
    }

    override fun configure(web: WebSecurity?) {
        web
                ?.ignoring()
                ?.antMatchers(HttpMethod.GET, "/coffees")
                ?.antMatchers(HttpMethod.GET, "/coffees/**")
                ?.antMatchers(HttpMethod.GET, "/quiz-questions")
                ?.antMatchers(HttpMethod.GET, "/quiz-questions/**")
                ?.antMatchers(HttpMethod.POST, "/payment/**")
                ?.antMatchers(HttpMethod.POST, "/register")
                ?.antMatchers(HttpMethod.POST, "/roaster")
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = ImmutableList.of("*")
        configuration.allowedMethods = ImmutableList.of("*")
        configuration.allowCredentials = true
        configuration.allowedHeaders = ImmutableList.of("Authorization", "Cache-Control", "Content-Type")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
