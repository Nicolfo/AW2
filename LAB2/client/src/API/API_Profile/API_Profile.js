const url = 'http://localhost:8080/';

async function getProfile(email=undefined){

    let tmpUser = null ;
    let response;
    try{
        response = await fetch(url+"API/profiles/"+email);
        tmpUser = await response.json();
    }catch (e) {
        throw {status:404,detail:"Cannot communicate with server",instance:"/API/profiles/{"+email+"}"}
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
            body: JSON.stringify({ "name" : addedUser.name, "email" :addedUser.email}),
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
            body: JSON.stringify({"name": updatedUser.name, "email": updatedUser.email}),
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
