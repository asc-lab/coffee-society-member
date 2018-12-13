package pl.altkom.coffee.members.api.enums

import org.springframework.security.core.GrantedAuthority

enum class UserRole : GrantedAuthority {
    ADMIN, MEMBER, BARISTA, ACCOUNTANT;

    override fun getAuthority(): String {
        return name
    }
}