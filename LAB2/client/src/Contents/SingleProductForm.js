import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {useState} from "react";
import ShowProductsTable from "./ShowProductsTable";

function SingleProductForm(props){
    const [eanToSearch,setEanToSearch]=useState("");
    const [isSubmitted,setIsSubmitted]=useState(false);
    const [name,setName]=useState("");
    const [brand,setBrand]=useState("");
    const [errMsg,setErrMsg]=useState("");
    const handleSubmit=(event)=>{
        event.preventDefault();
        setIsSubmitted(true);
        props.getProduct(eanToSearch).then((product)=>{
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
            <Form.Label>Ean</Form.Label>
            <Form.Control type="text" placeholder="Enter product ean" onChange={ev=>setEanToSearch(ev.target.value)}/>
            <Form.Text className="text-muted">
            </Form.Text>
        </Form.Group>

        <Button variant="primary" type="submit">
            Submit
        </Button>
    </Form>

    if(errMsg!==""){
        return (<div>{errMsg}</div>)
    }
    if(isSubmitted){

        return <>
            <div>
                <ShowProductsTable listOfProducts={[{ean:eanToSearch,name,brand}]}></ShowProductsTable>
                <Button variant="primary" type="submit" onClick={()=>{
                    setErrMsg("");
                    setName("");
                    setBrand("");
                    setIsSubmitted(false);
                    setEanToSearch("");
                }
                }>
                    Search another
                </Button>
            </div>

        </>
    }

}

export default SingleProductForm;