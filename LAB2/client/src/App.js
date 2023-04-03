
import './App.css';
import 'bootstrap/dist/css/bootstrap.css';
import SideBar from "./SideBar/SideBar";
import {BrowserRouter as Router, Routes, Route, useParams} from "react-router-dom";
import NavBar from "./Navbar/NavBar";
import API_Products from "./API/API_Products/API_Products";
import API_Profile from "./API/API_Profile/API_Profile";
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
    let {param}=useParams();

    switch (param){
        case 'list-products':
        case 'get-profile-by-mail':
        case 'add-profile':
        case 'update-profile':
        default:
    }
    return (<div className="col-9">Prova</div>)
}

export default App;
