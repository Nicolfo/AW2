import {useEffect, useState} from "react";
import "./Page.css"
import API_Products from "../API/API_Products/API_Products";
import searchImage from '../Resouces/images/searchImage.png'
import sadImage from '../Resouces/images/sadImage.png'
function Page(props){

    const [fisrtProductsDownloaded,setFisrtProductsDownloaded] = useState([])
    const [products,setProducts]=useState([]);
    const [valueSearchBar,setValueSearchBar]= useState("");

    useEffect(()=>{

        async function getProducts(){
            let tmpProducts = await API_Products.getAllProducts();
            setFisrtProductsDownloaded(tmpProducts);
            setProducts(tmpProducts);
        }

        getProducts();

    },[]);

    async function handleValueChangeSearchBar(event=null,clicked=false){
        if((event && event.key=="Enter") || clicked){
            if(valueSearchBar=="") {
                setProducts(await API_Products.getAllProducts());
                    return ;
            }
            let tmpProduct = fisrtProductsDownloaded.find(product => (product.ean && product.ean.includes(valueSearchBar) || (product.name && product.name.toLowerCase().includes(valueSearchBar.toLowerCase()))))
            if(tmpProduct && tmpProduct.ean)
                setProducts(await API_Products.getProduct(tmpProduct.ean));
            else
                setProducts([]);
        }
    }

    return(<>
        <div className="container-searchBar">
            <span className="text-searchBar">Search Product: </span>
            <input className="input-textarea-searchBar" value={valueSearchBar} placeholder="Write something ..."
                   onKeyDown={(event)=>handleValueChangeSearchBar(event)}
                   onChange={event => setValueSearchBar(event.target.value)}
                   type={<textarea name="" id="" rows="1"></textarea>}/>
            <img src={searchImage} onClick={()=>handleValueChangeSearchBar(null,true)}></img>
        </div>
        {
        products && products.length>0 ?
        <>
            <div className='titleTable' >
                <div className='singleProduct_ean'>EAN</div>
                <div className='singleProduct_name'>NAME</div>
                <div className='singleProduct_brand'>BRAND</div>
            </div>
            {
                products.map(p=>{return (<>
                <div className='singleProduct' id={'singleProduct_'+p.ean}>
                    <div className='singleProduct_ean'>{p.ean}</div>
                    <div className='singleProduct_name'>{p.name}</div>
                    <div className='singleProduct_brand'>{p.brand}</div>
                </div>

                </>) })
            }
        </>
        :
        <>
            <div className="mainPage_noProducts">
                <span>!!!No Product in Database!!!</span>
                <img src={sadImage}/>
            </div>

        </>
    }</>);
}
export default Page;
