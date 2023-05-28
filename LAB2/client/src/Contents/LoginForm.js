
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {useState} from "react";
import {Link} from "react-router-dom";

function LoginForm(props){
    const [username,setUsername]=useState("");
    const [password,setPassword]=useState("");
    const [response,setResponse]=useState("");
    const [errorMsg,setErrorMsg]=useState("");
    const handleSubmit=(event)=>{
        event.preventDefault();
        props.login(username,password).then((response)=>{
            //facci qualcosa poi, ora te lo mostro
            if(response.error==null)
            setResponse(response.access_token);
            else
                setErrorMsg((JSON.stringify(response)));
        })
            .catch((err)=>{
                setErrorMsg(err.detail?err.detail:JSON.stringify(err));

            })
    }
    if(errorMsg!==""){
        return <>
            Login was unsuccessful: {errorMsg}<br/>
            <Button variant="primary" type="submit" onClick={()=>{setErrorMsg("");}}>
                Try Again
            </Button>
            </>
    }
    if(response==="")
    return  <>
        <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3 col-5" controlId="formBasicEmail">
            <Form.Label>Username</Form.Label>
            <Form.Control type="text" placeholder="Username" onChange={ev=>setUsername(ev.target.value)}/>
            <Form.Label>Password</Form.Label>
            <Form.Control type="password" placeholder="Password" onChange={ev=>setPassword(ev.target.value)}/>
            <Form.Text className="text-muted">
            </Form.Text>
        </Form.Group>

        <Button variant="primary" type="submit">
            Login
        </Button>

        <p>Not registered yet?
            <Link to="/signup"> <b>SignUp</b>  </Link>
        </p>


    </Form>
        </>
    else
        return <><div className="col-12 text-break"> Your token is {response}</div>
            <Button variant="primary" type="submit" onClick={()=>{setResponse("");}}>
                Log Out
            </Button>
       </>

}

export default LoginForm;