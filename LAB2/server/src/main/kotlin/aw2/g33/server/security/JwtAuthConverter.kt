package aw2.g33.server.security

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimNames
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.stereotype.Component
import java.util.stream.Collectors
import java.util.stream.Stream


@Component
class JwtAuthConverter(private val properties: JwtAuthConverterProperties) : Converter<Jwt?, AbstractAuthenticationToken?> {
    private val jwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()
    override fun convert(jwt: Jwt): AbstractAuthenticationToken {

        val authorities: Collection<GrantedAuthority> = Stream.concat(
            jwtGrantedAuthoritiesConverter.convert(jwt)!!.stream(),
            extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet())
        return JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt))
    }

    private fun getPrincipalClaimName(jwt: Jwt): String {
        var claimName: String? = JwtClaimNames.SUB
        if (properties.getPrincipalAttribute() != null) {
            claimName = properties.getPrincipalAttribute()
        }
        return jwt.getClaim(claimName)
    }

    private fun extractResourceRoles(jwt: Jwt): Collection<GrantedAuthority> {
        val resourceAccess: Map<String, Any> = jwt.getClaim("resource_access")
        try {
            val resource: Map<String?, Any?> = resourceAccess[properties.getResourceId()] as Map<String?, Any?>
            var resourceRoles: Collection<String> = resource["roles"] as Collection<String>;
            return resourceRoles.stream().map { SimpleGrantedAuthority("ROLE_" + it) }.collect(Collectors.toSet())
        }catch(ex:Exception){
            return emptySet()
        }
    }
}