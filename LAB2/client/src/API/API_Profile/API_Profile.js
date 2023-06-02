const url = 'http://localhost:8081/';

async function getProfile(username=undefined){

    let tmpUser = null ;
    let response;
    try{
        response = await fetch(url+"API/profiles/"+username);
        tmpUser = await response.json();
    }catch (e) {
        throw {status:404,detail:"Cannot communicate with server",instance:"/API/profiles/{"+username+"}"}
    }


    if(response.ok)
        return tmpUser;
    else
        throw tmpUser;

}

async function addProfile(addedUser=null){
    let tmpUser = null ;
    let response;
    try{
        response = await fetch(url+"API/profiles/", {
            method: 'POST',
            headers : { 'Content-Type' : 'application/json'},
            body: JSON.stringify({ "username" : addedUser.username, "email" :addedUser.email,"role":addedUser.role}),
        });
        tmpUser = await response.json();
    }catch (e) {
        throw {status:404,detail:"Cannot communicate with server",instance:"/API/profiles/"}
    }


    if(response.ok)
        return tmpUser;
    else
        throw tmpUser;


}

async function updateProfile(oldMail,updatedUser){
    let tmpUser = null ;
    let response;
    try {
        response = await fetch(url + "API/profiles/" + oldMail, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({"username": updatedUser.username, "email": updatedUser.email,"role": updatedUser.role}),
        });
        tmpUser = await response.json();
    }catch (e) {
        throw {status:404,detail:"Cannot communicate with server",instance:"/API/profiles/"+oldMail}
    }
        if(response.ok)
            return tmpUser;
        else
            throw tmpUser;
}





export default {getProfile,updateProfile,addProfile};
