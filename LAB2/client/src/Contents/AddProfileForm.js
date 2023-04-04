import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {useState} from "react";

function AddProfileForm(props){
    const [email,setEmail]=useState("");
    const [isSubmitted,setIsSubmitted]=useState(false);
    const [name,setName]=useState("");
    const [resultMessage,setResultMessage]=useState("");
    const handleSubmit=(event)=>{
        event.preventDefault();
        setIsSubmitted(true);
        props.addProfile({email,name}).then(()=>{
            setResultMessage("Profile added correctly");

        })
            .catch((err)=>{
                setResultMessage("Error "+err.status+" "+err.detail+" on API call "+err.instance)
            })
    }
    if(!isSubmitted)
    return (<Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3" controlId="formBasicEmail">
            <Form.Label>Email</Form.Label>
            <Form.Control type="text" placeholder="Enter email " onChange={ev=>setEmail(ev.target.value)}/>
            <Form.Text className="text-muted">
            </Form.Text>

            <Form.Label>Name</Form.Label>
            <Form.Control type="text" placeholder="Enter name " onChange={ev=>setName(ev.target.value)}/>
            <Form.Text className="text-muted">
            </Form.Text>
        </Form.Group>

        <Button variant="primary" type="submit">
            Submit
        </Button>
    </Form>);
    else
        return <div>
            {resultMessage}
            <br></br>
            <Button variant="primary" type="submit" onClick={()=>{
                setResultMessage("");
                setName("");
                setIsSubmitted(false);
                setEmail("");
            }
            }>
                Add another
            </Button>
    </div>

}
export default AddProfileForm;