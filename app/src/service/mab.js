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
    const response = await axiosInstance.get(`/snapshot?url=${url}`);
    return response;
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
