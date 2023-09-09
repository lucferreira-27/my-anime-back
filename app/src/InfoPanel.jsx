import React, { useContext } from 'react';
import { Grid, Card, CardContent, Typography, Container, Box, Divider, Stack, Button, Paper } from '@mui/material';
import { styled } from '@mui/system';
import TimeArchive from './TimeArchive';
import AnimePanel from './AnimePanel'
import { Context } from "./App"
const PanelGrid = styled(Grid)(({ theme }) => ({
    backgroundColor: `#0d1117`,
    borderRadius: `5px`,
    padding: `10px`,
    marginTop: theme.spacing(3),
    maxWidth: '600px',
    [theme.breakpoints.up('md')]: {
        maxWidth: '80%',
        marginLeft: '10%',
    },
}));

const TitleDivider = styled(Divider)(({ theme }) => ({
    flexItem: true,
    color: 'white',
      '&::before, &::after': {
      borderColor: 'white',
    },
    marginBottom: '20px',
  }));

const AnimeImage = styled('img')(({ theme }) => ({
    width: '100%',
    maxWidth: '250px',
    height: 'auto',
    [theme.breakpoints.down('sm')]: {
        maxWidth: '100%',
        height: '250px',
        objectFit: 'cover',
        objectPosition: 'center',
        borderRadius: '0px',
    },
    borderRadius: '15px',
    borderColor: 'red',
    position: 'relative', // Required for overlay positioning
}));

function InfoPanel({ media }) {
    const {
        images,
        title,
    } = media;

    return (
        <Container>
            <PanelGrid container spacing={2}>
                <Grid container spacing={2}>
                    <Grid item xs={12} md={12}>
                        <TitleDivider
                            flexItem
                            sx={{
                                letterSpacing: {
                                    xs: '4px', // Adjust the font size for small screens (adjust as needed)
                                    sm: '8px',
                                },
                                fontSize: {
                                    xs: '24px', // Adjust the font size for small screens (adjust as needed)
                                    sm: '32px', // Font size for screens that match or are larger than 'sm' breakpoint
                                },
                            }}
                        >
                            {title}
                        </TitleDivider>
                    </Grid>
                </Grid>
                <Grid item xs={12} md={4}>
                    <AnimeImage src={images.jpg.large_image_url} alt={title} />
                </Grid>
                <Grid item xs={12} md={8}>
                    <AnimePanel media={media} />
                    <TimeArchive />
                </Grid>
            </PanelGrid>


        </Container>


    );
}

export default InfoPanel;
