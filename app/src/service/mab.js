import axios from 'axios';
import { apiUrl as devApiUrl } from '../config/config.dev';
import { apiUrl as prodApiUrl } from '../config/config.prod';
import { GIFEncoder } from 'jsgif'; // Assuming this is the correct import path for jsgif

// Determine the environment
const environment = `dev`;

// Import the appropriate API URL based on the environment
const apiUrl = environment === 'production' ? prodApiUrl : devApiUrl;

// Create an Axios instance with the base URL
const axiosInstance = axios.create({
  baseURL: `${apiUrl}/api`,
});


const getSnapshotsByUrl = async (url) => {
  try {
    const regex = /\/[^/]+$/;
    const normalUrl = url
    const urlWithId = url.replace(regex, '')

    const response = axiosInstance.get(`/wayback/snapshot?url=${normalUrl}`);
    const responseWithId = axiosInstance.get(`/wayback/snapshot?url=${urlWithId}`);
    const finalResponse = await Promise.all([response, responseWithId]);
    return await Promise.all([response, responseWithId]);
  } catch (error) {
    // Handle error here
    console.error('Error fetching snapshots:', error);
    throw error;
  }
};

const getRecordByArchive = async (archiveUrls) => {
  try {
    const response = await axiosInstance.post('/wayback/record/media', archiveUrls);
    return response.data;
  } catch (error) {
    // Handle error here
    console.error('Error fetching record by archive:', error);
    throw error;
  }
};

const convertToGif = async (dataUrls) => {
  try {
    const response = await axiosInstance.post('/gif/convert', dataUrls, {
      headers: {
        'Content-Type': 'application/json'
      },
      responseType: 'blob'
    });

    return response.data;
  } catch (error) {
    // Handle error here
    console.error('Error converting to gif:', error);
    throw error;
  }
};

const convertToGif2 = async (dataUrls) => {
  console.log(dataUrls.length)
  const encoder = new GIFEncoder();
  console.log(dataUrls.length)

  // Start the encoder
  encoder.start();

  // Assuming all frames should have the same delay and repeat infinitely
  encoder.setRepeat(0);
  encoder.setDelay(500); // for example, 500ms delay between frames

  // Process each data URL
  const processFrame = (dataUrl) => {
    return new Promise((resolve, reject) => {
      const img = new Image();
      img.onload = () => {
        encoder.addFrame(img);
        resolve();
      };
      img.onerror = reject;
      img.src = dataUrl;
    });
  };

  // Convert all dataUrls to frames
  for (let dataUrl of dataUrls) {
    await processFrame(dataUrl);
  }

  // Finish encoding
  encoder.finish();

  // Return the generated GIF as a data URL
  const gifBinary = encoder.stream().getData();
  const gifDataUrl = `data:image/gif;base64,${btoa(gifBinary)}`;
  console.log(gifDataUrl)
  return gifDataUrl;
};



export { getSnapshotsByUrl, getRecordByArchive, convertToGif,convertToGif2 };
