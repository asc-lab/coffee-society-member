package pl.altkom.coffee.members.api.dto

import pl.altkom.coffee.members.api.enums.UserRole

data class UserDto(
        val username: String,
        val roles: List<UserRole>)