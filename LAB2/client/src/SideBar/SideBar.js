import {useNavigate,useLocation} from 'react-router-dom';
function SideBar(props){
    const path = useLocation().pathname;
    const navigate = useNavigate();

    //const { loggedIn, user } = props;

return (
        <div className="d-flex flex-column flex-shrink-0 bg-light col-3 p-2">

                <aside>
                    <ul className="nav nav-pills flex-column mb-auto nav-fill ">
                        <li className="nav-item">
                            <button className={path==='/list-products' || path==='/' ? "nav-link active link-light text-start":"nav-link link-dark text-start"} onClick={()=>{navigate('/list-products')}} >
                                List All Products
                            </button>
                        </li>
                        <li className="nav-item">
                            <button className={path==='/get-product' ? "nav-link active link-light text-start":"nav-link link-dark text-start"}   onClick={()=>{navigate('/get-product')}}
                        >Get a Single Product</button></li>
                        <li className="nav-item">
                            <button className={path==='/get-profile-by-mail' ? "nav-link active link-light text-start":"nav-link link-dark text-start"}   onClick={()=>{navigate('/get-profile-by-mail')}}>
                                Get Profile By Username
                            </button>
                        </li>
                        <li className="nav-item">
                            <button className={path==='/add-profile' ? "nav-link active link-light text-start":"nav-link link-dark text-start"}  onClick={()=>{navigate('/add-profile')}}>
                                Add Profile
                            </button>
                        </li>
                        <li className="nav-item">
                            <button className={path==='/update-profile' ? "nav-link active link-light text-start":"nav-link link-dark text-start"}  onClick={()=>{navigate('/update-profile')}}>
                                Update Profile
                            </button>
                        </li>
                        <li className="nav-item">
                            <button className={path==='/login' ? "nav-link active link-light text-start":"nav-link link-dark text-start"}  onClick={()=>{navigate('/login')}}>
                                Login
                            </button>
                        </li>
                        {props.loggedIn && props.user.role==="Manager" &&
                            <li className="nav-item">
                                <button className={path==='/createExpert' ? "nav-link active link-light text-start":"nav-link link-dark text-start"}  onClick={()=>{navigate('/createExpert')}}>
                                    Create Expert
                                </button>
                            </li>
                        }

                    </ul>
                </aside>

        </div>

)

}


export default SideBar;