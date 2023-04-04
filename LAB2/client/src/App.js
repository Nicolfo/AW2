
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




function App() {
  return (<Router>
            <Layout></Layout>

        </Router>

  );
}


function Layout(props){
    return ( <Routes>

        <Route path='/:param' element={
            <div className="container-fluid" style={{height: '90vh'}}>
                <div className="row align-items-start">
                    <NavBar></NavBar><SideBar></SideBar><Content></Content>
                </div>
            </div>}>
        </Route>


    </Routes>)
}
function Content(props){
    const [listOfProducts,setListOfProducts]=useState([]);
    let {param}=useParams();
    switch (param){
        case 'list-products':
            API_Products.getAllProducts().then((products)=>{setListOfProducts(old=>old=products)});
            return (<div className="col-9"><ShowProductsTable listOfProducts={listOfProducts}></ShowProductsTable></div>)
        case 'get-product':
            return (<div className="col-9"><SingleProductForm getProduct={API_Products.getProduct}></SingleProductForm></div>);
            break;
        case 'get-profile-by-mail':
            return (<div className="col-9"><SingleProfileForm getProfile={API_Profile.getProfile}></SingleProfileForm></div>);
            break;
        case 'add-profile':
            return (<div className="col-9"><AddProfileForm addProfile={API_Profile.addProfile}></AddProfileForm></div>);
            break;
        case 'update-profile':
            break;
        default:
            API_Products.getAllProducts().then((products)=>{setListOfProducts(products)});
            return (<div className="col-9"><ShowProductsTable listOfProducts={listOfProducts}></ShowProductsTable></div>)
    }
}

export default App;
