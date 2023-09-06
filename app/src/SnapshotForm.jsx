import React, { useEffect, useState } from 'react';
import { Button, Typography, Divider, LinearProgress, Box, FormControl, InputLabel, Select, MenuItem, Grid, TextField } from '@mui/material';
import { styled } from '@mui/system';
import useSplitSnapshots from './hooks/useSplitSnapshots'; // Import the custom hook
import useResourceArchive from './hooks/useResourceArchive'; // Import the custom hook
import { set } from 'date-fns/esm';

const FormContainer = styled('form')`
  display: grid;
  gap: 16px;
  background-color: #24292f;
  padding: 20px;
  border-radius: 5px
`;

const StyledFormControl = styled(FormControl)`
  && {
    width: 100%;
  }
  
  & label.MuiFormLabel-root {
    color: white;
  }

  & .MuiOutlinedInput-root {
    &.Mui-focused fieldset {
      border-color: white;
    }
    color: #AEAEAE;
    border-color: #0288d1;

    &:hover fieldset {
      border-color: #ffffff; /* Change the hover border color */
    }
  }
`;

const StyledTextField = styled(TextField)`
  & label.MuiFormLabel-root {
    color: white;
  }

  & .MuiOutlinedInput-root {
    &.Mui-focused fieldset {
      border-color: white;
    }
    color: #AEAEAE;
    border-color: #0288d1;

    &:hover fieldset {
      border-color: #ffffff; /* Change the hover border color */
    }

    & .MuiSvgIcon-root { // Style for the icon
      fill: white; /* Change the icon color */
    }
  }
`;

export default function SnapshotForm({ formData, setFormData, handleRangeSnapshots, snapshotData }) {
  const splitSnapshots = useSplitSnapshots(snapshotData, formData);
  const [working, setWorking] = useState(false)
  const [resourceArchiveData, setResourceArchiveData] = useState([])
  const [progress, setProgress] = useState(0)
  const {
    loading,
    error,
    getNextSnapshot,
  } = useResourceArchive(splitSnapshots)

  const createTimeline = async () => {
    const batchSize = 8; // Number of URLs to send in each batch
    const totalBatches = Math.ceil(splitSnapshots.length / batchSize);

    const loadSnapshots = async () => {
      console.log('loadSnapshots');
      setWorking(true);
      setResourceArchiveData([]);

      for (let batchIndex = 0; batchIndex < totalBatches; batchIndex++) {
        const start = batchIndex * batchSize;
        const end = start + batchSize;
        const batchUrls = splitSnapshots.slice(start, end);

        const batchPromises = [getNextSnapshot(batchUrls)]; // Pass the batchUrls to getNextSnapshot

        const batchResults = await Promise.all(batchPromises);

        // Filter out null results (failed requests)
        const validResults = batchResults.flat().filter((result) => result !== null);

        // Update state with valid results
        setResourceArchiveData((prevData) => [...prevData, ...validResults]);

        setProgress(((batchIndex + 1) / totalBatches) * 100);
      }

      setWorking(false);
    };

    // Start loading snapshots in batches
    loadSnapshots();
  };


  const viewTimeLine = () => {
    console.log(`view timeline`)
  }

  useEffect(() => {
    // This effect will run whenever resourceArchiveData changes.
    // You can perform any additional actions here when data updates.
    console.log('resourceArchiveData updated:', resourceArchiveData);
  }, [resourceArchiveData]);

  return (
    <>
      <FormContainer>
        <Grid container spacing={2}>
          <Grid item xs={6}>
            <StyledFormControl>
              <StyledTextField
                InputLabelProps={{ shrink: true }}
                id="startDate"
                label="Start Date"
                type="date"
                variant="outlined"
                onChange={(e) => setFormData({ ...formData, startDate: e.target.value })}
                value={formData.startDate}
                required
              />
            </StyledFormControl>
          </Grid>
          <Grid item xs={6}>
            <StyledFormControl>
              <StyledTextField
                InputLabelProps={{ shrink: true }}
                label="End Date"
                id="endDate"
                type="date"
                variant="outlined"
                onChange={(e) => setFormData({ ...formData, endDate: e.target.value })}
                value={formData.endDate}
                required
              />
            </StyledFormControl>
          </Grid>
          <Grid item xs={6}>
            <StyledFormControl>
              <StyledTextField
                label="Distance"
                id="distance"
                type="number"
                variant="outlined"
                onChange={(e) => setFormData({ ...formData, distance: e.target.value })}
                value={formData.distance}
                required
              />
            </StyledFormControl>
          </Grid>
          <Grid item xs={6}>
            <StyledFormControl>
              <InputLabel htmlFor="distanceType">Distance Type</InputLabel>
              <Select
                label="Distance Type"
                id="distanceType"
                variant="outlined"
                onChange={(e) => setFormData({ ...formData, distanceType: e.target.value })}
                value={formData.distanceType}
                required
              >
                <MenuItem value="days">Days</MenuItem>
                <MenuItem value="months">Months</MenuItem>
                <MenuItem value="years">Years</MenuItem>
              </Select>
            </StyledFormControl>
          </Grid>
          <Grid item xs={12} style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
            <Box>
              <Typography
                variant="caption"
                sx={{
                  color: 'white',
                  padding: '3px 6px',
                  borderRadius: '5px',
                  backgroundColor: '#1c2025',
                  alignSelf: 'center',
                }}
              >
                {`It's projected that ${splitSnapshots?.length || 0} snapshots will be part of this timeline of ${snapshotData?.length}.`}
              </Typography>
            </Box>
          </Grid>
          <Grid item xs={12}>
            {working ? (
              <Box
                sx={{
                  mt: "5px",
                  display: 'flex',
                  flexDirection: 'column',
                  alignItems: 'center',
                }}>
                <LinearProgress
                  variant={error ? 'indeterminate' : "determinate"}
                  value={progress}
                  sx={{
                    width: '100%', '& .MuiLinearProgress-bar': {
                      backgroundColor: error ? '#d32f2f' : `#0288d1`
                    }
                  }}
                />
                <Typography
                  variant="caption"
                  sx={{
                    color: `white`,
                    mt: 1,
                    padding: `3px 6px`,
                    borderRadius: `5px`,
                    backgroundColor: `#1c2025`
                  }}
                >
                  {error ? `Error` : (
                    `${resourceArchiveData.length}/${splitSnapshots.length}`
                  )}
                </Typography>
              </Box>
            ) : (
              <Button variant="contained" sx={{ width: '100%' }} onClick={() => !working && resourceArchiveData
                ? createTimeline() : viewTimeLine()}>
                {!working && resourceArchiveData.length > 0 ? `VIEW TIMELINE` : `CREATE TIMELINE`}
              </Button>)
            }
          </Grid>

        </Grid>
      </FormContainer>
    </>

  );
}
