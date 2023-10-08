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
           // const media = await getMediaByUrl(url)
            const snapshtots = await getSnapshotsByUrl(url)
            const sortedSnapshots=  snapshtots.sort((a, b) =>  a.timestamp.dateInMillis - b.timestamp.dateInMillis)
            setSnapshotData(sortedSnapshots);
        } catch (error) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    return { snapshotData ,setSnapshotData, loading, setLoading, setError, error, performSearch };
};

export default useMABSearch;