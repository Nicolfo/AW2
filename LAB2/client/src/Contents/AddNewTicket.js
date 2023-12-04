import Form from "react-bootstrap/Form";
import Slider from '@mui/joy/Slider';
import Box from '@mui/joy/Box';
import Button from '@mui/joy/Button';
import FormControl from '@mui/joy/FormControl';
import { useTheme } from '@mui/material/styles';
import Textarea from '@mui/joy/Textarea';
import IconButton from '@mui/joy/IconButton';
import Menu from '@mui/joy/Menu';
import MenuItem from '@mui/joy/MenuItem';
import ListItemDecorator from '@mui/joy/ListItemDecorator';
import FormatBold from '@mui/icons-material/FormatBold';
import FormatItalic from '@mui/icons-material/FormatItalic';
import KeyboardArrowDown from '@mui/icons-material/KeyboardArrowDown';
import Check from '@mui/icons-material/Check';
import {useState,useEffect} from "react";
import API_Ticket from "../API/API_TICKET/API_Ticket";


function AddNewTicket(props) {
    const [priority, setPriority] = useState(-1)
    const [description, setDescription] = useState("")
    const [isSubmitted, setIsSubmitted] = useState(false);
    const [username, setUsername] = useState("");
    const [role, setRole] = useState("");
    const [resultMessage, setResultMessage] = useState("");
    const [listExpert, setListExpert] = useState([])
    const [errorMessage, setErrorMessage] = useState("")
    const [succefulMessage, setSuccefulMessage] = useState("")
    const [ticket, setTicket] = useState(null);
    const [listOpenTicket, setListOpenTicket] = useState([]);
    const [italic, setItalic] = useState(false);
    const [fontWeight, setFontWeight] = useState('normal');
    const [anchorEl, setAnchorEl] = useState(null);
    const [expert,setExpert] = useState(null);
    let isLoadingData = false;


    useEffect(() => {
        try {
            setSuccefulMessage("")
            setErrorMessage("")
            setRole(props.roleUser)
            setTicket(null);
            if(props.roleUser==="Manager" && !isLoadingData){
                    isLoadingData =true
                    API_Ticket.getListTicketByStatus("open").then(result => {
                        setListOpenTicket(result)
                    }).catch(
                        error =>
                        {
                            setListOpenTicket([]);
                            setErrorMessage(error.status +" "+ error.detail)
                        })
                    API_Ticket.getListUserByRole("Expert").
                        then(result => {
                            setListExpert(result)
                            isLoadingData = false
                    }).
                        catch(error =>{
                        isLoadingData = false
                            setExpert(null);
                            setErrorMessage(error.status +" "+ error.detail)
                        })
                }
        }
        catch (e) {
            console.error(e)
        }
    }, [props.roleUser]);
    const getListExpert = async () => {
        let response = await API_Ticket.getListExpert()
        return response;
    }

    function getStyles(ticket, listTicket) {
        return {
            fontWeight:
                listTicket.indexOf(ticket) === -1
                    ? "bold"
                    : "italic"
        };
    }

    const MenuProps = {
        PaperProps: {
            style: {
                maxHeight: 48 * 4.5 + 8,
                width: 250
            }
        }
    }

    async function createTicket() {
        try {
            setIsSubmitted(true)
            let response = await API_Ticket.addTicket(description)
            setTicket([response])

        } catch (e) {
            console.error(e);
            setTicket([])
            setErrorMessage(`Error Sending Creation Ticket : ${e.status} - ${e.detail}`)
        }
    }

    if(succefulMessage){
        return <span style={{color: "green", fontSize: "18px", fontWeight: "bold"}}>{succefulMessage}</span>
    }
    if(errorMessage){
        return <span style={{color: "red", fontSize: "18px", fontWeight: "bold"}}>{errorMessage}</span>
    }
    if (role === "Client") {
        if (!isSubmitted) {
            return (
                <>
                    <Form onSubmit={(event) => {
                        event.preventDefault();
                        setIsSubmitted(true)
                    }}>
                        <Form.Group className="mb-3 col-5" controlId="formBasicEmail">
                            <FormControl>
                                <Textarea
                                    placeholder="Type ticket description here…"
                                    minRows={3}
                                    onChange={ev => setDescription(ev.target.value)}
                                    endDecorator={
                                        <Box
                                            sx={{
                                                display: 'flex',
                                                gap: 'var(--Textarea-paddingBlock)',
                                                pt: 'var(--Textarea-paddingBlock)',
                                                borderTop: '1px solid',
                                                borderColor: 'divider',
                                                flex: 'auto',
                                            }}
                                        >
                                            <IconButton
                                                variant="plain"
                                                color="neutral"
                                                onClick={(event) => setAnchorEl(event.currentTarget)}
                                            >
                                                <FormatBold/>
                                                <KeyboardArrowDown fontSize="md"/>
                                            </IconButton>
                                            <Menu
                                                anchorEl={anchorEl}
                                                open={Boolean(anchorEl)}
                                                onClose={() => setAnchorEl(null)}
                                                size="sm"
                                                placement="bottom-start"
                                                sx={{'--ListItemDecorator-size': '24px'}}
                                            >
                                                {['200', 'normal', 'bold'].map((weight) => (
                                                    <MenuItem
                                                        key={weight}
                                                        selected={fontWeight === weight}
                                                        onClick={() => {
                                                            setFontWeight(weight);
                                                            setAnchorEl(null);
                                                        }}
                                                        sx={{fontWeight: weight}}
                                                    >
                                                        <ListItemDecorator>
                                                            {fontWeight === weight && <Check fontSize="sm"/>}
                                                        </ListItemDecorator>
                                                        {weight === '200' ? 'lighter' : weight}
                                                    </MenuItem>
                                                ))}
                                            </Menu>
                                            <IconButton
                                                variant={italic ? 'soft' : 'plain'}
                                                color={italic ? 'primary' : 'neutral'}
                                                aria-pressed={italic}
                                                onClick={() => setItalic((bool) => !bool)}
                                            >
                                                <FormatItalic/>
                                            </IconButton>
                                            <Button sx={{ml: 'auto'}} onClick={() => createTicket()}>Send</Button>
                                        </Box>
                                    }
                                    sx={{
                                        minWidth: 300,
                                        fontWeight,
                                        fontStyle: italic ? 'italic' : 'initial',
                                    }}
                                />
                            </FormControl>
                        </Form.Group>
                    </Form>
                </>)

        }
    }
    if (role === "Manager") {
        return (<>
            <div style={{width:"80%"}}>
            <Form.Label> Choose Ticket</Form.Label>
            <Form.Control as="select" onChange={ev => setTicket(ev.target.value)}>
                <option key={"-"}  value={null}>-</option>
                {listOpenTicket.map((ticket,index) =>
                    <option key={ticket.ticketId} value={ticket.ticketId}> {"TICKET-"+ticket.ticketId.split("-")[2]+" : "+ticket.description}</option>
                )}
            </Form.Control>

            {ticket ?
                (<>
                    <Form.Label style={{marginTop: "1rem", marginBottom: "1.5rem"}}>Priority</Form.Label>
                    <Slider
                        min={0}
                        max={5}
                        marks
                        size="lg"
                        onChange={ev => setPriority(ev.target.value)}
                        valueLabelDisplay="on"
                        variant="solid"
                    />
                </>) : "" }
            {priority!==-1 ?
                (<>
                    <Form.Label> Choose Expert</Form.Label>
                    <Form.Control as="select" onChange={ev => {console.log(ev.target.value);setExpert(ev.target.value)}}>
                        <option key={"-"}  value={null}>-</option>
                        {listExpert.map((expert,index) =>
                            <option key={expert.username} value={expert.username}> {expert.username + "("+expert.email+")"}</option>
                        )}
                    </Form.Control>
                </>)
                :
                ""}
            {expert ?
                (<>
                    <Button variant="primary" type="submit" style={{backgroundColor:"blue",color:"white",marginTop:"40px"}}
                     onClick={()=>{API_Ticket.updateTicketPriorityAndWorker(listOpenTicket.find(ele=> ele.ticketId === ticket),priority,expert).then(()=>setSuccefulMessage("The Ticket Priority and Expert are successful Added")).catch((error)=>setErrorMessage(error.status+"   "+error.detail))}}>Submit</Button>
                </>)
                : ""
            }
            </div>
            </>)

        } else {
            if (ticket) {
                return <span style={{color:"green"}}>The ticket is {ticket[0].ticketId}</span>
            }
        }
}
export default AddNewTicket;
