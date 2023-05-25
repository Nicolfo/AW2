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

        http.authorizeHttpRequests()
            .requestMatchers("/user/validate/").permitAll()//mettere url da garantire a tutti
            .requestMatchers("/user/signup").permitAll()
            //.requestMatchers("/user/get_info").permitAll()
            //.requestMatchers("/API/ticket/start/**").hasAuthority("ROLE_Manager")
            //.requestMatchers("/API/ticket/stop/**").authenticated()
//            .requestMatchers("/API/ticket/close/**").hasAuthority("ROLE_Manager")
//            .requestMatchers("/API/ticket/resolve/**").hasAuthority("ROLE_Manager")
//            .requestMatchers("/API/ticket/reopen/**").hasAuthority("ROLE_Manager")
            .anyRequest().permitAll()
            .and()
            .oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(jwtAuthConverter)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    return http.build()
}

}
