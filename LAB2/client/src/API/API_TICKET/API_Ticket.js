const url = 'http://localhost:8081/';

async function addTicket(description =""){

    let response;
    try{
        response = await fetch(url+"API/ticket/create", {
            method: 'POST',
            headers : { 'Content-Type' : 'application/json' , 'Authorization': 'Bearer '+ localStorage.getItem("jwt")},
            body: description,
        });
        if(response && response.ok){
            return await response.json()
        }
        else{
            throw {status:response.status,detail:"Cannot communicate with server",instance:"/API/ticket/create"}
        }
    }catch (e) {
        throw {status:404,detail:"Cannot communicate with server",instance:"/API/ticket/create"}
    }
}

async function updateTicketPriorityAndWorker(ticket ={},priority=-1,workerUsername=""){

    let response;
    try{
        if(ticket && priority!=-1 && workerUsername!=""){
            response = await fetch(url+"API/ticket/start?priority="+priority, {
                method: 'POST',
                headers : { 'Content-Type' : 'application/json' , 'Authorization': 'Bearer '+ localStorage.getItem("jwt")},
                body: JSON.stringify({ "ticket" : ticket, "workerUsername": workerUsername}),
            });
            if(response && response.ok){
                return await response.json()
            }
            else{
                throw {status:response.status,detail:"Cannot communicate with server",instance:"/API/ticket/start"}
            }
        }
        else{
            throw {status:500,detail:"Cannot communicate with server",instance:"/API/ticket/start"}
        }

    }catch (e) {
        throw {status:e.status,detail:"Cannot communicate with server",instance:"API/ticket/start/"}
    }
}

async function getListTicketByStatus(status){
    try{
        let response =await fetch(url + "API/ticket/getListTicketByStatus?status="+status, {
            headers : { 'Content-Type' : 'application/json' , 'Authorization': 'Bearer '+ localStorage.getItem("jwt")},
        })
        if(response && response.ok){
            return await response.json()
        }
        else{
            throw {status:response.status,detail:"Cannot communicate with server",instance:"/API/ticket/create"}
            return []
        }

    }
    catch (e) {
        console.error(e)
        throw {status:e.status,detail:"Cannot communicate with server"}
        return []
    }
}

async function getTicketByUsername(username=""){
    try{
        let response =await fetch(url + "API/ticket/getListTicketByUsername", {
            headers : { 'Content-Type' : 'application/json' , 'Authorization': 'Bearer '+ localStorage.getItem("jwt")},
        })
        if(response && response.ok){
            return await response.json()
        }
        else{
            throw {status:response.status,detail:"Cannot communicate with server",instance:"/API/ticket/getListTicketByUsername"}
            return []
        }

    }
    catch (e) {
        console.error(e)
        throw {status:e.status,detail:"Cannot communicate with server",instance:"/API/ticket/getListTicketByUsername"}
        return []
    }
}


async function getListUserByRole(roleName){
    let response;
    try{
        response = await fetch(url+"user/getUsersByRole?roleName="+roleName, {
            headers : { 'Content-Type' : 'application/json' , 'Authorization': 'Bearer '+ localStorage.getItem("jwt")},
        });
        if(response.ok){
            return response.json()
        }
    }catch (e) {
        throw {status:e.status,detail:"Cannot communicate with server",instance:"/API/getListExpert"}
    }
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

export default {addTicket,getListUserByRole,getListTicketByStatus,updateTicketPriorityAndWorker,getTicketByUsername};
