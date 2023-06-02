import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {useState} from "react";
import {Link} from "react-router-dom";

function SignupForm(props){

    //const { signup, signedUp } = props;

    const [username,setUsername]=useState("");
    const [email,setEmail]=useState("");
    const [password,setPassword]=useState("");
    const [errorMsg,setErrorMsg]=useState("");
    const insertAnother=()=>{
        props.setSignedUp(false);
    }
    const handleSubmit=(event)=>{
        event.preventDefault();

        // eventualmente aggiungere qui validation dell'input

        let funToCall = props.signup? props.signup : props.createExpert
        funToCall(username,email,password)
            .catch((err) => {
                setErrorMsg(err.detail?err.detail:JSON.stringify(err));
            })
    }

    if(errorMsg!==""){
        return <>
            Signup was unsuccessful: {errorMsg}<br/>
            <Button variant="primary" type="submit" onClick={()=>{setErrorMsg("");}}>
                Try Again
            </Button>
        </>
    }

    if(props.signedUp===false )
        return  <>
            <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3 col-5" controlId="formBasicEmail">
                    <Form.Label>Username</Form.Label>
                    <Form.Control type="text" placeholder="Username" onChange={ev=>setUsername(ev.target.value)}/>
                    <Form.Label>Email</Form.Label>
                    <Form.Control type='email' placeholder="Email" onChange={ev=>setEmail(ev.target.value)}/>
                    <Form.Label>Password</Form.Label>
                    <Form.Control type="password" placeholder="Password" onChange={ev=>setPassword(ev.target.value)}/>
                    <Form.Text className="text-muted">
                    </Form.Text>
                </Form.Group>

                { props.signup &&
                    <p style={{ fontSize: 10}}>
                        <Link to="/login"> back to login </Link>
                    </p>
                }

                <Button variant="primary" type="submit">
                    {props.signup ? "Signup" : "Create" }
                </Button>
            </Form>
        </>
    else
        if(props.createExp)
            return <><div className="col-12 text-break"> New user correctly registered!</div>
                <Button variant="primary" type="submit" onClick={()=>{insertAnother();}}>
                    Insert another
                </Button>
            </>
        return <><div className="col-12 text-break"> New user correctly registered!</div>
            <p> -> Go back to
                <Link to="/"> <b>HomePage</b>  </Link>
            </p>
        </>

}

export default SignupForm;