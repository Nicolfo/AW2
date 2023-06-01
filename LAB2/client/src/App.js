
import './App.css';
import 'bootstrap/dist/css/bootstrap.css';
import SideBar from "./SideBar/SideBar";
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
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




function App() {
  return (<Router>
            <Layout></Layout>

        </Router>

  );
}


function Layout(){
    return (
        <Routes>
            <Route path='/' element={
                <div className="container-fluid" style={{height: '90vh'}}>
                    <div className="row align-items-start">
                        <NavBar></NavBar><Content></Content>
                    </div>
                </div>}>
            </Route>
            <Route path='/list-products' element={
                <div className="container-fluid" style={{height: '90vh'}}>
                    <div className="row align-items-start">
                        <NavBar></NavBar><Content></Content>
                    </div>
                </div>}>
            </Route>
            <Route path='/get-product' element={
                <div className="container-fluid" style={{height: '90vh'}}>
                    <div className="row align-items-start">
                        <NavBar></NavBar><Content></Content>
                    </div>
                </div>}>
            </Route>
            <Route path='/get-profile-by-mail' element={
                <div className="container-fluid" style={{height: '90vh'}}>
                    <div className="row align-items-start">
                        <NavBar></NavBar><Content></Content>
                    </div>
                </div>}>
            </Route>
            <Route path='/add-profile' element={
                <div className="container-fluid" style={{height: '90vh'}}>
                    <div className="row align-items-start">
                        <NavBar></NavBar><Content></Content>
                    </div>
                </div>}>
            </Route>
            <Route path='/update-profile' element={
                <div className="container-fluid" style={{height: '90vh'}}>
                    <div className="row align-items-start">
                        <NavBar ></NavBar><Content></Content>
                    </div>
                </div>}>
            </Route>
            <Route path='/login' element={
                <div className="container-fluid" style={{height: '90vh'}}>
                    <div className="row align-items-start">
                        <NavBar ></NavBar><Content></Content>
                    </div>
                </div>}>
            </Route>
            <Route path='/signup' element={
                <div className="container-fluid" style={{height: '90vh'}}>
                    <div className="row align-items-start">
                        <NavBar ></NavBar><Content></Content>
                    </div>
                </div>}>
            </Route>
            <Route path='/createExpert' element={
                <div className="container-fluid" style={{height: '90vh'}}>
                    <div className="row align-items-start">
                        <NavBar ></NavBar><Content></Content>
                    </div>
                </div>}>
            </Route>
            <Route path='*' element={<h1>Path Not Found</h1>}></Route>
        </Routes>
    )
}
function Content(){

    const [listOfProducts,setListOfProducts]=useState([]);
    const [errorMsg,setErrorMsg]=useState("");
    const [loggedIn, setLoggedIn] = useState(false);
    const [user, setUser] = useState('');
    const [jwtToken, setJwtToken] = useState('');
    const [signedUp, setSignedUp] = useState(false);


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

    const doLogIn = async (username,password) => {
        try {
            const token = await API_User.login(username,password);
            setJwtToken(token);
            const loggedUser = await API_Profile.getProfile(username);
            setUser(loggedUser);
            setLoggedIn(true);
        }
        catch (err) {
            throw err; // error handled in LoginForm
        }
    }

    const doLogout = () => {
        setJwtToken('');
        setUser('');
        setLoggedIn(false);
        setSignedUp(false);
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

    const createExpert = async (username,email,password,jwtToken) => { //questa crea soltanto
        try {
            await API_User.createExpert(username,email,password,jwtToken);
            setSignedUp(true);
        }
        catch (err) {
            throw err; // error handled in SignupForm
        }
    }


    switch (path){
        case '/list-products':
            if(errorMsg!=="")
                return (<div className="col-9">{errorMsg}</div>)
            return (<><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9"><ShowProductsTable listOfProducts={listOfProducts}></ShowProductsTable></div></>)
        case '/get-product':
            return (<><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9"><SingleProductForm getProduct={API_Products.getProduct}></SingleProductForm></div></>);
        case '/get-profile-by-mail':
            return (<><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9"><SingleProfileForm getProfile={API_Profile.getProfile}></SingleProfileForm></div></>);
        case '/add-profile':
            return (<><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9"><AddProfileForm addProfile={API_Profile.addProfile}></AddProfileForm></div></>);
        case '/update-profile':
            return (<><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9"><UpdateProfileForm updateProfile={API_Profile.updateProfile}></UpdateProfileForm></div></>);
        case '/login':
            return (<><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9"><LoginForm login={doLogIn} loggedIn={loggedIn} jwtToken={jwtToken} logout={doLogout}></LoginForm></div></>);
        case '/signup':
            return (<><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9"><SignupForm signup={doSignup} signedUp={signedUp}></SignupForm></div></>);
        case '/createExpert':
            return (<><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9"><SignupForm createExpert={createExpert} signedUp={signedUp}></SignupForm></div></>);

        default:
            if(errorMsg!=="")
                return (<><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9">{errorMsg}</div></>)
            return (<><SideBar loggedIn={loggedIn} user={user}></SideBar><div className="col-9"><ShowProductsTable listOfProducts={listOfProducts}></ShowProductsTable></div></>)
    }
}

export default App;
