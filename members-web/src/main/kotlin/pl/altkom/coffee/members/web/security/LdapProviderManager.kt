package pl.altkom.coffee.members.web.security

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl
import pl.altkom.coffee.members.domain.service.UserService


class LdapProviderManager(
        ldapProviders: MutableList<AuthenticationProvider>,
        private val userService: UserService) : ProviderManager(ldapProviders) {

    override fun authenticate(authentication: Authentication?): Authentication {
        val auth = super.authenticate(authentication)
        return when (auth.isAuthenticated) {
            true -> authenticateUser(auth)
            false -> auth
        }
    }

    private fun authenticateUser(user: Authentication): Authentication {
        val userDetails = user.principal as LdapUserDetailsImpl
        val authenticatedUser = when (userService.exists(userDetails.username)) {
            true -> userService.loadUserByUsername(userDetails.username)
            false -> userService.registerUser(userDetails.username)
        }
        val principal = User(userDetails.username, "", AuthorityUtils.commaSeparatedStringToAuthorityList(authenticatedUser.roles))

        return UsernamePasswordAuthenticationToken(principal, "", AuthorityUtils.commaSeparatedStringToAuthorityList(authenticatedUser.roles))
    }

}
