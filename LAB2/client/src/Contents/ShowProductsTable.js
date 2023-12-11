import Table from 'react-bootstrap/Table';
import Button from "react-bootstrap/Button";
import {useState} from "react";

function ShowProductsTable(props) {
    let [index, setIndex] = useState(0);
    if (props.listOfProducts === undefined)
        return (<h1>Loading Products...</h1>)
    let rows = props.listOfProducts.slice(0 + index * 10, 10 + index * 10).map(row => {
        return <tr key={row.productId}>
            <td>{row.productId}</td>
            <td>{row.name}</td>
            <td>{row.brand}</td>
        </tr>


    })
    return <><Table striped bordered hover>
        <thead>
        <tr>
            <th>ProductId</th>
            <th>Name</th>
            <th>Brand</th>
        </tr>
        </thead>
        <tbody>
        {rows}
        </tbody>
    </Table>
        <div style={{position:"fixed",bottom:"10px"}}>
            <Button disabled={index === 0} onClick={() => {
                if (index > 0)
                    setIndex((old) => {
                        return old - 1;
                    });
            }
            }>Prev</Button>
            <Button disabled={index === Math.ceil(props.listOfProducts.length / 10) - 1} onClick={() => {

                if (index < (props.listOfProducts.length / 10 - 1))
                    setIndex((old) => {
                        return old + 1;
                    });
            }
            }>Next</Button>
            Page {index + 1} of {Math.ceil(props.listOfProducts.length / 10)}

        </div>
    </>

}

export default ShowProductsTable;