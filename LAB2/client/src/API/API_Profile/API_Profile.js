const url = 'http://localhost:8080/';
let response;

async function getProfile(email=""){
    let tmpUser = null ;
    try{
        if(email.trim()!="" && email.match('[a-z0-9]+@[a-z]+\.[a-z]{2,3}')){
            response = await fetch(url+"API/profiles/"+email);
            if(response && response.ok){
                tmpUser = await response.json();
            }
        }
    }
    catch(err){
        console.log(err);
        return tmpUser;
    }

    return tmpUser;
}

async function addProfile(addedUser=null){
    let tmpUser = null ;
    try{
        response = await fetch(url+"API/profiles/", {

            method: 'POST',
            headers : { 'Content-Type' : 'application/json'},
            body: JSON.stringify({ "name" : addedUser.name, "email" :addedUser.email}),
        });

        if(response && response.ok){
            tmpUser = await response.json();
        }
    }
    catch(err){
        console.log(err);
        return tmpUser
    }

    return tmpUser;
}

async function updateProfile(updatedUser=null){
    let tmpUser = null ;
    try{
        response = await fetch(url+"API/profiles/"+updatedUser.email, {

            method: 'PUT',
            headers : { 'Content-Type' : 'application/json'},
            body: JSON.stringify({ "name" : updatedUser.name, "email" :updatedUser.email}),
        });

        if(response && response.ok){
            tmpUser = await response.json();
        }
    }
    catch(err){
        console.log(err);
        return tmpUser
    }

    return tmpUser;
}

export default {getProfile,updateProfile,addProfile};
