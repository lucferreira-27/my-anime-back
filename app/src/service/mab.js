import axios from 'axios';
import { apiUrl as devApiUrl } from '../config/config.dev';
import { apiUrl as prodApiUrl } from '../config/config.prod';

// Determine the environment
const environment = `dev`;

// Import the appropriate API URL based on the environment
const apiUrl = environment === 'production' ? prodApiUrl : devApiUrl;

// Create an Axios instance with the base URL
const axiosInstance = axios.create({
  baseURL: `${apiUrl}/api/wayback`,
});

const getSnapshotsByUrl = async (url) => {
  try {
    const regex = /\/[^/]+$/;
    const normalUrl = url
    const urlWithId = url.replace(regex, '') 

    const response =  axiosInstance.get(`/snapshot?url=${normalUrl}`);
    const responseWithId =  axiosInstance.get(`/snapshot?url=${urlWithId}`);
    const finalResponse =  await Promise.all([response,responseWithId]);
    console.log(finalResponse)
    return  await Promise.all([response,responseWithId]);
  } catch (error) {
    // Handle error here
    console.error('Error fetching snapshots:', error);
    throw error;
  }
};

const getRecordByArchive = async (archiveUrl, range, distance) => {
  try {
    const response = await axiosInstance.get(`/record?archiveUrl=${archiveUrl}&range=${range}&distance=${distance}`);
    return response.data;
  } catch (error) {
    // Handle error here
    console.error('Error fetching record by archive:', error);
    throw error;
  }
};

export { getSnapshotsByUrl, getRecordByArchive };
