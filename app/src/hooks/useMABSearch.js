import { useState } from 'react';
import { getSnapshotsByUrl } from '../service/mab';



const useMABSearch = () => {
    const [snapshotData, setSnapshotData] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const performSearch = async (url) => {

        try {
            setSnapshotData(null)
            setLoading(true);
            setError(null);
            const [{ data:data1 },{ data:data2 }] = await getSnapshotsByUrl(url);
            const data = [...data1,...data2].sort((a, b) =>  a.timestamp.date - b.timestamp.date)
            console.log(data1)
            console.log(data2)
            console.log(data)

            setSnapshotData(data);
        } catch (error) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    return { snapshotData ,setSnapshotData, loading, setLoading, setError, error, performSearch };
};

export default useMABSearch;