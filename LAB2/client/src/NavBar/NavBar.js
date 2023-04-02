import "./NavBar.css"
import userImage from '../Resouces/images/userImage.png'
import UserDialog from "../Dialogs/UserDialog/UserDialog";
import {useState} from "react";

function NavBar() {

    const [openUserDialog, setOpenUserDialog] = useState(false);

    function closeUserDialogNavBar(){
        setOpenUserDialog(false);
    }

    return (
        <>
            <UserDialog openUserDialog={openUserDialog} closeUserDialogNavBar={closeUserDialogNavBar}></UserDialog>
            <div className="mainNavBar">
                <div className="containerUserImage" onClick={()=>{setOpenUserDialog(true)}}><img src={userImage}/></div>
            </div>

        </>
    );
}

export default NavBar;
