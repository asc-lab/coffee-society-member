package pl.altkom.coffee.members.domain.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import pl.altkom.coffee.members.domain.model.User

@Repository
interface UserRepository : MongoRepository<User, String> {
    fun existsByName(name: String): Boolean
    fun findByName(name: String): User

}
