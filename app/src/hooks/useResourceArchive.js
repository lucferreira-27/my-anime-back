import { useState, useEffect } from 'react';
import { getRecordByArchive } from '../service/mab';



const useResourceArchive = (snapshots) => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [currentResourceData, setCurrentResourceData] = useState(null);
    const performSearch = async (malUrl,snapshots) => {
        setLoading(true);
        setError(null);

        try {
            console.log(snapshots)
            const archiveUrls = snapshots.map(s => s.url)
            console.log("archiveUrls",archiveUrls)
            const data = await getRecordByArchive(malUrl,archiveUrls);
            setCurrentResourceData(data);
            return data
        } catch (error) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };


    const getNextSnapshot = async (url,snapshots) => {
        const data = await performSearch(url,snapshots);
        return data

    };

    return {
        loading,
        error,
        getNextSnapshot,
    };
};

export default useResourceArchive;