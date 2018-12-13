package pl.altkom.coffee.members.web.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore


@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfig(
        val authenticationManager: AuthenticationManager,
        val jwtSettingsHolder: JwtSettingsHolder)
    : AuthorizationServerConfigurerAdapter() {


    @Throws(Exception::class)
    override fun configure(configurer: ClientDetailsServiceConfigurer?) {
        configurer!!
                .inMemory()
                .withClient(jwtSettingsHolder.clientId)
                .secret(jwtSettingsHolder.clientSecret)
                .authorizedGrantTypes(jwtSettingsHolder.grantType)
                .scopes(jwtSettingsHolder.scopeRead, jwtSettingsHolder.scopeWrite, jwtSettingsHolder.trust)
                .accessTokenValiditySeconds(jwtSettingsHolder.accessTokenValiditySeconds!!)
                .refreshTokenValiditySeconds(jwtSettingsHolder.refreshTokenValiditySeconds!!)
    }

    @Bean
    fun tokenStore(): TokenStore {
        return JwtTokenStore(accessTokenConverter())
    }

    @Bean
    fun accessTokenConverter(): JwtAccessTokenConverter {
        val converter = JwtAccessTokenConverter()
        converter.setSigningKey(jwtSettingsHolder.signingKey)
        return converter
    }


    @Throws(Exception::class)
    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer?) {
        endpoints!!.tokenStore(tokenStore())
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .authenticationManager(authenticationManager)
                .accessTokenConverter(accessTokenConverter())
    }

    @Throws(Exception::class)
    override fun configure(oauthServer: AuthorizationServerSecurityConfigurer?) {
        oauthServer!!
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")

    }
}