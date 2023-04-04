const url = 'http://localhost:8080/';
let response;

async function getProfile(email=undefined){

    let tmpUser = null ;

        /*if(email.trim()!="" && email.match('[a-z0-9]+@[a-z]+\.[a-z]{2,3}')){
            response = await fetch(url+"API/profiles/"+email);
            if(response && response.ok){
                tmpUser = await response.json();
            }
        }*/

    response = await fetch(url+"API/profiles/"+email);
    tmpUser = await response.json();

    if(response.ok)
        return tmpUser;
    else
        throw tmpUser;

}

async function addProfile(addedUser=null){
    let tmpUser = null ;

        response = await fetch(url+"API/profiles/", {

            method: 'POST',
            headers : { 'Content-Type' : 'application/json'},
            body: JSON.stringify({ "name" : addedUser.name, "email" :addedUser.email}),
        });


            tmpUser = await response.json();


    if(response.ok)
        return tmpUser;
    else
        throw tmpUser;


}

async function updateProfile(oldMail,updatedUser){
    let tmpUser = null ;

        response = await fetch(url+"API/profiles/"+oldMail, {
            method: 'PUT',
            headers : { 'Content-Type' : 'application/json'},
            body: JSON.stringify({ "name" : updatedUser.name, "email" :updatedUser.email}),
        });


        tmpUser = await response.json();
        if(response.ok)
            return tmpUser;
        else
            throw tmpUser;
}





export default {getProfile,updateProfile,addProfile};
