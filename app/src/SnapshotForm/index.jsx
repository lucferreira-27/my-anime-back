import React, { useEffect, useState, useContext } from 'react';
import { Button, Typography, Divider, LinearProgress, Box, FormControl, InputLabel, Select, MenuItem, Grid, TextField } from '@mui/material';
import { styled } from '@mui/system';
import { useSpring, animated } from 'react-spring'; // Import the necessary components from react-spring
import useSplitSnapshots from '../hooks/useSplitSnapshots'; // Import the custom hook
import useResourceArchive from '../hooks/useResourceArchive'; // Import the custom hook
import { Context } from '../App';
import DateInputs from './DateInputs';
import DistanceInputs from './DistanceInputs'
import ProgressSection from './ProgressSection'
import SubmitButton from './SubmitButton'

const FormContainer = styled('form')`
  display: grid;
  gap: 16px;
  background-color: #24292f;
  padding: 20px;
  border-radius: 5px
`;

const ResourceProgressNumber = styled(Typography)(({ theme }) => ({
  color: `white`,
  margin: `10px 0px 0px 0px`,
  padding: `3px 6px`,
  borderRadius: `5px`,
  backgroundColor: `#1c2025`
}));


const AnimatedResourceProgressNumber = animated(ResourceProgressNumber)



export default function SnapshotForm({ formData, setFormData, snapshotData }) {
  const splitSnapshots = useSplitSnapshots(snapshotData, formData);
  const { resources, setResources, setShowTimeline } = useContext(Context)

  const [working, setWorking] = useState(false)
  const [progress, setProgress] = useState(0)
  const {
    loading,
    error,
    getNextSnapshot,
  } = useResourceArchive(splitSnapshots)

  const resourceProgressSpring = useSpring({
    to: { resources: resources.length, range: splitSnapshots.length },
    from: { score: resources.length, range: splitSnapshots.length }, // You can change the initial value
  });

  const createTimeline = async () => {
    const batchSize = 8; // Number of URLs to send in each batch
    const totalBatches = Math.ceil(splitSnapshots.length / batchSize);

    const loadSnapshots = async () => {
      console.log('loadSnapshots');
      setWorking(true);
      setResources([]);

      for (let batchIndex = 0; batchIndex < totalBatches; batchIndex++) {
        const start = batchIndex * batchSize;
        const end = start + batchSize;
        const batchUrls = splitSnapshots.slice(start, end);

        const batchPromises = [getNextSnapshot(batchUrls)]; // Pass the batchUrls to getNextSnapshot

        const batchResults = await Promise.all(batchPromises);

        const validResults = batchResults.flat().filter((result) => result !== null);

        setResources((prevData) => [...prevData, ...validResults]);

        setProgress(((batchIndex + 1) / totalBatches) * 100);
      }
      setTimeout(() => setWorking(false), 1000)

    };
    loadSnapshots();
  };


  const viewTimeLine = () => {
    setResources(resources.sort((a, b) => new Date(a.archiveDate).getTime() - new Date(b.archiveDate).getTime()))
    setShowTimeline(true)
  }

  return (
    <>
      <FormContainer>
        <Grid container spacing={2}>
          <DateInputs formData={formData} setFormData={setFormData} />
          <DistanceInputs formData={formData} setFormData={setFormData} />

          <Grid item xs={12} style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
            <Box>
              <AnimatedResourceProgressNumber
                variant="caption"
                sx={{
                  color: 'white',
                  padding: '3px 6px',
                  borderRadius: '5px',
                  backgroundColor: '#1c2025',
                  alignSelf: 'center',
                }}
              >
                {resourceProgressSpring.range.to((value) => `It's projected that ${Math.floor(value)} snapshots will be part of this timeline of ${snapshotData?.length}.`)}
              </AnimatedResourceProgressNumber>
            </Box>
          </Grid>
          <Grid item xs={12}>
            {working ? (
              <ProgressSection working={working} resources={resources} progress={progress} error={error}  splitSnapshots={splitSnapshots} />
            ) : (
              <SubmitButton working={working} resources={resources} onClick={resources.length <= 0 ? createTimeline : viewTimeLine}  />
            )}
          </Grid>

        </Grid>
      </FormContainer>
    </>

  );
}
