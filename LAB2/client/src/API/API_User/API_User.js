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

async function signup(username,email,password){
    let response = false;

    try{
        response = await fetch(url +"user/signup", {
            method: 'POST',
            headers : { 'Content-Type' : 'application/json'},
            body: JSON.stringify({ "username" : username, "email" :email, "password" :password})
        });

    }catch (e) {
        throw {status:404,detail:"Cannot communicate with server",instance:'/user/signup'}
    }

    if(response.ok){
        return true;
    }

    else
        throw {status:500,detail:"Internal Server Error",instance:'/user/signup'};
}

async function managerCreateUser(username,email,password,role,jwt){
    console.log(jwt)
    let response = false;
    try{
        response = await fetch(url+"user/createUser/"+role, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwt}`
            },
            body: JSON.stringify({ "username" : username, "email" :email, "password" :password})
        });
    }catch (e) {
        throw {status:404,detail:"Cannot communicate with server",instance:"user/createUser"}
    }

    if(response.ok)
        return true;
    else
        throw {status:response.status,detail:"Internal Server Error",instance:'user/createUser'};
}



export default {login, signup, managerCreateUser}