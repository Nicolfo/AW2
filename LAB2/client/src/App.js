
import './App.css';
import 'bootstrap/dist/css/bootstrap.css';
import SideBar from "./SideBar/SideBar";
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import NavBar from "./Navbar/NavBar";
import API_Products from "./API/API_Products/API_Products";
import API_Profile from "./API/API_Profile/API_Profile";
import API_Login from "./API/API_Login/API_Login";
import ShowProductsTable from "./Contents/ShowProductsTable";
import {useEffect, useState} from "react";
import SingleProductForm from "./Contents/SingleProductForm";
import SingleProfileForm from "./Contents/SingleProfileForm";
import AddProfileForm from "./Contents/AddProfileForm";
import { useLocation } from 'react-router-dom'
import UpdateProfileForm from "./Contents/UpdateProfileForm";
import LoginForm from "./Contents/LoginForm";




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
                        <NavBar></NavBar><SideBar></SideBar><Content></Content>
                    </div>
                </div>}>
            </Route>
            <Route path='/list-products' element={
                <div className="container-fluid" style={{height: '90vh'}}>
                    <div className="row align-items-start">
                        <NavBar></NavBar><SideBar></SideBar><Content></Content>
                    </div>
                </div>}>
            </Route>
            <Route path='/get-product' element={
                <div className="container-fluid" style={{height: '90vh'}}>
                    <div className="row align-items-start">
                        <NavBar></NavBar><SideBar></SideBar><Content></Content>
                    </div>
                </div>}>
            </Route>
            <Route path='/get-profile-by-mail' element={
                <div className="container-fluid" style={{height: '90vh'}}>
                    <div className="row align-items-start">
                        <NavBar></NavBar><SideBar></SideBar><Content></Content>
                    </div>
                </div>}>
            </Route>
            <Route path='/add-profile' element={
                <div className="container-fluid" style={{height: '90vh'}}>
                    <div className="row align-items-start">
                        <NavBar></NavBar><SideBar></SideBar><Content></Content>
                    </div>
                </div>}>
            </Route>
            <Route path='/update-profile' element={
                <div className="container-fluid" style={{height: '90vh'}}>
                    <div className="row align-items-start">
                        <NavBar ></NavBar><SideBar></SideBar><Content></Content>
                    </div>
                </div>}>
            </Route>
            <Route path='/login' element={
                <div className="container-fluid" style={{height: '90vh'}}>
                    <div className="row align-items-start">
                        <NavBar ></NavBar><SideBar></SideBar><Content></Content>
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

    switch (path){
        case '/list-products':
            if(errorMsg!=="")
                return (<div className="col-9">{errorMsg}</div>)
            return (<div className="col-9"><ShowProductsTable listOfProducts={listOfProducts}></ShowProductsTable></div>)
        case '/get-product':
            return (<div className="col-9"><SingleProductForm getProduct={API_Products.getProduct}></SingleProductForm></div>);
        case '/get-profile-by-mail':
            return (<div className="col-9"><SingleProfileForm getProfile={API_Profile.getProfile}></SingleProfileForm></div>);
        case '/add-profile':
            return (<div className="col-9"><AddProfileForm addProfile={API_Profile.addProfile}></AddProfileForm></div>);
        case '/update-profile':
            return (<div className="col-9"><UpdateProfileForm updateProfile={API_Profile.updateProfile}></UpdateProfileForm></div>);
        case '/login':
            return (<div className="col-9"><LoginForm login={API_Login.login}></LoginForm></div>);
        default:
            if(errorMsg!=="")
                return (<div className="col-9">{errorMsg}</div>)
            return (<div className="col-9"><ShowProductsTable listOfProducts={listOfProducts}></ShowProductsTable></div>)
    }
}

export default App;
