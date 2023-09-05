import { useEffect, useState } from 'react';
import getSplitSnapshots from "../service/range"
// Define your getSplitSnapshots function here (as shown in a previous response).

const useSplitSnapshots = (snapshots, config) => {
  const [splitSnapshots, setSplitSnapshots] = useState([]);

  useEffect(() => {
    // Function to update splitSnapshots based on the config
    const updateSplitSnapshots = () => {
      const filteredSnapshots = getSplitSnapshots(snapshots, config);
      setSplitSnapshots(filteredSnapshots);
    };

    // Call the update function initially
    updateSplitSnapshots();
  }, [snapshots, config]); // Re-run the effect when snapshots or config changes

  return splitSnapshots;
};

export default useSplitSnapshots;