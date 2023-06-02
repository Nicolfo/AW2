import Table from 'react-bootstrap/Table';

function ShowProductsTable(props){
    if(props.listOfProducts===undefined)
        return(<h1>Loading Products...</h1>)
    let rows=props.listOfProducts.map(row=>{
        return <tr key={row.productId}>
            <td>{row.productId}</td>
            <td>{row.name}</td>
            <td>{row.brand}</td>
        </tr>


    })
    return <Table striped bordered hover>
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
}

export default ShowProductsTable;