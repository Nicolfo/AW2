import './UserDialog.css'
import {useEffect, useState} from "react";
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {Box, Tab, Tabs} from "@mui/material";
import * as PropTypes from "prop-types";
import API_Profile from "../../API/API_Profile/API_Profile";

function TabPanel(props) {
    return null;
}

TabPanel.propTypes = {
    index: PropTypes.number,
    children: PropTypes.node
};

function UserDialog(props) {

    const [openUserDialog, setOpenUserDialog] = useState(false);
    const [indexTab, setIndexTab] = useState(0);
    const [userLogged,setUserLogged] = useState(null);
    const [errorMessage,setErrorMessage] = useState("");
    const [emailUser,setEmailUser]= useState("");
    const [updatedUser,setUpdatedUser] = useState({"name":"","email":""});

    useEffect(()=>{
        setIndexTab(0);
        setOpenUserDialog(props.openUserDialog);
    },[props.openUserDialog])

    async function handleValueChangeEmailLogIn(event=null, clickedSend = false){
        if((event && event.key=="Enter") || clickedSend ){
            if(!emailUser.match('[a-z0-9]+@[a-z]+\.[a-z]{2,3}'))
            {
                setErrorMessage("No valid email ...")
                return;
            }
            let tmpUser = await API_Profile.getProfile(emailUser);
            if(!tmpUser) setErrorMessage("No User found with this email !!!");
            else{
                setErrorMessage("");
                setUserLogged(tmpUser);
                setUpdatedUser(tmpUser)
            }
        }
    }

    async function handleAddNewUser(){
        if(updatedUser.email && updatedUser.name){
            if(!updatedUser.email.match('[a-z0-9]+@[a-z]+\.[a-z]{2,3}'))
            {
                setErrorMessage("No valid email ...")
                return;
            }
            let tmpUser = await API_Profile.addProfile(updatedUser);
            if(tmpUser){
                setErrorMessage("");
                setUserLogged(tmpUser);
                setOpenUserDialog(false);
                props.closeUserDialogNavBar();
            }
            else{
                setErrorMessage("Error adding profile =(")
            }
        }
    }

    async function handleUpdateUser(){
        if(updatedUser.email && updatedUser.name){
            if(updatedUser.email == userLogged.email && updatedUser.name == userLogged.name){
                setErrorMessage("No changes have been done ...")
                return ;
            }
            if(!updatedUser.email.match('[a-z0-9]+@[a-z]+\.[a-z]{2,3}'))
            {
                setErrorMessage("No valid email ...")
                return;
            }
            let tmpUser = await API_Profile.updateProfile(updatedUser);
            if(tmpUser){
                setErrorMessage("");
                setUserLogged(tmpUser);
                setOpenUserDialog(false);
                props.closeUserDialogNavBar();
            }
            else{
                setErrorMessage("Error uploading profile =(")
            }
        }
    }

    return (
        <>
            <Dialog open={openUserDialog} onClose={()=>{setOpenUserDialog(false);props.closeUserDialogNavBar()}}>
                <Box>
                <DialogTitle>
                    <Tabs
                        value={indexTab}
                        onChange={(event,index)=>{
                                  setIndexTab(index);
                                  if(index==1)
                                      setUpdatedUser({"name":"","email":""})
                                  else{
                                      setUpdatedUser(userLogged)
                                  }
                        }}
                        indicatorColor="secondary"
                        textColor="inherit" variant="fullWidth" aria-label="full width tabs example" >
                        <Tab label={!userLogged ? "Log In" : "Edit User"} />
                        <Tab label="Add User" />
                    </Tabs>
                </DialogTitle>
                <DialogContent className="main-container-log-in">
                    {indexTab == 0 ?
                        !userLogged ?
                            <>
                                <span className="text-description-user-dialog">Write your email to log in </span>
                                <input placeholder="Write your email" pattern={"/^(([^<>()[\\]\\\\.,;:\\s@\"]+(\\.[^<>()[\\]\\\\.,;:\\s@\"]+)*)|.(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$/"}
                                       className="input-log-in-user-dialog"
                                       onKeyDown={(event)=>handleValueChangeEmailLogIn(event)}
                                       onChange={event => setEmailUser(event.target.value)}
                                       type={<textarea name="" id="" rows="1"></textarea>}/>
                            </>
                            :
                            (<>
                                <div className="text-welcome-user-dialog">{"Hello "+userLogged.name+" !"}</div>
                                <div className="container-edit-name-user-dialog">
                                    <span className="text-edit-description-user-dialog">Name : </span>
                                    <input value={updatedUser.name}
                                           onChange={(event)=>setUpdatedUser({...updatedUser,"name": event.target.value})}
                                            type={"text"} className="input-edit-user-dialog"/>
                                </div>
                                <div className="container-edit-name-user-dialog">
                                    <span className="text-edit-description-user-dialog">Email : </span>
                                    <input value={updatedUser.email}
                                           onChange={(event)=>setUpdatedUser({...updatedUser,"email": event.target.value})}
                                           type={"email"} className="input-edit-user-dialog"/>
                                </div>
                            </>)
                        :
                        indexTab == 1 ?
                        <>
                            <div className="text-welcome-user-dialog">{"Add New User :"}</div>
                            <div className="container-edit-name-user-dialog">
                                <span className="text-edit-description-user-dialog">Name : </span>
                                <input placeholder="Write name"
                                       value={updatedUser.name}
                                       onChange={(event)=>setUpdatedUser({...updatedUser,"name": event.target.value})}
                                       type={"text"} className="input-edit-user-dialog"/>
                            </div>
                            <div className="container-edit-name-user-dialog">
                                <span className="text-edit-description-user-dialog">Email : </span>
                                <input placeholder="Write email"
                                       value={updatedUser.email}
                                       onChange={(event)=>setUpdatedUser({...updatedUser,"email": event.target.value})}
                                       type={"email"} className="input-edit-user-dialog"/>
                            </div>
                        </>
                        :
                         <>
                         </>
                    }
                    <span className={errorMessage.trim()!="" ? "error-message-user-dialog" : ""}>{errorMessage}</span>
                </DialogContent>
                <DialogActions>
                    <Button onClick={()=>{setOpenUserDialog(false);props.closeUserDialogNavBar()}}>Close</Button>
                    <Button onClick={async ()=>{
                        if(indexTab ==0 && !userLogged)
                            await  handleValueChangeEmailLogIn(null,true)
                        else if(indexTab==0 && userLogged)
                            await handleUpdateUser();
                        else if(indexTab==1)
                            await handleAddNewUser();
                    }}>Send</Button>
                </DialogActions>
                </Box>
            </Dialog>

        </>
    );
}

export default UserDialog;
