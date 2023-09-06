import { useState, useEffect } from 'react';
import { getRecordByArchive } from '../service/mab';



const useResourceArchive = (snapshots) => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [currentResourceData, setCurrentResourceData] = useState(null);
    const performSearch = async (snapshots) => {
        setLoading(true);
        setError(null);

        try {
            console.log(snapshots)
            const data = await getRecordByArchive(snapshots.map(s => s.url));
            setCurrentResourceData(data);
            return data
        } catch (error) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };


    const getNextSnapshot = async (snapshots) => {
        const data = await performSearch(snapshots);
        return data

    };

    return {
        loading,
        error,
        getNextSnapshot,
    };
};

export default useResourceArchive;