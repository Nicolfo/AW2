import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {useState} from "react";

function AddProfileForm(props){
    const [email,setEmail]=useState("");
    const [isSubmitted,setIsSubmitted]=useState(false);
    const [username,setUsername]=useState("");
    const [password,setPassword]=useState("");
    const [role,setRole]=useState("Client");
    const [resultMessage,setResultMessage]=useState("");
    const handleSubmit=(event)=>{
        event.preventDefault();
        setIsSubmitted(true);
        props.addProfile(username,email,password,role).then(()=>{
            setResultMessage("Profile added correctly");

        })
            .catch((err)=>{
                setResultMessage("Error "+err.status+" "+err.detail+" on API call "+err.instance)
            })
    }
    if(!isSubmitted)
    return (<Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3 col-5" controlId="formBasicEmail">
            <Form.Label>Email</Form.Label>
            <Form.Control type="text" placeholder="Enter email " onChange={ev=>setEmail(ev.target.value)}/>
            <Form.Text className="text-muted">
            </Form.Text>

            <Form.Label>Username</Form.Label>
            <Form.Control type="text" placeholder="Enter username " onChange={ev=>setUsername(ev.target.value)}/>
            <Form.Text className="text-muted">
            </Form.Text>

            <Form.Label>Password</Form.Label>
            <Form.Control type="password" placeholder="Enter password " onChange={ev=>setPassword(ev.target.value)}/>
            <Form.Text className="text-muted">
            </Form.Text>


            <Form.Label>Role</Form.Label>
            <Form.Control  as="select" onChange={ev=>setRole(ev.target.value)}>
                <option value="Client">Client</option>
                <option value="Expert">Expert</option>
                <option value="Manager">Manager</option>
            </Form.Control><Form.Text className="text-muted">
            </Form.Text>
        </Form.Group>

        <Button variant="primary" type="submit">
            Submit
        </Button>
    </Form>);
    else if(resultMessage!=="")
        return <div>
            {resultMessage}
            <br></br>
            <Button variant="primary" type="submit" onClick={()=>{
                setResultMessage("");
                setUsername("");
                setIsSubmitted(false);
                setEmail("");
            }
            }>
                Add another
            </Button>
    </div>
    else return <div>Sending Request ...</div>

}
export default AddProfileForm;