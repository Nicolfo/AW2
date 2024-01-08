import Form from "react-bootstrap/Form";
import SearchIcon from '@mui/icons-material/Search';
import {useState,useEffect} from "react";
import API_Ticket from "../API/API_TICKET/API_Ticket";
import Slider from "@mui/joy/Slider";
import Button from '@mui/joy/Button';
import {Divider} from "@mui/joy";
import Chat from "./Chat";

function DisplayTickets(props) {
    const [listTicket,setListTicket] = useState([])
    const [listTicketToDisplay,setListTicketToDisplay] = useState([])
    const [currentUser,setCurrentUser] = useState(null);
    const [ticketIDChat,setTicketIDChat] = useState(null);
    const [chatDisplayed,setChatDisplayed] = useState(false);
    const [ticketIdToSearch,setTicketIdToSearch] = useState("")
    const [statusToSearch,setStatusToSearch] = useState("")
    const [priorityToSearch,setPriorityToSearch] = useState(0)
    const [errorMessage,setErrorMessage] = useState("")
    let isLoadingData = false;

    useEffect(() => {
        try {
                if(!isLoadingData){
                    isLoadingData = true
                    setTicketIDChat(false) ;
                    setCurrentUser(props.currentUser)
                    API_Ticket.getTicketByUsername().then(result => {
                        setListTicket(result);
                        setListTicketToDisplay(result)
                        isLoadingData=false
                    }).catch(
                        error => {
                            setListTicketToDisplay([]);
                            setErrorMessage(error.status+" "+error.detail)
                            isLoadingData=false
                        })
                }
        }
        catch (e) {
            console.error(e)
        }
    }, [props.roleUser,props.currentUser]);

    useEffect(()=>{
        if(isLoadingData)
            return;
        let newList=[];
        if(ticketIdToSearch!=="")
            newList=listTicket.filter(ticket => (ticket.customerUsername && ticket.customerUsername.includes(ticketIdToSearch)) || (ticket.workerUsername && ticket.workerUsername.includes(ticketIdToSearch)) || (ticket.ticketId && ticket.ticketId.split("-")[2].includes(ticketIdToSearch)))
        else
            newList=listTicket;

        if(statusToSearch!=="-"){
            newList=newList.filter(ticket => ticket.status && ticket.status.toLowerCase() ===  statusToSearch.toLowerCase() )
        }

        if(priorityToSearch!==0){
            newList=newList.filter(ticket => ticket.priority && ticket.priority >=  priorityToSearch );
        }
        setListTicketToDisplay(newList);

    },[ticketIdToSearch,statusToSearch,priorityToSearch]);
            function onChangeSearchBar(search=""){
                setTicketIdToSearch(search)
            }

            function onChangeStatusTicket(status=""){
                setStatusToSearch(status);
            }

            function onChangePriorityTicket(priority=-1){
                setPriorityToSearch(priority);
            }

            function onResetButtonClicked(){
                setStatusToSearch("-");
                setPriorityToSearch(0);
                setTicketIdToSearch("");
            }

            if(chatDisplayed){
                return (<Chat user={currentUser} ticketID={ticketIDChat}/>)
            }
            else {

                return ( <>
                        <div style={{width: "100%", height: "100%", overflowY: "scroll", marginTop: "2rem"}}>
                            <div style={{display: "flex", alignItems: "center", flexDirection: "row", height: "12vh"}}>
                                <div style={{
                                    display: "flex",
                                    alignItems: "center",
                                    flexDirection: "row",
                                    marginRight: "5%",
                                    width: "20%"
                                }}>
                                    <Form.Group className="mb-3 col-5" controlId="formBasicEmail"
                                                style={{width: "100%"}}>
                                        <Form.Control type="text" placeholder="Search Ticket" value={ticketIdToSearch}
                                                      onChange={ev => onChangeSearchBar(ev.target.value)}/>
                                        <Form.Text className="text-muted">
                                        </Form.Text>
                                    </Form.Group>
                                    <SearchIcon style={{position: "relative", right: "30px", bottom: "8px"}}
                                                onClick={() => onChangeSearchBar(ticketIdToSearch)}/>
                                </div>
                                <Form.Control as="select" value={statusToSearch} onChange={ev => {
                                    onChangeStatusTicket(ev.target.value)
                                }} style={{width: "15%", height: "40%", marginRight: "5%", marginBottom: "1%"}}>
                                    <option key={"-"} value={null}>-</option>
                                    {["OPEN", "IN PROGRESS", "RESOLVED", "REOPENED", "CLOSED"].map((status, index) =>
                                        <option key={status} value={status}> {status}</option>
                                    )}
                                </Form.Control>
                                <Slider
                                    min={0}
                                    max={5}
                                    value={priorityToSearch}
                                    marks
                                    style={{width: "15%", marginRight: "5%"}}
                                    size="lg"
                                    onChange={ev => onChangePriorityTicket(ev.target.value)}
                                    valueLabelDisplay="on"
                                    variant="solid"
                                />
                                <Button variant="primary" type="submit" style={{
                                    backgroundColor: "blue",
                                    color: "white",
                                    width: "10%",
                                    marginBottom: "15px",
                                    fontSize:"18px"
                                }} onClick={() => onResetButtonClicked()}>Reset</Button>
                            </div>
                            <Divider style={{height: "3px", width: "75%", backgroundColor: "black"}}/>
                            <div style={{
                                display: "flex",
                                flexDirection: "column",
                                alignItems: "flex-start",
                                justifyContent: "flex-start",
                                height: "80vh",
                                overflowY: "scroll"
                            }}>
                                {errorMessage !== "" ? (<> <span style={{
                                    color: "red",
                                    fontSize: "18px",
                                    fontWeight: "bold"
                                }}>{errorMessage}</span> </>) : ""}
                                {errorMessage === "" ? listTicketToDisplay.map(ticket =>
                                    <div style={{
                                        width: "75%",
                                        height: "auto",
                                        paddingTop: "1rem",
                                        border: "1px solid black",
                                        borderRadius: "10px",
                                        display: "flex",
                                        flexDirection: "row",
                                        alignItems: "start",
                                        marginTop: "1rem"
                                    }}>
                                        <div style={{
                                            width: "30%",
                                            height: "auto",
                                            display: "flex",
                                            flexDirection: "column",
                                            alignItems: "start",
                                            marginLeft: "15px",
                                            marginRight: "1rem"
                                        }}>
                                            <div style={{
                                                width: "100%",
                                                height: "auto",
                                                display: "flex",
                                                flexDirection: "row",
                                                marginBottom: "2%"
                                            }}>
                                                <span style={{
                                                    width: "5rem",
                                                    height: "auto",
                                                    fontWeight: "bold",
                                                    marginLeft: "2%",
                                                    marginRight: "2%"
                                                }}>Ticket:</span>
                                                <span>{"Ticket-" + ticket.ticketId.split("-")[2]}</span>
                                            </div>
                                            <div style={{
                                                width: "100%",
                                                height: "auto",
                                                display: "flex",
                                                flexDirection: "row",
                                                marginBottom: "2%"
                                            }}>
                                                <span style={{
                                                    width: "5rem",
                                                    height: "auto",
                                                    fontWeight: "bold",
                                                    marginLeft: "2%",
                                                    marginRight: "2%"
                                                }}>Priority:</span>
                                                <span>{ticket.priority}</span>
                                            </div>
                                            <div style={{
                                                width: "100%",
                                                height: "auto",
                                                display: "flex",
                                                flexDirection: "row",
                                                marginBottom: "2%"
                                            }}>
                                                <span style={{
                                                    width: "5rem",
                                                    height: "auto",
                                                    fontWeight: "bold",
                                                    marginLeft: "2%",
                                                    marginRight: "2%"
                                                }}>Status:</span>
                                                <span>{ticket.status}</span>
                                            </div>
                                            <div style={{
                                                width: "100%",
                                                height: "auto",
                                                display: "flex",
                                                flexDirection: "row",
                                                marginBottom: "2%"
                                            }}>
                                                <span style={{
                                                    width: "5rem",
                                                    height: "auto",
                                                    fontWeight: "bold",
                                                    marginLeft: "2%",
                                                    marginRight: "2%"
                                                }}>Customer:</span>
                                                <span>{ticket.customerUsername}</span>
                                            </div>
                                            {ticket.workerUsername ?
                                                (<>
                                                    <div style={{
                                                        width: "100%",
                                                        height: "auto",
                                                        display: "flex",
                                                        flexDirection: "row",
                                                        marginBottom: "2%"
                                                    }}>
                                                        <span style={{
                                                            width: "5rem",
                                                            height: "auto",
                                                            fontWeight: "bold",
                                                            marginLeft: "2%",
                                                            marginRight: "2%"
                                                        }}>Worker:</span>
                                                        <span>{ticket.workerUsername}</span>
                                                    </div>
                                                </>) : ""}
                                        </div>
                                        <div style={{
                                            width: "30%",
                                            height: "auto",
                                            display: "flex",
                                            flexDirection: "column",
                                            alignItems: "start",
                                            marginLeft: "15px",
                                            marginRight: "1rem"
                                        }}>
                                            <span style={{
                                                width: "25%",
                                                height: "auto",
                                                fontWeight: "bold",
                                                marginBottom: "1rem"
                                            }}>{"Description:"}</span>
                                            <span>{ticket.description}</span>
                                        </div>
                                        <div style={{
                                            width: "30%",
                                            height: "100%",
                                            display: "flex",
                                            alignItems: "center",
                                            justifyContent: "center"
                                        }}>
                                            {(ticket.status==="IN PROGRESS" || props.roleUser==="Manager" ||  props.roleUser==="Expert" )  && <Button variant="primary" type="submit"
                                                    style={{backgroundColor: "blue", color: "white", width: "30%"}}
                                                    onClick={() => {
                                                        setTicketIDChat(ticket.ticketId);
                                                        setChatDisplayed(true)
                                                    }}>CHAT</Button>}
                                        </div>
                                    </div>
                                ) : ""}
                            </div>
                        </div>
                    </>)
            }



}
export default DisplayTickets;
