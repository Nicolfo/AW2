import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {useState} from "react";

function UpdateProfileForm(props){

    const [oldUsername,setOldUsername]=useState("");
    const [isSubmitted,setIsSubmitted]=useState(false);
    const [email,setEmail]=useState("");
    const [username,setUsername]=useState("");
    const [role,setRole]=useState("");
    const [resultMessage,setResultMessage]=useState("");
    const handleSubmit=(event)=>{
        event.preventDefault();
        setIsSubmitted(true);
        props.updateProfile(oldUsername,{username,email,role: role}).then(()=>{
            setResultMessage("Profile updated correctly");

        })
            .catch((err)=>{
                setResultMessage("Error "+err.status+" "+err.detail+" on API call "+err.instance)
            })
    }
    if(!isSubmitted)
    return (<Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3 col-5" controlId="formBasicEmail">
            <Form.Label>Old Username</Form.Label>
            <Form.Control type="text" placeholder="Enter username to update " onChange={ev=>setOldUsername(ev.target.value)}/>
            <Form.Text className="text-muted">
            </Form.Text>

            <Form.Label>New Username</Form.Label>
            <Form.Control type="text" placeholder="Enter new username " onChange={ev=>setUsername(ev.target.value)}/>
            <Form.Text className="text-muted">
            </Form.Text>

            <Form.Label>New Email</Form.Label>
            <Form.Control type="text" placeholder="Enter new email " onChange={ev=>setEmail(ev.target.value)}/>
            <Form.Text className="text-muted">
            </Form.Text>

            <Form.Label>New Role</Form.Label>
            <Form.Control  as="select" onChange={ev=>setRole(ev.target.value)}>
                <option value="Client">Client</option>
                <option value="Expert">Expert</option>
                <option value="Manager">Manager</option>
            </Form.Control>
            <Form.Text className="text-muted">
            </Form.Text>
        </Form.Group>

        <Button variant="primary" type="submit">
            Submit
        </Button>
    </Form>);
    else if(resultMessage!=="")
        return <div>
            {resultMessage}
            <br/>
            <Button variant="primary" type="submit" onClick={()=>{
                setResultMessage("");
                setRole("");
                setIsSubmitted(false);
                setEmail("");
            }
            }>
                Update another
            </Button>
    </div>
    else
        return <div>Sending Request ...</div>

}
export default UpdateProfileForm;