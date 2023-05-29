const url = 'http://localhost:8081/';

async function login(username,password){
    let respJson;
    let response;
    try{
        response = await fetch(url+"user/validate/", {
            method: 'POST',
            headers : { 'Content-Type' : 'application/json'},
            body: JSON.stringify({ "username" : username, "password" :password}),
        });
        respJson = await response.json();
    }catch (e) {
        throw {status:404,detail:"Cannot communicate with server",instance:"/user/validate/"}
    }

    if(response.ok && respJson.error==null)
        return respJson.access_token; //userJwt
    else
        throw respJson;
}

async function signup(username,email,password,isManager){
    let response = false;
    const targetURL =
        isManager ?
        url + '/user/createExpert'
        :
        url + '/user/signup';

    try{
        response = await fetch(targetURL, {
            method: 'POST',
            headers : { 'Content-Type' : 'application/json'},
            body: JSON.stringify({ "username" : username, "email" :email, "password" :password}),
        });
        response = await response.json();
    }catch (e) {
        throw {status:404,detail:"Cannot communicate with server",instance:targetURL}
    }

    if(response.ok)
        return true;
    else
        throw response;
}



export default {login, signup}