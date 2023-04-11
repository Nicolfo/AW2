import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {useState} from "react";
import ShowProductsTable from "./ShowProductsTable";

function SingleProductForm(props){
    const [idToSearch,setIdToSearch]=useState("");
    const [isSubmitted,setIsSubmitted]=useState(false);
    const [name,setName]=useState("");
    const [brand,setBrand]=useState("");
    const [errMsg,setErrMsg]=useState("");
    const handleSubmit=(event)=>{
        event.preventDefault();
        setIsSubmitted(true);
        props.getProduct(idToSearch).then((product)=>{
            setErrMsg("");
            setName(product.name);
            setBrand(product.brand);

        })
            .catch((err)=>{
                setErrMsg("Error "+err.status+" "+err.detail+" on API call "+err.instance)

            })
    }
    if(!isSubmitted && errMsg==="")
    return  <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3" controlId="formBasicEmail">
            <Form.Label>ProductId</Form.Label>
            <Form.Control type="text" placeholder="Enter productId" onChange={ev=>setIdToSearch(ev.target.value)}/>
            <Form.Text className="text-muted">
            </Form.Text>
        </Form.Group>

        <Button variant="primary" type="submit">
            Submit
        </Button>
    </Form>

    if(errMsg!==""){
        return (
            <div>
                {errMsg}
                <br/>
                <Button variant="primary" type="submit" onClick={()=>{
                    setErrMsg("");
                    setName("");
                    setBrand("");
                    setIsSubmitted(false);
                    setIdToSearch("");
                }
                }>
                    Search another
                </Button>
            </div>)
    }
    if(isSubmitted && name===""){
        return <div>Waiting Server Response ...</div>
    }
    if(isSubmitted){

        return <>
            <div>
                <ShowProductsTable listOfProducts={[{productId:idToSearch,name,brand}]}></ShowProductsTable>
                <br></br>
                <Button variant="primary" type="submit" onClick={()=>{
                    setErrMsg("");
                    setName("");
                    setBrand("");
                    setIsSubmitted(false);
                    setIdToSearch("");
                }
                }>
                    Search another
                </Button>
            </div>

        </>
    }
    else return (<>Please fill productID field
        <br></br>
        <Button variant="primary" type="submit" onClick={()=>{
            setErrMsg("");
            setName("");
            setBrand("");
            setIsSubmitted(false);
            setIdToSearch("");
        }
        }>
            Search another
        </Button>

    </>)
}

export default SingleProductForm;