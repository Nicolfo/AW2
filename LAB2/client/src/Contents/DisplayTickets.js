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
    const [search,setSearch] = useState("")
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

            function onChangeSearchBar(search=""){
                if(search!=="")
                    setListTicketToDisplay(listTicket.filter(ticket => (ticket.customerUsername && ticket.customerUsername.includes(search)) || (ticket.workerUsername && ticket.workerUsername.includes(search)) || (ticket.ticketId && ticket.ticketId.split("-")[2].includes(search))))
            }

            function onChangeStatusTicket(status=""){
                if(status!==""){
                    setListTicketToDisplay(listTicket.filter(ticket => ticket.status && ticket.status.toLowerCase() ===  status.toLowerCase() ))
                }
            }

            function onChangePriorityTicket(priority=-1){
                if(priority!==0){
                    setListTicketToDisplay(listTicket.filter(ticket => ticket.priority && ticket.priority >=  priority ))
                }
                else{
                    setListTicketToDisplay(listTicket)
                }
            }

            function onResetButtonClicked(){
                setListTicketToDisplay(listTicket);
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
                                        <Form.Control type="text" placeholder="Search Ticket"
                                                      onChange={ev => onChangeSearchBar(ev.target.value)}/>
                                        <Form.Text className="text-muted">
                                        </Form.Text>
                                    </Form.Group>
                                    <SearchIcon style={{position: "relative", right: "30px", bottom: "8px"}}
                                                onClick={() => onChangeSearchBar(search)}/>
                                </div>
                                <Form.Control as="select" onChange={ev => {
                                    onChangeStatusTicket(ev.target.value)
                                }} style={{width: "15%", height: "60%", marginRight: "5%", marginBottom: "1%"}}>
                                    <option key={"-"} value={null}>-</option>
                                    {["OPEN", "IN PROGRESS", "RESOLVED", "REOPENED", "CLOSED"].map((status, index) =>
                                        <option key={status} value={status}> {status}</option>
                                    )}
                                </Form.Control>
                                <Slider
                                    min={0}
                                    max={5}
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
                                                    width: "25%",
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
                                                    width: "25%",
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
                                                    width: "25%",
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
                                                    width: "25%",
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
                                                            width: "25%",
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
                                            <Button variant="primary" type="submit"
                                                    style={{backgroundColor: "blue", color: "white", width: "30%"}}
                                                    onClick={() => {
                                                        setTicketIDChat(ticket.ticketId);
                                                        setChatDisplayed(true)
                                                    }}>CHAT</Button>
                                        </div>
                                    </div>
                                ) : ""}
                            </div>
                        </div>
                    </>)
            }



}
export default DisplayTickets;
