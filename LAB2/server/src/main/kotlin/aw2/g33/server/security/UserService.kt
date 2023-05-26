package aw2.g33.server.security

import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.RoleRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import javax.ws.rs.core.Response

@Service
class UserService(
    private val keycloak: Keycloak,
    @Value("\${keycloak.realm}")
    private val realm: String
) {
    /*
    fun findAll(): List<UserRepresentation> =
        keycloak
            .realm(realm)
            .users()
            .list()
    fun findByUsername(username: String): List<UserRepresentation> =
        keycloak
            .realm(realm)
            .users()
            .search(username)
    fun findById(id: String): UserRepresentation =
        keycloak
            .realm(realm)
            .users()
            .get(id)
            .toRepresentation()
    */

    fun findRoleByName(roleName: String): RoleRepresentation =
        keycloak
            .realm(realm)
            .roles()
            .get(roleName)
            .toRepresentation()

    fun assignRole(userId: String, roleRepresentation: RoleRepresentation) {
        keycloak
            .realm(realm)
            .users()
            .get(userId)
            .roles()
            .realmLevel()
            .add(listOf(roleRepresentation))
    }


    fun create(request: UserDTO): Response {
        val password = preparePasswordRepresentation(request.password)
        val user = prepareUserRepresentation(request, password)
        return keycloak
            .realm(realm)
            .users()
            .create(user)
    }



    private fun preparePasswordRepresentation(
        password: String
    ): CredentialRepresentation {
        val cR = CredentialRepresentation()
        cR.isTemporary = false
        cR.type = CredentialRepresentation.PASSWORD
        cR.value = password
        return cR
    }

    private fun prepareUserRepresentation(
        request: UserDTO,
        cR: CredentialRepresentation
    ): UserRepresentation {
        val newUser = UserRepresentation()
        newUser.username = request.username
        if(request.email!=null)
            newUser.email=request.email
        newUser.credentials = listOf(cR)
        newUser.isEnabled = true
        return newUser
    }
}