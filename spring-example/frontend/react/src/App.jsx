import { Button, Spinner, Text, Flex } from '@chakra-ui/react';
import SidebarWithHeader from './shared/sidebar';
import { useEffect, useState } from 'react';
import { getCustomers } from './services/client.js';
import CardWithImage from './components/card.jsx';

const App = () => {
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);
    useEffect(() => {
        setLoading(true);
        getCustomers().then(res => {
            setCustomers(res.data);
        }).catch( err => {
            console.log(err);
        }).finally(() => {
            setLoading(false);
        })
    }, []);
    if(loading){
        return (
            <SidebarWithHeader>
            <Spinner />
            </SidebarWithHeader>
        );
    }
    if(customers.length <= 0){
        return (
            <SidebarWithHeader>
                <Text fontSize='3xl'>No customers yet!</Text>
            </SidebarWithHeader>
        );
    }
    return(
        <SidebarWithHeader>
            <Flex direction="row" wrap="wrap" justify="center" gap={6}>
            {customers.map((customer, index) => (
                <CardWithImage {...customer} key={index}></CardWithImage>
            ))}
            </Flex>
        </SidebarWithHeader>
    )
}


export default App;