import { React, createContext, useContext } from 'react'
import { Stack, Divider, Box, Typography, Card, CardContent } from '@mui/material';
import { styled } from '@mui/system';
import { useSpring, animated } from 'react-spring'; // Import the necessary components from react-spring



// Create a context for copyMal
const CopyMalContext = createContext();

// Create a custom hook to access copyMal
function useCopyMal() {
    return useContext(CopyMalContext);
}

// Create a provider component to set the copyMal value
function CopyMalProvider({ children, copyMal }) {
    return (
        <CopyMalContext.Provider value={copyMal}>
            {children}
        </CopyMalContext.Provider>
    );
}



const AnimeCard = animated(styled(Card)(({ theme }) => ({

    '& .MuiTypography-body1': {
        fontFamily: useCopyMal() && `Avenir,lucida grande,tahoma,verdana,arial,sans-serif`,
    },
    color: useCopyMal() ? `#e0e0e0` : `#fff`,
    display: 'flex',
    backgroundColor: useCopyMal() ? `#181818` : `#24292f`,
    border: useCopyMal() && `#272727 1px solid`,
    flexDirection: 'column',
    justifyContent: 'flex-start',
    alignItems: 'flex-start',
    width: useCopyMal() && '550px',
    maxWidth: '550px',
    [theme.breakpoints.down('sm')]: {
        maxWidth: '100%',
    },
})))

const AnimeCardContent = animated(styled(CardContent)(({ theme }) => ({
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'space-between',
})))



const ScoreContainer = styled(Box)(({ theme }) => ({
    width: useCopyMal() && `75px`,
    margin: `auto`,
    justifyContent: `center`,
    textAlign: `center`,
}));

const ScoreLabel = styled(Box)(({ theme }) => ({
    backgroundColor: `#2e51a2`,
    fontSize: `10px`,
    color: useCopyMal() && `#e0e0e0`,
    lineHeight: useCopyMal() && `1.0em`,
    fontWeight: !useCopyMal() && `bold`,
    padding: useCopyMal() ? `2px 0px` : `1px 5px 1px 5px`,
    width: useCopyMal() && `60px`,
    borderRadius: `2px`,
    margin: `auto`,
}));
const AnimeInfo = styled(Typography)(({ theme }) => ({
    marginTop: 'auto',
    fontSize: '16px',
}));

const ScoreValue = styled(AnimeInfo)(({ theme }) => ({
    fontWeight: `bold`,
    fontSize: `24px`,
}));

const ScoreUserCount = styled(AnimeInfo)(({ theme }) => ({
    fontSize: useCopyMal() ? `10px` : `12px`,
}));

const InfoValue = styled('span')(({ theme, copyMal }) => ({
    fontFamily: useCopyMal() && `Avenir,lucida grande,tahoma,verdana,arial,sans-serif`,
    fontWeight: `bold`,
    fontSize: `16px`
}));


const AnimatedScoreValue = animated(ScoreValue); // Wrap ScoreValue with animated
const AnimatedScoreUserCount = animated(ScoreUserCount); // Wrap ScoreValue with animated
const AnimatedInfoValue = animated(InfoValue); // Wrap ScoreValue with animated

const ExtraAnimeInfo = styled(AnimeInfo)(({ theme }) => ({
    fontSize: '12px',
    color: useCopyMal() ? `#abc4ed` : `#AEAEAE`,
}));


export default function AnimePanel({ media, copyMal, darkMode }) {
    const { rank, popularity, members, season, year, type, studios, serializations, score, authors, scored_by } = media

    return (

        <CopyMalProvider copyMal={copyMal}>
            <AnimeCard>
                <AnimeCardContent>
                    <ScoreContainer>
                        <ScoreLabel>SCORE</ScoreLabel>

                        <ScoreValue>{score.toFixed(2)}</ScoreValue>
                        <ScoreUserCount>{scored_by.toLocaleString()} users</ScoreUserCount>
                    </ScoreContainer>
                    <Divider flexItem orientation='vertical' sx={{
                        mx: 2,
                        backgroundColor: copyMal ? `#272727` : `gray`,
                        boxShadow: copyMal ? `#121212 1px 0 1px 0` : ``
                    }
                    } />
                    <Box>
                        <Stack direction="row" spacing={2}>
                            <AnimeInfo variant="body1">Ranked
                                <InfoValue sx={{
                                    fontFamily: `Avenir,lucida grande,tahoma,verdana,arial,sans-serif`
                                }}>
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
                            {season && (
                                <>
                                    <Divider flexItem orientation='vertical' sx={{
                                        mx: 2,
                                        backgroundColor: copyMal ? `#272727` : `gray`,
                                        boxShadow: copyMal ? `#121212 1px 0 1px 0` : ``

                                    }
                                    } />
                                    <ExtraAnimeInfo variant="body1">{`${season?.replace(/\b\w/g, (match) => match.toUpperCase())} ${year}`}</ExtraAnimeInfo>
                                </>
                            )}

                            <ExtraAnimeInfo variant="body1">{type}</ExtraAnimeInfo>
                            <Divider flexItem orientation='vertical' sx={{
                                mx: 2,
                                backgroundColor: copyMal ? `#272727` : `gray`,
                                boxShadow: copyMal ? `#121212 1px 0 1px 0` : ``

                            }
                            } />
                            {studios && <ExtraAnimeInfo variant="body1">{studios[0]?.name}</ExtraAnimeInfo>}
                            {serializations && <ExtraAnimeInfo variant="body1">{serializations[0]?.name}</ExtraAnimeInfo>}
                            <Divider flexItem orientation='vertical' sx={{
                                mx: 2,
                                backgroundColor: copyMal ? `#272727` : `gray`,
                                boxShadow: copyMal ? `#121212 1px 0 1px 0` : ``

                            }
                            } />
                            {authors && <ExtraAnimeInfo variant="body1">{authors[0]?.name}</ExtraAnimeInfo>}

                        </Stack>
                    </Box>
                </AnimeCardContent>
            </AnimeCard>
        </CopyMalProvider>
    )
}
