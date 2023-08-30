import { useState } from 'react';
import {jikanSearch} from '../service/search';



const useJikanSearch = () => {
    const [searchData, setSearchData] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
  
    const performSearch = async (url) => {
      try {
        setLoading(true);
        setError(null);
        const {data} = await jikanSearch(url);
        console.log(data)
        setSearchData(data);
      } catch (error) {
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };
  
    return { searchData,setSearchData, loading,setLoading,setError,error, performSearch };
  };
  
  export default useJikanSearch;