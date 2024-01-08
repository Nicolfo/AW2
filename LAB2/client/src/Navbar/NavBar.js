import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import {Dropdown} from "react-bootstrap";
import { PersonCircle } from 'react-bootstrap-icons';
import {Link, useNavigate} from "react-router-dom";
import Button from "react-bootstrap/Button";
import {useState} from "react";

function NavBar(props) {

    const {user, loggedIn, logout,login} = props;
    const [username,setUsername]=useState("");
    const [password,setPassword]=useState("");
    const navigate=useNavigate();
    const handleLogoutClick = (event) => {
        event.preventDefault();
        setUsername("");
        setPassword("");
        logout();
    }

    const handleLoginClick = (event) => {
        event.preventDefault();
        login(username,password);
    }

    return (
        <Navbar bg="light" expand="lg">
            <Container fluid className="ms-2 me-2">
                <Navbar.Brand >
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                         stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"
                         className="feather feather-shopping-bag">
                        <path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"></path>
                        <line x1="3" y1="6" x2="21" y2="6"></line>
                        <path d="M16 10a4 4 0 0 1-8 0"></path>
                    </svg>
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link >Web Application 2- 2023</Nav.Link>
                        <Nav.Link >Final Project</Nav.Link>
                        <Nav.Link >Group 33</Nav.Link>
                        <NavDropdown title="Members" id="basic-nav-dropdown">
                            <NavDropdown.Item >Matteo Fontana S292567</NavDropdown.Item>
                            <NavDropdown.Item >Nicolò Fontana S303361</NavDropdown.Item>
                            <NavDropdown.Item >Jacopo Spaccatrosi S285891</NavDropdown.Item>
                        </NavDropdown>
                    </Nav>
                </Navbar.Collapse>

                <Nav >
                    {loggedIn?

                        <div className="form-inline" style={{color: "black", marginRight: "15px"}}>
                            <span style={{paddingRight:"15px"}}>Welcome <b>{props.user.username}</b></span>
                            <svg alt="" xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor"
                                 className="bi bi-person-circle" viewBox="0 0 16 16">
                                <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"/>
                                <path fillRule="evenodd"
                                      d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"/>
                            </svg>
                            <Button className="btn btn-warning" style={{marginLeft:"10px"}} onClick={handleLogoutClick}>Logout</Button>
                        </div>
                        :


                        <form className="d-flex" style={{marginRight: "15px"}} >
                            <input className="form-control me-2" type="text" placeholder="Username" value={username} onChange={ev => setUsername(ev.target.value)}/>
                            <input className="form-control me-2" type="password" placeholder="Password" value={password} onChange={ev => setPassword(ev.target.value)}/>
                            <Button type="submit" className="btn btn-warning me-2" onClick={handleLoginClick}>SignIn</Button>
                            <Button className="mr-2 btn btn-warning" onClick={()=>{navigate("/signup")}}>SignUp</Button>
                        </form>

                    }
                </Nav>

            </Container>
        </Navbar>
    );
}

export default NavBar;