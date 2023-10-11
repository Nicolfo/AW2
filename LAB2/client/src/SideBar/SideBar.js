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
                        {props.loggedIn && props.user.role==="Manager" ?
                            <>
                            <li className="nav-item">
                                <button className={path==='/createExpert' ? "nav-link active link-light text-start":"nav-link link-dark text-start"}  onClick={()=>{navigate('/add-profile')}}>
                                    Add Profile
                                </button>
                            </li>
                            <li className="nav-item">
                                <button className={path==='/update-profile' ? "nav-link active link-light text-start":"nav-link link-dark text-start"}  onClick={()=>{navigate('/update-profile')}}>
                                    Update Profile
                                </button>
                            </li>
                            </>
                            :
                            <></>
                        }
                        {props.loggedIn && (props.user.role==="Manager" || props.user.role=="Client") ?
                            <>
                                <li className="nav-item">
                                    <button className={path==='/add-new-ticket' ? "nav-link active link-light text-start":"nav-link link-dark text-start"}   onClick={()=>{navigate('/add-new-ticket')}}>
                                        Add {props.user.role==="Manager" ? "Priority and Expert for" : "New"} Ticket
                                    </button>
                                </li>
                            </>
                            :
                            <></>
                        }
                        {props.loggedIn && (props.user.role==="Manager" || props.user.role=="Client" || props.user.role=="Expert") ?
                            <>
                                <li className="nav-item">
                                    <button className={path==='/display-ticket' ? "nav-link active link-light text-start":"nav-link link-dark text-start"}   onClick={()=>{navigate('/display-ticket')}}>
                                        List Ticket
                                    </button>
                                </li>
                            </>
                            :
                            <></>
                        }
                        {path==='/login' ?
                            <li className="nav-item">
                                <button className="nav-link active link-light text-start" >
                                    Login
                                </button>
                            </li>
                            :
                            <></>
                        }

                    </ul>
                </aside>

        </div>

)

}


export default SideBar;
