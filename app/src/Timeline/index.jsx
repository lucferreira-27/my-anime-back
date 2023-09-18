import React, { useState, useContext, useEffect, useRef } from 'react';
import { Container, Divider, Paper, Button, Box, Grid } from '@mui/material';
import styled from '@mui/system/styled';
import { Context } from "../App";
import TimeDisplay from '../TimeDisplay';
import TimelineSlider from './TimelineSlider';
import MembersChart from './MembersChart'
import ConvertGif from '../ConvertGif'
import { CircularProgress } from '@mui/material';
import useGifGenerator from '../hooks/useGifGenerator';
const StyledContainer = styled(Container)`
    margin-top: 100px;
    display: flex;
    flex-direction: column;
    align-items: center;
    height: 100vh;
`;

const StyledDivider = styled(Divider)`
    color: white;
    &::before, &::after {
        border-color: white;
    }
    letter-spacing: 8px;
    font-size: 24px;

    @media (min-width: 600px) {
        font-size: 32px;
    }
`;

const StyledBox = styled(Box)`
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
`;

const StyledPaper = styled(Paper)`
    margin-top: 50px;
    color: #fff;
    min-width: 275px;
    background-color: #1f2938;
    width: 100%;
    padding: 16px;
    border-radius: 2px;
    box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
`;

const Timeline = () => {
    const { resources, media } = useContext(Context);
    const [timeMedia, setTimeMedia] = useState({ ...media });
    const timeDisplayRef = useRef(null);

    const convertToCSV = (data) => {
        const replacer = (key, value) => value === null ? '' : value;
        const header = Object.keys(data[0]);
        const csv = [
            header.join(','),
            ...data.map(row => header.map(fieldName => JSON.stringify(row[fieldName], replacer)).join(','))
        ].join('\r\n');
        return csv;
    };

    const downloadData = () => {
        const csvData = convertToCSV(resources);
        const blob = new Blob([csvData], { type: 'text/csv' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = url;
        a.download = 'resources.csv';
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
    };

    return (
        <StyledContainer>
            <StyledDivider flexItem>
                Timeline
            </StyledDivider>
            <StyledBox>
                <TimelineSlider resources={resources} timeMedia={timeMedia} setTimeMedia={setTimeMedia} />
                <StyledPaper>
                    <Grid container spacing={3}>
                        <Grid item xs={12} md={6}>
                            <Box>
                                <TimeDisplay ref={timeDisplayRef} timeMedia={timeMedia} />
                                <ConvertGif timeDisplayRef={timeDisplayRef} resources={resources} setTimeMedia={setTimeMedia} timeMedia={timeMedia} />
                            </Box>
                        </Grid>
                        <Grid item xs={12} md={6}>
                            <Box>
                                <MembersChart resources={resources} />
                                <Button sx={{
                                    margin: `20px`
                                }} variant={'contained'} onClick={() => downloadData()}>Download</Button>
                            </Box>
                        </Grid>
                    </Grid>
                </StyledPaper>
            </StyledBox>
        </StyledContainer>
    );
}

export default Timeline;
