import axios from "axios";

export const getHelloWorld = async () => {
    const response = await axios.get('/api/helloworld')
    return response.data;
}