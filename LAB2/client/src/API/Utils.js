let access_token =""

function getToken(){
    return access_token
}

function setToken(token){
    access_token=token
}

export default {getToken,setToken};
