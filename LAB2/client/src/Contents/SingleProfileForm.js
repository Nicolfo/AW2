import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {useState} from "react";
import Table from "react-bootstrap/Table";

function SingleProfileForm(props){
    const [emailToSearch,setEmailToSearch]=useState("");
    const [isSubmitted,setIsSubmitted]=useState(false);
    const [name,setName]=useState("");
    const [errMsg,setErrMsg]=useState("");
    const handleSubmit=(event)=>{
        event.preventDefault();
        setIsSubmitted(true);
        props.getProfile(emailToSearch).then((profile)=>{
            setErrMsg("");
            setName(profile.name);

        })
            .catch((err)=>{
                setErrMsg("Error "+err.status+" "+err.detail+" on API call "+err.instance)

            })
    }
    if(!isSubmitted && errMsg==="")
        return  <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label>Email</Form.Label>
                <Form.Control type="text" placeholder="Enter email " onChange={ev=>setEmailToSearch(ev.target.value)}/>
                <Form.Text className="text-muted">
                </Form.Text>
            </Form.Group>

            <Button variant="primary" type="submit">
                Submit
            </Button>
        </Form>

    if(errMsg!==""){
        return (<div>{errMsg}</div>)
    }
    if(isSubmitted){

        return <>
            <div>
                <Table striped bordered hover>
                <thead>
                <tr>
                    <th>Email</th>
                    <th>Name</th>
                </tr>
                </thead>
                <tbody>
                <tr key={emailToSearch}>
                    <td>{emailToSearch}</td>
                    <td>{name}</td>
                </tr>
                </tbody>
            </Table>

                <Button variant="primary" type="submit" onClick={()=>{
                    setErrMsg("");
                    setName("");
                    setIsSubmitted(false);
                    setEmailToSearch("");
                }
                }>
                    Search another
                </Button>
            </div>

        </>
    }

}

export default SingleProfileForm;