package pl.altkom.coffee.members.web.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.*
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import pl.altkom.coffee.members.api.enums.UserRole
import java.util.*

@Configuration
@ConditionalOnProperty(name = ["mockSecurity"], havingValue = "true")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class TestSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(activeDirectoryLdapAuthenticationProvider())
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
                .anonymous().disable()
                .authorizeRequests()
                .antMatchers("*/**").permitAll()
    }


    @Bean
    fun corsFilter(): FilterRegistrationBean<CorsFilter> {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", config)
        val bean = FilterRegistrationBean(CorsFilter(source))
        bean.order = 0
        return bean
    }

    private fun activeDirectoryLdapAuthenticationProvider(): AuthenticationProvider {
        return MockAuthenticationProvider()

    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManager(): AuthenticationManager {
        return MockAuthenticaionProviderManager(Arrays.asList(activeDirectoryLdapAuthenticationProvider()))
    }

    class MockAuthenticaionProviderManager(ldapProviders: MutableList<AuthenticationProvider>) : ProviderManager(ldapProviders) {

        override fun authenticate(authentication: Authentication?): Authentication {
            val auth = super.authenticate(authentication)
            return when (auth.isAuthenticated) {
                true -> authenticateUser(auth)
                false -> auth
            }
        }

        private fun authenticateUser(user: Authentication): Authentication {
            val userDetails = user.principal as LdapUserDetailsImpl
            val principal = User(userDetails.username, "", user.authorities)
            return UsernamePasswordAuthenticationToken(principal, "", user.authorities)
        }

    }

    class MockAuthenticationProvider : AuthenticationProvider {
        override fun authenticate(authentication: Authentication?): Authentication {

            val userToken = authentication as UsernamePasswordAuthenticationToken

            val username = userToken.name
            val password = authentication.getCredentials() as String

            if (username != password) {
                throw BadCredentialsException("Bad creds")
            }

            return when {
                "barista" in username -> createAuthentication(username, password, listOf(UserRole.BARISTA))
                "accountant" in username -> createAuthentication(username, password, listOf(UserRole.ACCOUNTANT))
                "admin" in username -> createAuthentication(username, password, listOf(UserRole.ADMIN))
                "member" in username -> createAuthentication(username, password, listOf(UserRole.MEMBER))
                "god" in username -> createAuthentication(username, password, UserRole.values().toList())
                else -> throw BadCredentialsException("Bad creds")
            }

        }

        private fun createAuthentication(username: String?, password: String, roles: List<UserRole>): Authentication {
            val essence = LdapUserDetailsImpl.Essence()
            essence.setDn(username)
            essence.setUsername(username)
            return UsernamePasswordAuthenticationToken(essence.createUserDetails(), password, roles)
        }

        override fun supports(authentication: Class<*>?): Boolean {
            return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
        }


    }

}