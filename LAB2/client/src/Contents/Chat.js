import {useEffect, useState} from "react";
import Form from "react-bootstrap/Form";
import SockJS from 'sockjs-client';
import {Client} from '@stomp/stompjs';
import {Button, Col, Container, FormControl, InputGroup, Row} from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.css';
import {BsFillFileEarmarkPlusFill,BsFillTrashFill} from "react-icons/bs";


function Chat() {
    let [isSubmitted, setSubmitted] = useState(false);
    let [errorMessage, setErrorMessage] = useState("");
    let [userName, setUserName] = useState("");
    let [chatId, setChatId] = useState("");
    let [messages, setMessages] = useState([]);
    let [newMessage, setNewMessage] = useState("");
    let [stompClient, setStompClient] = useState(new Client());
    let [files, setFiles] = useState([]);

    const connect = (event) => {
        event.preventDefault();
        setSubmitted(true);
        if (userName && userName.length > 0) {
            setStompClient((client) => {
                client.beforeConnect=getOldMessages;
                client.brokerURL = "ws://localhost:8080/websocket"
                client.onConnect = onConnected;
                client.onStompError = onError;
                console.log("starting connection")
                client.activate();
                return client;
            })
        }
    }

    const onConnected = (frame) => {

        console.log("connected to " + frame)
        stompClient.subscribe('/topic/' + chatId, onMessageReceived);
        stompClient.publish({
                destination: "/app/chat.register/" + chatId, body:
                    JSON.stringify({sender: userName, type: 'JOIN',content: userName + ' joined!'})
            }
        );


        // Tell your username to the server

    }



    // Tell your username to the server


    const onError = (error) => {
        setErrorMessage("Error " + error + ", try again later");
    }


    const send = async (event) => {
        event.preventDefault();
        if (newMessage && stompClient) {

            let chatMessage = {
                sender: userName,
                content: newMessage,
                type: 'CHAT',
                files: files
            };
            stompClient.publish({destination: "/app/chat.send/" + chatId, body: JSON.stringify(chatMessage)})
            setNewMessage("");
            setFiles([]);
        }
    }

    const onMessageReceived = (payload) => {
        let message = JSON.parse(payload.body);
        console.log(message);
        if (message.type === 'JOIN') {
            message.content = message.sender + ' joined!';
        } else if (message.type === 'LEAVE') {
            message.content = message.sender + ' left!';
        }

        setMessages((old) => [...old, message])
    }
    const uploadFile = async (file) => {
        const formData = new FormData();
        formData.append("file", file, file.name);

        let respJson;
        let response;
        try {
            response = await fetch("http://127.0.0.1:8080/uploadFile", {
                method: "PUT",
                body: formData,
            });
            respJson = await response.json();
        } catch (e) {
            throw {status: 404, detail: "Cannot communicate with server"}
        }
        return respJson

    }
    const handleFileChange = (event) => {
        event.preventDefault();

        let vec = [...event.target.files];
        event.target.value=null;
        console.log("files detected", vec)
        const uploadPromises = [];

        vec.forEach((file) => {
            uploadPromises.push(uploadFile(file));
        });
        // Wait for all file uploads to complete
        Promise.all(uploadPromises)
            .then((responses) => {
                // Update the 'files' state with the responses for all files
                setFiles((prevFiles) => [...prevFiles, ...responses]);
            })
            .catch((error) => {
                console.error("Error uploading files:", error);
            });
    }


    async function getOldMessages() {
        let response = await fetch("http://127.0.0.1:8080/chat.old/" + chatId);
        try{
            if(response.data!=='' && response.ok){

                let data = await response.json();
                setMessages([...data,...messages]);
            }
        }catch (e) {

        }
        //return data;
    }




    if (!isSubmitted) {


        return <Form onSubmit={connect}>
            <Form.Group className="mb-3">
                <Form.Label>Username</Form.Label>
                <Form.Control type="text" name="username" placeholder="Enter username "
                              onChange={ev => setUserName(ev.target.value)}/>
                <Form.Text className="text-muted">
                </Form.Text>

                <Form.Label>chatID</Form.Label>
                <Form.Control type="text" name="chatID" placeholder="Enter chatId "
                              onChange={ev => setChatId(ev.target.value)}/>
                <Form.Text className="text-muted">
                </Form.Text>


            </Form.Group>
            <Button variant="primary" type="submit">
                Submit
            </Button>

        </Form>
    }
    else {
        let old = null;
        return <div className="container">
            <div className="row d-flex justify-content-center">
                <div >
                    <div className="card">
                        <div
                            className="card-header d-flex justify-content-between align-items-center p-3"
                            style={{borderTop: "4px solid #ffa900"}}
                        >
                            <h5 className="mb-0">Chat ID {chatId}</h5>

                        </div>
                        <div className="card-body overflow-auto scrollbar" style={{position: "relative", height: 400}}>


                            {messages.map((elem) => {
                                if (elem.type === 'CHAT')
                                    if (elem.sender !== old || old == null) {
                                        old = elem.sender;
                                        return <>
                                            <div className={elem.sender!==userName?"d-flex justify-content-between":"d-flex justify-content-end pt-1"}>
                                                <p className="small mb-1">{elem.sender}</p>
                                            </div>
                                            <div className={elem.sender!==userName?"d-flex flex-row justify-content-start":"d-flex flex-row justify-content-end pt-1"}>
                                                <div>

                                                    <div className={elem.sender!==userName?"small p-2 ms-3 mb-3 rounded-3":"small p-2 me-3 mb-3 text-white rounded-3 bg-warning"}
                                                         style={{backgroundColor: "#f5f6f7"}}>
                                                        {elem.content}
                                                        <div style={{fontSize:"0.6rem"}}>{elem.files.map((it)=>{
                                                            return <a href={"http://127.0.0.1:8080/getFile/"+it.id} download>{it.fileName} </a>
                                                        })
                                                        }</div>
                                                    </div>

                                                </div>

                                            </div>
                                        </>;
                                    } else
                                        return <>
                                            <div className={elem.sender!==userName?"d-flex flex-row justify-content-start":"d-flex flex-row justify-content-end pt-1"}>
                                                <div>
                                                    <div className={elem.sender!==userName?"small p-2 ms-3 mb-3 rounded-3":"small p-2 me-3 mb-3 text-white rounded-3 bg-warning"}
                                                         style={{backgroundColor: "#f5f6f7"}}>
                                                        {elem.content}
                                                        <div style={{fontSize:"0.6rem"}}>{elem.files.map((it)=>{
                                                            return <><a href={"http://127.0.0.1:8080/getFile/"+it.id} download>{it.fileName}</a><br/></>
                                                        })
                                                        }</div>
                                                    </div>
                                                </div>
                                            </div>
                                        </>
                                else
                                    return <div>{elem.content}</div>
                            })
                            }

                        </div>
                        {
                            files.length!==0?
                                <div className="card-footer text-muted d-flex justify-content-start align-items-center p-2">
                                    {files.map((elem)=> {
                                        return <div >{elem.fileName} <BsFillTrashFill onClick={()=>{setFiles((filesOld)=>{
                                            return filesOld.filter((it)=>it!==elem)
                                        })}}/></div>
                                    })}




                                </div>

                                :<></>
                        }
                        <form onSubmit={send}>
                            <div className="card-footer text-muted d-flex justify-content-start align-items-center p-3">
                                <div className="input-group mb-0">

                                    <input
                                        type="text"
                                        className="form-control"
                                        placeholder="Type message"
                                        aria-label="Recipient's username"
                                        aria-describedby="button-addon2"
                                        onChange={ev => setNewMessage(ev.target.value)}
                                        value={newMessage}
                                    />
                                    <label htmlFor="file_upload" style={{paddingLeft:"1rem",paddingRight:"1rem"}}>
                                        <BsFillFileEarmarkPlusFill/>
                                    </label>
                                    <input id="file_upload"type="file" style={{display:"none"}} onChange={handleFileChange}  multiple/>
                                    <button
                                        className="btn btn-warning"
                                        type="submit"
                                        id="button-addon2"
                                        style={{paddingTop: ".55rem"}}
                                    >
                                        Send
                                    </button>



                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    }


}

export default Chat;