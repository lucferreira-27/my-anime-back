import React, { useState, useEffect } from 'react';
import { LinearProgress, Grid, Card, CardContent, Typography, Container, Box, Divider, Stack, Button, Paper, TextField, FormControl, InputLabel, Select, MenuItem } from '@mui/material';
import { styled } from '@mui/system';
import useMABSearch from './hooks/useMABSearch'; // Import the custom hook
import SnapshotForm from './SnapshotForm';

const MABCard = styled(`div`)(({ theme }) => ({
    maxWidth: '550px',
    backgroundColor: `transparent`,
    [theme.breakpoints.down('sm')]: {
        maxWidth: '100%',
    },
    marginTop: `3vh`
}));

export default function FormMAB({ url }) {
    const { snapshotData, loading, error, performSearch } = useMABSearch();
    const [progress, setProgress] = useState(0);
    const [working, setWorking] = useState(false)
    const [next, setNext] = useState(false)
    const [formData, setFormData] = useState({
        startDate: '',
        endDate: '',
        distanceType: 'days',
        distance: '',
    });

    useEffect(() => {
        let timer;

        const updateProgress = () => {
            setProgress((prevProgress) => {
                let increaseProgress = 0
                if (prevProgress < 100) {
                    const slowdownFactor = 0.60 + Math.random() * 0.10;
                    const maxIncrement = 2.5 + Math.random() * 5;
                    if (prevProgress < 30) {
                        // Progress at max speed when below 30%
                        increaseProgress = prevProgress + maxIncrement;
                    } else {
                        // Gradually slow down after reaching 30%
                        const increment = maxIncrement * slowdownFactor ** ((prevProgress - 30) / 10);
                        increaseProgress = prevProgress + increment;
                    }
                }
                if (increaseProgress > 95) {
                    return prevProgress * 0.01
                }
                return increaseProgress
            });
        };


        if (loading) {
            // Reset the progress bar when loading starts
            setProgress(0);
            timer = setInterval(updateProgress, 600); // Increase progress every 600ms
        } else {
            setProgress(100)
            clearInterval(timer); // Stop the progress when loading is complete
        }

        return () => {
            clearInterval(timer); // Cleanup when the component unmounts
        };
    }, [loading]);

    const handleSearch = () => {
        setWorking(true)
        console.log(`test`);
        performSearch(url);
    };

    const handleData = () => {
        setNext(true)
    }

    useEffect(() => {
        console.log(snapshotData);
    }, [snapshotData]);

    return (
        <MABCard>
            <Divider
                flexItem
                sx={{
                    color: `white`,
                    "&::before, &::after": {
                        borderColor: "white",
                    },
                    fontSize: {
                        xs: '18px', // Adjust the font size for small screens (adjust as needed)
                        sm: '24px', // Font size for screens that match or are larger than 'sm' breakpoint
                    },
                }}
            >
                Time Archive
            </Divider>
            <Box
                sx={{
                    mt: "20px",
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                }}
            >
                {next ? (
                    <SnapshotForm formData={formData} setFormData={setFormData} handleData={handleData} />
                ) : (
                    <>
                        {(loading || working) && (
                            <>
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
                                    {error ? `Request Timeout` : (
                                        !snapshotData ? `${progress.toFixed(0)}%` : `${snapshotData.length} snapshots found`
                                    )}
                                </Typography>
                            </>
                        )}

                        {!working && (
                            <Button variant="contained" onClick={() => handleSearch()}>START</Button>
                        )}

                        {(!next && (snapshotData || error)) && (
                            <Button variant="contained" onClick={() => error ? handleSearch() : handleData()} sx={{ mt: 2 }}>
                                {!error ? `Next` : `Try Again`}
                            </Button>
                        )}
                    </>
                )}
            </Box>
        </MABCard>
    );
}
