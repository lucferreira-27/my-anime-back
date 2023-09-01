import React from 'react';
import { Grid, Card, CardContent, Typography, Container, Box, Divider, Stack } from '@mui/material';
import { styled } from '@mui/system';
import HighlightOffIcon from '@mui/icons-material/HighlightOff';

const PanelGrid = styled(Grid)(({ theme }) => ({
    backgroundColor: `#0d1117`,
    borderRadius: `5px`,
    padding: `10px`,
    marginTop: theme.spacing(3), // Increase margin top
    maxWidth: "600px",
    [theme.breakpoints.up('md')]: {
        maxWidth: "80%",
        marginLeft: "10%",
    },
}));


const AnimeImage = styled('img')(({ theme }) => ({
    width: `100%`,
    maxWidth: `250px`,
    height: `auto`,
    [theme.breakpoints.down('sm')]: {
        maxWidth: `200px`,
    },
    borderRadius: `15px`,
    borderColor: `red`
}));

const InfoValue = styled('span')(({ theme }) => ({
    fontWeight: `bold`,
    fontSize: `16px`
}));
const AnimeCard = styled(Card)(({ theme }) => ({
    color: `#fff`,
    display: 'flex',
    backgroundColor: `#24292f`,
    flexDirection: 'column',
    justifyContent: 'flex-start',
    alignItems: 'flex-start',
    maxWidth: "550px",
    [theme.breakpoints.down('sm')]: {
        maxWidth: "100%", // Adjust width for medium and larger screens
    },
}));

const AnimeCardContent = styled(CardContent)(({ theme }) => ({
    display: 'flex',
    flexDirection: 'row',
}));

const AnimeInfo = styled(Typography)(({ theme }) => ({
    marginTop: 'auto',
}));

const Title = styled(Typography)(({ theme }) => ({
    fontSize: '1.5rem',
    marginBottom: theme.spacing(2), // Add margin below title
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
        year
    } = result;

    return (
        <Container>
            <PanelGrid container spacing={2}>
                <Grid container spacing={2}>
                    <Grid item xs={12} md={12}>
                        <Divider flexItem sx={{
                            color: `white`,
                            "&::before, &::after": {
                                borderColor: "white",
                            },
                        }}>
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

                            <Box sx={{ margin: `auto`, justifyContent: `center`, textAlign: `center` }}>
                                <AnimeInfo variant="body1">
                                    <Box sx={
                                        {
                                            backgroundColor: `#2e51a2`,
                                            fontSize: `10px`,
                                            fontWeight: `bold`,
                                            padding: `1px 5px 1px 5px`,
                                            borderRadius: `2px`,
                                            margin: `auto`,
                                            maxWidth: `50%`
                                        }
                                    }>
                                        SCORE
                                    </Box>
                                </AnimeInfo>
                                <AnimeInfo variant="body1" sx={{
                                    fontWeight: `bold`,
                                    fontSize: `24px`,

                                }}>{score.toFixed(2)}</AnimeInfo>
                                <AnimeInfo variant="body1" sx={{
                                    fontSize: `12px`,
                                }}>{scored_by.toLocaleString()} users</AnimeInfo>
                            </Box>
                            <Divider flexItem orientation='vertical' sx={
                                {
                                    mx: 2,
                                    backgroundColor: `gray`
                                }
                            } />
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
                                    <AnimeInfo variant="body1"sx={{
                                        fontSize: `12px`
                                    }}>{`${season.replace(/\b\w/g, (match) => match.toUpperCase())} ${year}`}</AnimeInfo>
                                    <Divider flexItem orientation='vertical' sx={
                                        {
                                            backgroundColor: `gray`
                                        }
                                    } />
                                    <AnimeInfo variant="body1"sx={{
                                        fontSize: `12px`
                                    }}>{type}</AnimeInfo>
                                    <Divider flexItem orientation='vertical' sx={
                                        {
                                            backgroundColor: `gray`
                                        }
                                    } />
                                    <AnimeInfo variant="body1" sx={{
                                        fontSize: `12px`
                                    }}>{studios[0]?.name}</AnimeInfo>
                                </Stack>
                            </Box>

                            {/* You can add more info here */}
                        </AnimeCardContent>
                    </AnimeCard>
                </Grid>
            </PanelGrid>


        </Container>


    );
}

export default InfoPanel;
