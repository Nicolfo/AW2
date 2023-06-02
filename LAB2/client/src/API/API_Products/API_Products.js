const url = 'http://localhost:8081/';


async function getAllProducts(){



    return new Promise((resolve, reject)=>{

        fetch(url+"API/products/").then((response)=>{
            if(response.ok){
                response.json().then((res)=>{
                    resolve(res);
                }).catch(()=>{
                    reject({status:404,detail:"Cannot parse server response",instance:"GET "+url+"API/products/"});
                });

            }
            else{
                response.json().then((res)=>{
                    reject(res);
                }).catch(()=>{
                    reject({status:404,detail:"Cannot parse server response",instance:"GET "+url+"API/products/"});
                });

            }
        }).catch(()=>{
            reject({status:404,detail:"Cannot communicate with server",instance:"GET "+url+"API/products/"});
        });


    })


}

async function getProduct(productId=""){

    let products = [] ;
    let response;
    if(productId==="")
        throw {status:400,detail:"product id cannot be empty",instance:"/API/products/{productId}"};
    try{
        response = await fetch(url+"API/products/"+productId);
        products = await response.json();
    }catch (e) {
        throw {status:404,detail:"Cannot communicate with server",instance:"/API/products/{productId}"};
    }

    if(response.ok)
        return products;
    else
        throw products;
}

export default {getAllProducts,getProduct};
