const url = 'http://localhost:8081/';

async function login(username,password){
    let userJwt = null ;
    let response;
    try{
        response = await fetch(url+"user/validate/", {
            method: 'POST',
            headers : { 'Content-Type' : 'application/json'},
            body: JSON.stringify({ "username" : username, "password" :password}),
        });
        userJwt = await response.json();
    }catch (e) {
        throw {status:404,detail:"Cannot communicate with server",instance:"/API/profiles/"}
    }


    if(response.ok)
        return userJwt;
    else
        throw userJwt;
}
export default {login}