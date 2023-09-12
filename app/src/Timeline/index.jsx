import React, { useState, useContext } from 'react';
import { VictoryChart, VictoryLine, VictoryAxis, VictoryZoomContainer } from 'victory';
import { Container, Divider, Paper, Box,Grid } from '@mui/material';
import styled from '@mui/system/styled';
import { Context } from "../App";
import TimeDisplay from '../TimeDisplay';
import TimelineSlider from './TimelineSlider';
import MembersChart from './MembersChart'
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
                            <TimeDisplay timeMedia={timeMedia} />
                        </Grid>
                        <Grid item xs={12} md={6}>
                            <MembersChart resources={resources}/>
                        </Grid>
                    </Grid>
                </StyledPaper>
            </StyledBox>
        </StyledContainer>
    );
}

export default Timeline;
