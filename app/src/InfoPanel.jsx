import React from 'react';
import { Grid, Card, CardContent, Typography, Container, Box, Divider, Stack, Button, Paper } from '@mui/material';
import { styled } from '@mui/system';
import FormMAB from './FormMAB';
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

const AnimeCard = styled(Card)(({ theme }) => ({
    color: `#fff`,
    display: 'flex',
    backgroundColor: `#24292f`,
    flexDirection: 'column',
    justifyContent: 'flex-start',
    alignItems: 'flex-start',
    maxWidth: '550px',
    [theme.breakpoints.down('sm')]: {
        maxWidth: '100%',
    },
}));

const AnimeCardContent = styled(CardContent)(({ theme }) => ({
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'space-between',
}));

const AnimeInfo = styled(Typography)(({ theme }) => ({
    marginTop: 'auto',
    fontSize: '16px',
}));

const ScoreContainer = styled(Box)(({ theme }) => ({
    margin: `auto`,
    justifyContent: `center`,
    textAlign: `center`,
}));

const ScoreLabel = styled(Box)(({ theme }) => ({
    backgroundColor: `#2e51a2`,
    fontSize: `10px`,
    fontWeight: `bold`,
    padding: `1px 5px 1px 5px`,
    borderRadius: `2px`,
    margin: `auto`,
    maxWidth: `50%`,
}));

const ScoreValue = styled(AnimeInfo)(({ theme }) => ({
    fontWeight: `bold`,
    fontSize: `24px`,
}));

const ScoreUserCount = styled(AnimeInfo)(({ theme }) => ({
    fontSize: `12px`,
}));


const InfoValue = styled('span')(({ theme }) => ({
    fontWeight: `bold`,
    fontSize: `16px`
}));

const ExtraAnimeInfo = styled(AnimeInfo)(({ theme }) => ({
    fontSize: '12px',
    color: `#AEAEAE`,
}));

const MABCard = styled(`div`)(({ theme }) => ({
    maxWidth: '550px',
    backgroundColor: `transparent`,
    [theme.breakpoints.down('sm')]: {
        maxWidth: '100%',
    },
    marginTop: `3vh`
}));


function InfoPanel({ result }) {
    const {
        images,
        title,
        score,
        rank,
        popularity,
        members,
        scored_by,
        studios,
        type,
        season,
        year,
        url
    } = result;

    return (
        <Container>
            <PanelGrid container spacing={2}>
                <Grid container spacing={2}>
                    <Grid item xs={12} md={12}>
                        <Divider
                            flexItem
                            sx={{
                                color: `white`,
                                "&::before, &::after": {
                                    borderColor: "white",
                                },
                                mb: `20px`,
                                fontSize: {
                                    xs: '24px', // Adjust the font size for small screens (adjust as needed)
                                    sm: '32px', // Font size for screens that match or are larger than 'sm' breakpoint
                                },
                            }}
                        >
                            {title}
                        </Divider>
                    </Grid>
                </Grid>
                <Grid item xs={12} md={4}>

                        <AnimeImage src={images.jpg.large_image_url} alt={title} />
                </Grid>
                <Grid item xs={12} md={8}>
                    <AnimeCard>
                        <AnimeCardContent>
                            <ScoreContainer>
                                <ScoreLabel>SCORE</ScoreLabel>
                                <ScoreValue>{score.toFixed(2)}</ScoreValue>
                                <ScoreUserCount>{scored_by.toLocaleString()} users</ScoreUserCount>
                            </ScoreContainer>
                            <Divider flexItem orientation='vertical' sx={{ mx: 2, backgroundColor: `gray` }} />
                            <Box>
                                <Stack direction="row" spacing={2}>
                                    <AnimeInfo variant="body1">Ranked
                                        <InfoValue>
                                            {` #${rank}`}
                                        </InfoValue>
                                    </AnimeInfo>
                                    <AnimeInfo variant="body1">Popularity
                                        <InfoValue>
                                            {` #${popularity}`}
                                        </InfoValue>
                                    </AnimeInfo>
                                    <AnimeInfo variant="body1">Members
                                        <InfoValue>
                                            {` ${members.toLocaleString()}`}
                                        </InfoValue>
                                    </AnimeInfo>
                                </Stack>
                                <Stack sx={{
                                    mt: `25px`
                                }} direction="row" spacing={2}>
                                    <ExtraAnimeInfo variant="body1">{`${season.replace(/\b\w/g, (match) => match.toUpperCase())} ${year}`}</ExtraAnimeInfo>
                                    <Divider flexItem orientation='vertical' sx={{ backgroundColor: `gray` }} />
                                    <ExtraAnimeInfo variant="body1">{type}</ExtraAnimeInfo>
                                    <Divider flexItem orientation='vertical' sx={{ backgroundColor: `gray` }} />
                                    <ExtraAnimeInfo variant="body1">{studios[0]?.name}</ExtraAnimeInfo>
                                </Stack>
                            </Box>
                        </AnimeCardContent>
                    </AnimeCard>
                    <FormMAB result={result}/>
                </Grid>
            </PanelGrid>


        </Container>


    );
}

export default InfoPanel;
