package pl.altkom.coffee.members.domain.model

import org.springframework.data.annotation.Id

class User(var name: String) {
    @Id
    var id: String? = null
    var roles: String = ""

}