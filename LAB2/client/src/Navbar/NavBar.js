import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import {Dropdown} from "react-bootstrap";
import { PersonCircle } from 'react-bootstrap-icons';
import {Link} from "react-router-dom";
import Button from "react-bootstrap/Button";

function NavBar(props) {

    const {user, loggedIn, logout} = props;

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
                        <Nav.Link >Lab 5</Nav.Link>
                        <Nav.Link >Group 33</Nav.Link>
                        <NavDropdown title="Members" id="basic-nav-dropdown">
                            <NavDropdown.Item >Matteo Fontana S292567</NavDropdown.Item>
                            <NavDropdown.Item >Nicolò Fontana S303361</NavDropdown.Item>
                            <NavDropdown.Item >Jacopo Spaccatrosi S285891</NavDropdown.Item>
                            <NavDropdown.Item >Giuseppe Galluzzo S261187</NavDropdown.Item>
                        </NavDropdown>
                    </Nav>
                </Navbar.Collapse>

                <Nav >
                    <Dropdown drop='down' >
                        <Navbar.Text className="text-warning font-weight-bold pr-4" style={{ fontSize: "1rem" }}>
                            {user && user.username && `Welcome, ${user?.username}!`}
                        </Navbar.Text>

                        <Dropdown.Toggle variant="success" id="dropdown-basic">
                            <PersonCircle size="30" />
                        </Dropdown.Toggle>

                        <Dropdown.Menu className="bg-warning">
                            {!loggedIn &&
                                <Dropdown.Item>
                                    <Link to="/login">
                                        <Button variant="outline-primary"> Login </Button>
                                    </Link>
                                </Dropdown.Item>}
                            {loggedIn &&
                                <Dropdown.Item>
                                    <Link to="/">
                                        <Button variant="outline-secondary" onClick={logout}> Logout </Button>
                                    </Link>
                                </Dropdown.Item>
                            }
                        </Dropdown.Menu>
                    </Dropdown>
                </Nav>

            </Container>
        </Navbar>
    );
}

export default NavBar;