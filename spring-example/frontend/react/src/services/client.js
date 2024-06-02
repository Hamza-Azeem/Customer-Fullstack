import axios from 'axios'

const API_URL = "http://localhost:8080";

export const getCustomers = async () => {
    try{
        const apiUrl = import.meta.env.VITE_API_URL;
        return await axios.get(API_URL + "/customers");
    }catch(err){
        throw err;
    }
}
