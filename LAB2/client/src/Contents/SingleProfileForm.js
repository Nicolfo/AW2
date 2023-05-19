import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {useState} from "react";
import Table from "react-bootstrap/Table";

function SingleProfileForm(props){
    const [usernameToSearch,setUsernameToSearch]=useState("");
    const [isSubmitted,setIsSubmitted]=useState(false);
    const [email,setEmail]=useState("");
    const [role,setRole]=useState("");
    const [errMsg,setErrMsg]=useState("");
    const handleSubmit=(event)=>{
        event.preventDefault();
        setIsSubmitted(true);
        props.getProfile(usernameToSearch).then((profile)=>{
            setErrMsg("");
            setEmail(profile.email);
            setRole(profile.role);
        })
            .catch((err)=>{
                setErrMsg("Error "+err.status+" "+err.detail+" on API call "+err.instance)

            })
    }
    if(!isSubmitted && errMsg==="")
        return  <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3 col-5">
                <Form.Label>Username</Form.Label>
                <Form.Control type="text" placeholder="Enter username to search " onChange={ev=>setUsernameToSearch(ev.target.value)}/>
                <Form.Text className="text-muted">
                </Form.Text>
            </Form.Group>

            <Button variant="primary" type="submit">
                Search
            </Button>
        </Form>

    if(errMsg!==""){
        return (
            <div>
                {errMsg}
                <br/>
                <Button variant="primary" type="submit" onClick={()=>{
                    setErrMsg("");
                    setEmail("");
                    setRole("");
                    setIsSubmitted(false);
                    setUsernameToSearch("");
                }
                }>
                    Search another
                </Button>
            </div>)
    }
    if(isSubmitted && email===""){
        return <div>Waiting Server Response ...</div>
    }

    if(isSubmitted ){

        return <>
            <div>
                <Table striped bordered hover>
                <thead>
                <tr>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Role</th>
                </tr>
                </thead>
                <tbody>
                <tr key={usernameToSearch}>
                    <td>{usernameToSearch}</td>
                    <td>{email}</td>
                    <td>{role}</td>
                </tr>
                </tbody>
            </Table>

                <Button variant="primary" type="submit" onClick={()=>{
                    setErrMsg("");
                    setEmail("");
                    setIsSubmitted(false);
                    setUsernameToSearch("");
                }
                }>
                    Search another
                </Button>
            </div>

        </>
    }



}

export default SingleProfileForm;