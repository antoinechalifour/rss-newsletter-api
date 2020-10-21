package dev.antoinechalifour.newsletter.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class HttpBasicAuthAdapter : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .httpBasic()
    }

    @Autowired
    fun configureGlobal(
        auth: AuthenticationManagerBuilder,
        @Value("\${newsletter.auth.username}") username: String,
        @Value("\${newsletter.auth.password}") password: String
    ) {
        auth.inMemoryAuthentication()
            .withUser(username)
            .password("{noop}$password")
            .roles("USER")
    }
}
