package pl.altkom.coffee.members.web.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import pl.altkom.coffee.members.api.dto.UserDto
import pl.altkom.coffee.members.domain.service.UserService


@RestController
@RequestMapping("/api")
class UserController(private val userService: UserService) {

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    fun users(): ResponseEntity<List<UserDto>> {
        return ResponseEntity.ok().body(userService.findAllUser())
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/updateRole")
    fun updateRole(@RequestBody userDto: UserDto) {
        return userService.updateRole(userDto)
    }

}