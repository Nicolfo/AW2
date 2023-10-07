
import './App.css';
import 'bootstrap/dist/css/bootstrap.css';
import SideBar from "./SideBar/SideBar";
import {BrowserRouter as Router, Routes, Route, useNavigate} from "react-router-dom";
import NavBar from "./Navbar/NavBar";
import API_Products from "./API/API_Products/API_Products";
import API_Profile from "./API/API_Profile/API_Profile";
import API_User from "./API/API_User/API_User";
import ShowProductsTable from "./Contents/ShowProductsTable";
import {useEffect, useState} from "react";
import SingleProductForm from "./Contents/SingleProductForm";
import SingleProfileForm from "./Contents/SingleProfileForm";
import AddProfileForm from "./Contents/AddProfileForm";
import { useLocation } from 'react-router-dom'
import UpdateProfileForm from "./Contents/UpdateProfileForm";
import LoginForm from "./Contents/LoginForm";
import SignupForm from "./Contents/SignupForm";
import Chat from "./Contents/Chat";




function App() {
  return (<Router>
            <Layout></Layout>

        </Router>

  );
}


function Layout(){
    return (
        <div className="container-fluid" style={{height: '90vh'}}>
            <div className="row align-items-start">
                <Content></Content>
            </div>
        </div>
    )
}
function Content(){

    const [listOfProducts,setListOfProducts]=useState([]);
    const [errorMsg,setErrorMsg]=useState("");
    const [loggedIn, setLoggedIn] = useState(false);
    const [user, setUser] = useState("");
    const [jwtToken, setJwtToken] = useState('');
    const [signedUp, setSignedUp] = useState(false);
    const navigate = useNavigate();

    const path = useLocation().pathname.toString();
    useEffect(()=>{
        if(path==='/list-products' || path ==='/' || path === '')
        API_Products.getAllProducts().then((products)=>{
            setListOfProducts(((old) => old=products))
            setErrorMsg("");
        })
            .catch((err)=>{
                setErrorMsg("Error " + err.status + " " + err.detail + " on API call " + err.instance);
            });




    },[path])
    useEffect(()=>{
        if(jwtToken!==""){
            localStorage.setItem("jwt", jwtToken)
        }

        else {
            const jwt=localStorage.getItem("jwt");
            if(jwt!==null){
                setJwtToken(jwt);
                setLoggedIn(true);
            }

        }
        if(user!==""){
            localStorage.setItem("username",user.username);
        }else{
            const username=localStorage.getItem("username");
            if(username!==null){
                API_Profile.getProfile(username).then((loggedUser)=>{
                    setUser(loggedUser);
                    if(path=='/login' ||path=='/signup')
                        navigate('/');
                }).catch((err)=>{
                    setErrorMsg(err.detail)
                    setJwtToken('');
                    setUser('');
                    setLoggedIn(false);
                    setSignedUp(false);
                    navigate("login");
                });
            }
        }


    },[jwtToken,user])


    const doLogIn = async (username,password) => {

            API_User.login(username,password).then((token)=>{
                setJwtToken(token);
                setLoggedIn(true);
                API_Profile.getProfile(username).then((loggedUser)=>{
                    setUser(loggedUser);
                    if(path=='/login' ||path=='/signup')
                        navigate('/');

                }).catch((err)=>{
                    setErrorMsg(err.detail)
                    setJwtToken('');
                    setUser('');
                    setLoggedIn(false);
                    setSignedUp(false);
                    navigate("login");
                });

            })
                .catch((err)=>{
                    setErrorMsg(err.detail)
                    navigate("login");
            })



    }

    const doLogout = () => {
        localStorage.removeItem("username");
        localStorage.removeItem("jwt")
        setJwtToken('');
        setUser('');
        setLoggedIn(false);
        setSignedUp(false);
        navigate("/");
    }

    const doSignup = async (username,email,password) => {  //fa anche il login per l'user appena creato
        try {
            await API_User.signup(username,email,password);
            setSignedUp(true);
            await doLogIn(username,password);
        }
        catch (err) {
            throw err; // error handled in SignupForm
        }
    }

    const createExpert = async (username,email,password) => { //questa crea soltanto
        try {
            await API_User.createExpert(username,email,password,jwtToken);
            setSignedUp(true);
        }
        catch (err) {
            throw err; // error handled in SignupForm
        }
    }


    switch (path){
        case '':
        case '/':
        case '/list-products':
            if(errorMsg!=="")
                return (<><NavBar loggedIn={loggedIn} user={user} logout={doLogout} login={doLogIn}></NavBar><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9">{errorMsg}</div></>)
            return (<><NavBar loggedIn={loggedIn} user={user} logout={doLogout} login={doLogIn}></NavBar><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9"><ShowProductsTable listOfProducts={listOfProducts}></ShowProductsTable></div></>)
        case '/get-product':
            return (<><NavBar loggedIn={loggedIn} user={user} logout={doLogout} login={doLogIn}></NavBar><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9"><SingleProductForm getProduct={API_Products.getProduct}></SingleProductForm></div></>);
        case '/get-profile-by-mail':
            return (<><NavBar loggedIn={loggedIn} user={user} logout={doLogout} login={doLogIn}></NavBar><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9"><SingleProfileForm getProfile={API_Profile.getProfile}></SingleProfileForm></div></>);
        case '/add-profile':
            return (<><NavBar loggedIn={loggedIn} user={user} logout={doLogout} login={doLogIn}></NavBar><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9"><AddProfileForm addProfile={API_Profile.addProfile}></AddProfileForm></div></>);
        case '/update-profile':
            return (<><NavBar loggedIn={loggedIn} user={user} logout={doLogout} login={doLogIn}></NavBar><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9"><UpdateProfileForm updateProfile={API_Profile.updateProfile}></UpdateProfileForm></div></>);
        case '/login':
            if(loggedIn && errorMsg!=="")
                return (<><NavBar loggedIn={loggedIn} user={user} logout={doLogout} login={doLogIn}></NavBar><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9">{errorMsg}</div></>)
            if(loggedIn)
                return (<><NavBar loggedIn={loggedIn} user={user} logout={doLogout} login={doLogIn}></NavBar><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9">You are already logged in!</div></>)
            return (<><NavBar loggedIn={loggedIn} user={user} logout={doLogout} login={doLogIn}></NavBar><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9"><LoginForm login={doLogIn} loggedIn={loggedIn} logout={doLogout} errorMsg={errorMsg} setErrorMsg={setErrorMsg} isLoggedIn={loggedIn}></LoginForm></div></>);
        case '/signup':
            if(loggedIn)
                return (<><NavBar loggedIn={loggedIn} user={user} logout={doLogout} login={doLogIn}></NavBar><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9">You are already logged in!</div></>)
            return (<><NavBar loggedIn={loggedIn} user={user} logout={doLogout} login={doLogIn}></NavBar><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9"><SignupForm signup={doSignup} signedUp={signedUp} createExp={false}></SignupForm></div></>);
        case '/createExpert':
            if(user!=null && user.role==="Manager" )
                return (<><NavBar loggedIn={loggedIn} user={user} logout={doLogout} login={doLogIn}></NavBar><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9"><SignupForm createExpert={createExpert} signedUp={signedUp} createExp={true} setSignedUp={setSignedUp}></SignupForm></div></>);
            else
                return (<><NavBar loggedIn={loggedIn} user={user} logout={doLogout} login={doLogIn}></NavBar><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9">You have to be a logged in manager to use that function</div></>)
        case '/chat':
            return <Chat></Chat>
        default:
            return <h1>Path not found</h1>
    }
}

export default App;
