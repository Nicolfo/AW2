const url = 'http://localhost:8081/';
async function getAllTicketLog(){
    try{
        let response =await fetch(url + "/API/ticketLog/getAll", {
            headers : { 'Content-Type' : 'application/json' , 'Authorization': 'Bearer '+ localStorage.getItem("jwt")},
        })
        if(response && response.ok){
            return await response.json()
        }
        else{
            throw {status:response.status,detail:"Cannot communicate with server",instance:"/API/ticketLog/getAll"}
            return []
        }

    }
    catch (e) {
        console.error(e)
        throw {status:e.status,detail:"Cannot communicate with server"}
        return []
    }
}
export default {getAllTicketLog}