const url = 'http://localhost:8080/';
let response;

async function getAllProducts(){
    let products = [] ;
    try{
        response = await fetch(url+"API/products/");
        products = await response.json();
    }
    catch(err){
        console.log(err);
    }

    return products;
}

async function getProduct(ean=""){
    let products = [] ;
    try{
        response = await fetch(url+"API/products/"+ean);
        products.push(await response.json());
    }
    catch(err){
        console.log(err);
    }

    return products;
}

export default {getAllProducts,getProduct};
