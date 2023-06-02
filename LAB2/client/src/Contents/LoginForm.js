
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {useState} from "react";
import {Link} from "react-router-dom";

function LoginForm(props){

    const {errorMsg,setErrorMsg} = props;
    const [username,setUsername]=useState("");
    const [password,setPassword]=useState("");
    const handleSubmit=(event)=>{
        event.preventDefault();

        // eventualmente aggiungere qui validation dell'input

        props.login(username,password)
            .catch((err) => {
                setErrorMsg(err.detail?err.detail:JSON.stringify(err));
            })
    }

    if(props.isLoggedIn){
        return <div>You are already logged in!</div>
    }
    return  <>
        <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3 col-5" >
            <Form.Label>Username</Form.Label>
            <Form.Control type="text" placeholder="Username" onChange={ev=>setUsername(ev.target.value)}/>
            <Form.Label>Password</Form.Label>
            <Form.Control type="password" placeholder="Password" onChange={ev=>setPassword(ev.target.value)}/>
            <Form.Text className="text-muted">
            </Form.Text>
        </Form.Group>
            {errorMsg? <div className="col-5" style={{color: "red"}}>Login was unsuccessful: {errorMsg}</div>:<></>}
        <Button variant="primary" type="submit">
            Login
        </Button>

        <p>Not registered yet?
            <Link to="/signup"> <b>SignUp</b>  </Link>
        </p>


    </Form>
        </>


}

export default LoginForm;