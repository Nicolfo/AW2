
import './App.css';
import 'bootstrap/dist/css/bootstrap.css';
import SideBar from "./SideBar/SideBar";
import {BrowserRouter as Router, Routes, Route, useParams} from "react-router-dom";
import NavBar from "./Navbar/NavBar";
import API_Products from "./API/API_Products/API_Products";
import API_Profile from "./API/API_Profile/API_Profile";
import ShowProductsTable from "./Contents/ShowProductsTable";
import {useState} from "react";
import SingleProductForm from "./Contents/SingleProductForm";
import SingleProfileForm from "./Contents/SingleProfileForm";
import AddProfileForm from "./Contents/AddProfileForm";
import { useLocation } from 'react-router-dom'
import UpdateProfileForm from "./Contents/UpdateProfileForm";



function App() {
  return (<Router>
            <Layout></Layout>

        </Router>

  );
}


function Layout(props){
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
            <Route path='*' element={<h1>Path Not Found</h1>}></Route>
        </Routes>
    )
}
function Content(props){
    const [listOfProducts,setListOfProducts]=useState([]);
    //let {param}=useParams();
    const param = useLocation().pathname.toString();
    console.log(param)
    switch (param){
        case '/list-products':
            API_Products.getAllProducts().then((products)=>{setListOfProducts(old=>old=products)})
                .catch((err)=>{
                    return <div>{"Error " + err.status + " " + err.detail + " on API call " + err.instance}</div>;
                });
            return (<div className="col-9"><ShowProductsTable listOfProducts={listOfProducts}></ShowProductsTable></div>)
        case '/get-product':
            return (<div className="col-9"><SingleProductForm getProduct={API_Products.getProduct}></SingleProductForm></div>);
            break;
        case '/get-profile-by-mail':
            return (<div className="col-9"><SingleProfileForm getProfile={API_Profile.getProfile}></SingleProfileForm></div>);
            break;
        case '/add-profile':
            return (<div className="col-9"><AddProfileForm addProfile={API_Profile.addProfile}></AddProfileForm></div>);
            break;
        case '/update-profile':
            return (<div className="col-9"><UpdateProfileForm updateProfile={API_Profile.updateProfile}></UpdateProfileForm></div>);
            break;
        default:
            API_Products.getAllProducts().then((products)=>{setListOfProducts(products)});
            return (<div className="col-9"><ShowProductsTable listOfProducts={listOfProducts}></ShowProductsTable></div>)
    }
}

export default App;
