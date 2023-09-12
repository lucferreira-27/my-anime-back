import React from 'react';
import { styled } from '@mui/system';
import AnimePanel from './AnimePanel';
import { Box, Typography } from '@mui/material';


const CenteredBox = styled(Box)(({ theme }) => ({
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center', // Change 'top' to 'center'
    justifyContent: 'center',
}));

const DisplayBox = styled(Box)(({ theme }) => ({
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center', // Center vertically and horizontally
    position: 'relative', // Add relative positioning
    zIndex: 1, // Set a higher z-index value
}));

const InsideDisplayBox = styled(Box)(({ theme }) => ({
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center', // Center vertically and horizontally
    position: 'relative', // Add relative positioning
    zIndex: 1, // Set a higher z-index value
    width: `100%`,
    height: `100%`,
    backgroundColor: 'rgba(0, 0, 0, 0.5)', // Add a semi-transparent background
}));


const Background = styled("div")(({ theme }) => ({
    border: "1px solid gray",
    position: 'relative',
    width: '600px',
    height: '337.5px',
    overflow: 'hidden', // Hide overflow to prevent blur from affecting child elements
}));

const BackgroundImage = styled("div")(({ theme, timeMedia }) => ({

    position: 'absolute',
    top: 0,
    left: 0,
    width: '100%',
    height: '100%',
    backgroundImage: `url(${timeMedia?.images.jpg.large_image_url})`, // Set background image to AnimeImage
    backgroundPosition: 'center',
    backgroundSize: 'cover',
    backgroundRepeat: 'no-repeat',
    filter: 'blur(2.5px)', // Apply a blur filter to the background image
}));

const OutsideBox = styled(Box)(({ theme }) => ({
    [theme.breakpoints.down('md')]: { // Apply scale on screens less than or equal to 'sm' breakpoint
        alignItems: 'center',
        justifyContent: 'center', // Center vertically and horizontally
        transform: 'scale(0.85)', // Scale down by 20%
    },
}));


export default function TimeDisplay({ timeMedia }) {
    const formatDate = (originalDate) => {
        return new Date(originalDate).toLocaleDateString("en-US", {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
        });
    }
    return (
        <OutsideBox sx={{
            display: 'flex',
            flexDirection: 'row',
            alignItems: 'left',
            justifyContent: 'left', // Center vertically and horizontally
        }}>

            <DisplayBox>
                <Background>
                    <BackgroundImage timeMedia={timeMedia}></BackgroundImage>
                    <InsideDisplayBox>
                        <CenteredBox>
                            <Typography variant={`h4`} sx={{
                                fontSize: `24px`,
                                width: `100%`,
                                mb: `30%`,
                                textAlign: `center`,
                                backgroundColor: `rgba(2, 136, 209, 0.5)`,
                                color: '#fff', // Set text color to white
                            }}>{`${timeMedia.title} - ${formatDate(timeMedia.archiveDate)}`}</Typography>
                            <AnimePanel sx={{
                            }} media={timeMedia} copyMal={true} />
                        </CenteredBox>
                    </InsideDisplayBox>
                </Background>
            </DisplayBox>

        </OutsideBox>

    );
}
