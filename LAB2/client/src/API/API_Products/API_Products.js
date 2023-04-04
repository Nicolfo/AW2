const url = 'http://localhost:8080/';
let response;

async function getAllProducts(){
    let products = [] ;

        response = await fetch(url+"API/products/");

        products = await response.json();

    if(response.ok)
        return products;
    else
        throw products;

}

async function getProduct(ean=""){
    let products = [] ;

        response = await fetch(url+"API/products/"+ean);
        products = await response.json();
        if(response.ok)
            return products;
        else
        throw products;
}

export default {getAllProducts,getProduct};
