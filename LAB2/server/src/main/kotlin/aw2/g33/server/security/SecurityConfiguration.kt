package aw2.g33.server.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfiguration (private val jwtAuthConverter: JwtAuthConverter) {


    @Bean fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
            .formLogin().disable()

        http.cors().and()
            .authorizeHttpRequests()
            .requestMatchers("/user/validate/").permitAll()
            .requestMatchers("/user/signup").anonymous()
            .requestMatchers("/user/createExpert").hasRole("Manager")
            .anyRequest().permitAll()
            .and()
            .oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(jwtAuthConverter)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    return http.build()
}

}
