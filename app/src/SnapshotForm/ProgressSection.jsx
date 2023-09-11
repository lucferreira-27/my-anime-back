import React from 'react';
import { Box, LinearProgress, Typography } from '@mui/material';
import { useSpring, animated } from 'react-spring'; // Import the necessary components from react-spring
import { styled } from '@mui/system';


const ResourceProgressNumber = styled(Typography)(({ theme }) => ({
    color: `white`,
    margin: `10px 0px 0px 0px`,
    padding: `3px 6px`,
    borderRadius: `5px`,
    backgroundColor: `#1c2025`
}));


const AnimatedResourceProgressNumber = animated(ResourceProgressNumber)

const ProgressSection = ({ progress, error, resources, splitSnapshots }) => {

    const resourceProgressSpring = useSpring({
        to: { resources: resources.length, range: splitSnapshots.length },
        from: { resources: 0, range: splitSnapshots.length }, // You can change the initial value
    });

    return (
        <Box sx={{ mt: "5px", display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <LinearProgress
                variant={error ? 'indeterminate' : "determinate"}
                value={progress}
                sx={{
                    width: '100%',
                    '& .MuiLinearProgress-bar': {
                        backgroundColor: error ? '#d32f2f' : `#0288d1`
                    }
                }}
            />
            <AnimatedResourceProgressNumber variant="caption">
                {error ? `Error` : (
                    resourceProgressSpring.resources.to((value) => `${Math.floor(value)}/${splitSnapshots.length}`)
                )}
            </AnimatedResourceProgressNumber>
        </Box>
    );
};

export default ProgressSection;
