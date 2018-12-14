package pl.altkom.coffee.members.domain.service

import org.springframework.stereotype.Service
import pl.altkom.coffee.members.api.dto.UserDto
import pl.altkom.coffee.members.api.enums.UserRole
import pl.altkom.coffee.members.domain.model.User
import pl.altkom.coffee.members.domain.repository.UserRepository
import kotlin.streams.toList

@Service
class UserService(private val userRepository: UserRepository) {

    fun loadUserByUsername(userName: String): User {
        return userRepository.findByName(userName)
    }

    fun registerUser(userName: String): User {
        return userRepository.save(User(userName))
    }

    fun findAllUser(): List<UserDto> {
        return userRepository.findAll().stream().map { it ->
            UserDto(it.name,
                    it.roles.split(",")
                            .filter {
                                it.isNotBlank()
                            }
                            .map {
                                UserRole.valueOf(it)
                            }
            )
        }.toList()
    }

    fun updateRole(userDto: UserDto) {
        val user = userRepository.findByName(userDto.username)
        user.roles = userDto.roles.joinToString(",")
        userRepository.save(user)
    }

    fun exists(userName: String): Boolean {
        return userRepository.existsByName(userName)
    }

}