import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {useState} from "react";

function UpdateProfileForm(props){
    const [email,setEmail]=useState("");
    const [oldEmail,setOldEmail]=useState("");
    const [isSubmitted,setIsSubmitted]=useState(false);
    const [name,setName]=useState("");
    const [resultMessage,setResultMessage]=useState("");
    const handleSubmit=(event)=>{
        event.preventDefault();
        setIsSubmitted(true);
        props.updateProfile(oldEmail,{email,name}).then(()=>{
            setResultMessage("Profile updated correctly");

        })
            .catch((err)=>{
                setResultMessage("Error "+err.status+" "+err.detail+" on API call "+err.instance)
            })
    }
    if(!isSubmitted)
    return (<Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3" controlId="formBasicEmail">
            <Form.Label>Old Email</Form.Label>
            <Form.Control type="text" placeholder="Enter old email " onChange={ev=>setOldEmail(ev.target.value)}/>
            <Form.Text className="text-muted">
            </Form.Text>

            <Form.Label>New Email</Form.Label>
            <Form.Control type="text" placeholder="Enter new email " onChange={ev=>setEmail(ev.target.value)}/>
            <Form.Text className="text-muted">
            </Form.Text>

            <Form.Label>New Name</Form.Label>
            <Form.Control type="text" placeholder="Enter new name " onChange={ev=>setName(ev.target.value)}/>
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
            <br/>
            <Button variant="primary" type="submit" onClick={()=>{
                setResultMessage("");
                setName("");
                setIsSubmitted(false);
                setEmail("");
            }
            }>
                Update another
            </Button>
    </div>

}
export default UpdateProfileForm;