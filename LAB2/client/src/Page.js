import {useState} from "react";
import "./Page.css"
function Page(props){
    const a="[{\"ean\":\"4935531465706\",\"name\":\"JMT X-ring 530x2 Gold 104 Open Chain With Rivet Link for Kawasaki KH 400 a 1976\",\"brand\":\"JMT\"},{\"ean\":\"3528701753911\",\"name\":\"1x Summer Tyre Michelin Pilot Sport 4 255/40zr17 98y El\",\"brand\":\"Michelin\"},{\"ean\":\"5013879835005\",\"name\":\"Kent Bag of Rags 500g 100 Cotton KR500\",\"brand\":\"Kent\"},{\"ean\":\"5051747498761\",\"name\":\"Sealey Tools VS3815 Suspension Arm Lever\",\"brand\":\"Sealey\"},{\"ean\":\"4007817331927\",\"name\":\"Staedtler Lumocolor Medium Tip Water Soluble OHP Black Pen St33192\",\"brand\":\"Staedtler\"},{\"ean\":\"5052746141566\",\"name\":\"BM Fitting Kit FK80303B for Exhaust Catalytic Converter Bm80303h Fits OPEL\",\"brand\":\"BM Catalysts\"},{\"ean\":\"4260558851318\",\"name\":\"32gb Mini Button Security Camera WiFi WLAN IP Live App Video Tone Recording A106\",\"brand\":\"\"},{\"ean\":\"5905133258661\",\"name\":\"Pipe Connector Exhaust System FA1 913-962\",\"brand\":\"FA1\"}]"
    const [products,setProducts]=useState(JSON.parse(a));
    return(<>{
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
        </>:<>!!!no products in database!!!</>
    }</>);
}
export default Page;