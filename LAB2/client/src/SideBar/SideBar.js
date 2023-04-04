import {useNavigate, useParams,useLocation} from 'react-router-dom';
function SideBar(props){
    //let {param}=useParams();
    const param = useLocation().pathname;
    const navigate = useNavigate();

return (
        <div className="d-flex flex-column flex-shrink-0 bg-light col-3 p-2">

                <aside>
                    <ul className="nav nav-pills flex-column mb-auto">
                        <li className="nav-item">
                            <a className={param==='/list-products' || param===undefined ? "nav-link active link-dark":"nav-link link-dark"} onClick={()=>{navigate('/list-products')}} >
                                List All Products
                            </a>
                        </li>
                        <li className="nav-item"> <a className={param==='/get-product' ? "nav-link active link-dark":"nav-link link-dark"}   onClick={()=>{navigate('/get-product')}}
                        >Get a Single Product</a></li>
                        <li className="nav-item"><a className={param==='/get-profile-by-mail' ? "nav-link active link-dark":"nav-link link-dark"}   onClick={()=>{navigate('/get-profile-by-mail')}}
                        >Get Profile By Mail</a></li>
                        <li className="nav-item"><a className={param==='/add-profile' ? "nav-link active link-dark":"nav-link link-dark"}  onClick={()=>{navigate('/add-profile')}}
                        >Add Profile</a></li>
                        <li className="nav-item"> <a className={param==='/update-profile' ? "nav-link active link-dark":"nav-link link-dark"}  onClick={()=>{navigate('/update-profile')}}>Update Profile</a></li>

                    </ul>
                </aside>

        </div>

)

}


export default SideBar;