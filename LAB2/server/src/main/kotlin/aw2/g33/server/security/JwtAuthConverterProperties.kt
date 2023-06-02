package aw2.g33.server.security


import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated



@Validated
@Configuration
@ConfigurationProperties(prefix = "jwt.auth.converter")
class JwtAuthConverterProperties {
    private var resourceId: String? = null
    private var principalAttribute: String? = null
    fun getPrincipalAttribute():String?{
        return this.principalAttribute
    }
    fun setPrincipalAttribute(principalAttribute:String){
        this.principalAttribute=principalAttribute
    }
    fun setResourceId(resourceId:String){
        this.resourceId=resourceId
    }
    fun getResourceId():String?{
        return this.resourceId
    }
}