import React from 'react';
import { styled } from '@mui/system';
import AnimePanel from './AnimePanel';
import { Box, Typography } from '@mui/material';

const CenteredBox = styled(Box)(({ theme }) => ({
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center', // Center vertically and horizontally
}));

const Background = styled("div")(({ theme, timeMedia }) => ({
    position: `relative`,
    width: "80%",
    height: "350px",
    backgroundImage: `url(${timeMedia?.images.jpg.large_image_url})`, // Set background image to AnimeImage
    backgroundPosition: "center",
    backgroundSize: "cover",
    backgroundRepeat: "no-repeat",
}));

export default function TimeDisplay({ timeMedia }) {
    return (
        <CenteredBox>
            <Background timeMedia={timeMedia}>
                <CenteredBox sx={{
                    width: `100%`,
                    position: `absolute`,
                    bottom: `0px`,
                }}>
                    <Typography variant={`h4`} sx={{
                        padding: `5px`,
                        width: `98%`,
                        textAlign: `center`,
                        margin: `5px`,
                        backgroundColor: `#0288d1`
                    }}>{timeMedia.title}</Typography>
                    <AnimePanel sx={{
                        mb: `10px`
                    }} media={timeMedia} copyMal={true} />
                </CenteredBox>
            </Background>

        </CenteredBox>
    );
}
