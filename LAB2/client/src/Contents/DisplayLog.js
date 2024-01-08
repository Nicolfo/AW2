import {useEffect, useState} from "react";
import Form from "react-bootstrap/Form";
import SearchIcon from '@mui/icons-material/Search';
import API_TicketLog from "../API/API_TicketLog/API_TicketLog";
import Button from '@mui/joy/Button';
import {Divider} from "@mui/joy";

function DisplayLog(props) {
    const [listLog,setListLog] = useState([])
    const [currentUser,setCurrentUser] = useState(null);
    const [listLogToDisplay,setListLogToDisplay]=useState([])
    const [search,setSearch] = useState("")
    const [errorMessage,setErrorMessage] = useState("")
    let isLoadingData = false;

    useEffect(() => {
        try {
            if(!isLoadingData){
                isLoadingData = true
                setCurrentUser(props.currentUser)
                API_TicketLog.getAllTicketLog().then(result => {
                    setListLog(result);
                    setListLogToDisplay(result);
                    isLoadingData=false
                }).catch(
                    error => {
                        setListLog([])
                        setListLogToDisplay([])
                        setErrorMessage(error.status+" "+error.detail)
                        isLoadingData=false
                    })
            }
        }
        catch (e) {
            console.error(e)
        }
    }, [props.roleUser,props.currentUser]);

    function onChangeSearchBar(search=""){
        setSearch(search);
        if(search!=="")
            setListLogToDisplay(listLog.filter(ticket => (ticket.customerUsername && ticket.customerUsername.includes(search)) || (ticket.workerUsername && ticket.workerUsername.includes(search)) || (ticket.ticketId && ticket.ticketId.split("-")[2].includes(search))))
        else
            setListLogToDisplay(listLog);
    }



    function onResetButtonClicked(){
        setSearch("");
        setListLogToDisplay(listLog);
    }

    return (
        <>
            <div style={{width:"100%",height:"100%",overflowY:"scroll", marginTop:"2rem"}}>
                <div style={{display:"flex",alignItems:"center",flexDirection:"row",height:"10vh"}}>
                    <div style={{display:"flex",alignItems:"center",flexDirectiosn:"row",marginRight:"5%", width:"20%"}}>
                        <Form.Group className="mb-3 col-5" controlId="formBasicEmail" style={{width:"100%"}}>
                            <Form.Control type="text" placeholder="Search Ticket" value={search} onChange={ev=>onChangeSearchBar(ev.target.value)}/>
                            <Form.Text className="text-muted">
                            </Form.Text>
                        </Form.Group>
                        <SearchIcon style={{position:"relative", right:"30px",bottom:"8px"}} onClick={()=>onChangeSearchBar(search)}/>
                    </div>

                    <Button variant="primary" type="submit" style={{backgroundColor:"blue",color:"white",width:"10%",marginBottom:"15px"}} onClick={()=> onResetButtonClicked()}>Reset</Button>
                </div>
            </div>
                <Divider style={{height:"3px" ,width:"75%",backgroundColor:"black"}}/>
                <div style={{display:"flex",flexDirection:"column",alignItems:"flex-start",justifyContent:"flex-start",height:"80vh",overflowY:"scroll" }}>
                    {errorMessage!=="" ? (<> <span style={{color: "red", fontSize: "18px", fontWeight: "bold"}}>{errorMessage}</span> </>) : ""}
                    {errorMessage==="" ?  listLogToDisplay.map(logEntry =>
                        <div style={{width:"75%",height:"auto", paddingTop :"1rem",border:"1px solid black",borderRadius:"10px",display:"flex",flexDirection:"row", alignItems:"start",marginTop:"1rem"}}>
                            <div style={{width:"100%",height:"auto",display:"flex",flexDirection:"column", alignItems:"start",marginLeft:"15px",marginRight:"1rem"}}>
                                <div style={{width:"100%",height:"auto",display:"flex",flexDirection:"row",marginBottom:"2%"}}>
                                    <span style ={{width:"5rem",height:"auto",fontWeight:"bold",marginLeft:"2%",marginRight:"2%"}}>TicketID:</span>
                                    <span>{"Ticket-"+logEntry.ticketId.split("-")[2]}</span>
                                </div>
                                <div style={{width:"100%",height:"auto",display:"flex",flexDirection:"row",marginBottom:"2%"}}>
                                    <span style ={{width:"5rem",height:"auto",fontWeight:"bold",marginLeft:"2%",marginRight:"2%"}}>Status:</span>
                                    <span>{logEntry.status}</span>
                                </div>
                                <div style={{width:"100%",height:"auto",display:"flex",flexDirection:"row",marginBottom:"2%"}}>
                                    <span style ={{width:"5rem",height:"auto",fontWeight:"bold",marginLeft:"2%",marginRight:"2%"}}>LogId:</span>
                                    <span>{logEntry.logId}</span>
                                </div>
                                <div style={{width:"100%",height:"auto",display:"flex",flexDirection:"row",marginBottom:"2%"}}>
                                    <span style ={{width:"5rem",height:"auto",fontWeight:"bold",marginLeft:"2%",marginRight:"2%"}}>TimeStamp:</span>
                                    <span>{new Date(logEntry.timeStamp).toLocaleString('default', {year: 'numeric',month:"long",day:"numeric",hour:"numeric",minute:"numeric"})}</span>
                                </div>

                </div>
            </div>
                    ):""}
                </div>
        </>)



}
export default DisplayLog;