package pl.altkom.coffee.members.web.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class JwtSettingsHolder {

    @Value("\${jwt.clientId}")
    var clientId: String? = null

    @Value("\${jwt.clientSecret}")
    var clientSecret: String? = null

    @Value("\${jwt.grantType}")
    var grantType: String? = null

    @Value("\${jwt.authorizationCode}")
    var authorizationCode: String? = null

    @Value("\${jwt.refreshToken}")
    var refreshToken: String? = null

    @Value("\${jwt.implicit}")
    var implicit: String? = null

    @Value("\${jwt.scopeRead}")
    var scopeRead: String? = null

    @Value("\${jwt.scopeWrite}")
    var scopeWrite: String? = null

    @Value("\${jwt.trust}")
    var trust: String? = null

    @Value("\${jwt.accessTokenValiditySeconds}")
    var accessTokenValiditySeconds: Int? = null

    @Value("\${jwt.refreshTokenValiditySeconds}")
    var refreshTokenValiditySeconds: Int? = null

    @Value("\${jwt.signingKey}")
    var signingKey: String? = null

}

