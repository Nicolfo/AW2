package aw2.g33.server.security

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.UserResource
import org.keycloak.admin.client.resource.UsersResource
import org.keycloak.representations.idm.ClientRepresentation
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.RoleRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import javax.ws.rs.core.Response


@Service
class   UserService(
    private val keycloak: Keycloak,
    @Value("\${keycloak.realm}")
    private val realm: String
) {

    fun findByUsername(username: String): List<UserRepresentation> =
        keycloak
            .realm(realm)
            .users()
            .search(username)
    fun findRoleByName(roleName: String): RoleRepresentation =
        keycloak
            .realm(realm)
            .roles()
            .get(roleName)
            .toRepresentation()

    fun getRoleById(id:String): List<String> {

        return keycloak
            .realm(realm)
            .users()
            .get(id)
            .roles()
            .realmLevel()
            .listAll()
            .map { it.toString() }
    }



    fun assignRoleWithUsername(username: String, roleRepresentation: RoleRepresentation) {
        var resultSearch= findByUsername(username)
        if (resultSearch.isEmpty()){
            throw UsernameNotFoundException("Cannot find the username")
        }

        val user=resultSearch.first()

        keycloak
            .realm(realm)
            .users()
            .get(user.id)
            .roles()
            .realmLevel()
            .add(listOf(roleRepresentation))
    }

    fun updateUser(usernameOld:String,userDTO: UserDTO, roleName: String) {         //da vedere
        var resultSearch= findByUsername(usernameOld)
        if (resultSearch.isEmpty()){
            throw UsernameNotFoundException("Cannot find the username")
        }
        val role = findRoleByName(roleName);
        val user=resultSearch.first()

        keycloak
            .realm(realm)
            .users()
            .get(user.id)
            .update(prepareUserRepresentation(userDTO, preparePasswordRepresentation(userDTO.password)));

        keycloak
            .realm(realm)
            .users()
            .get(user.id)
            .roles()
            .realmLevel()
            .listAll()
            .clear()
        keycloak
            .realm(realm)
            .users()
            .get(user.id)
            .roles()
            .realmLevel()
            .add(listOf(findRoleByName(roleName)))

    }

    fun getListUserByRole(roleName: String):List<UserDTO>{
        return keycloak.realm(realm).roles().get(roleName).getUserMembers().filter { user -> user.isEnabled }.map { user -> UserDTO(user.email,user.username,"")};
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
