import axios from 'axios';
const BASE_URL = 'https://api.jikan.moe/v4/';


const getType = (url) => {
    return /\/anime\//.test(url) ? 'anime' : 'manga'
}
const getMALId = (url) => {
    const match = url.match(/\/(anime|manga)\/(\d+)/);
    return match ? match[2] : null;
}

export const jikanSearch = async (url) => {
    const id = getMALId(url);
    if (!id) {
        throw new Error('Invalid URL, ID is missing.');
    }
    const type = getType(url);

    try {
        const response = await axios.get(`${BASE_URL}${type}/${id}`);
        const data = response.data;
        // Handle the API response data as needed
        return data;
    } catch (error) {
        // Handle any errors that occur during the API request
        throw new Error('Failed to fetch data from Jikan API');
    }
}