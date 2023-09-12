import { React, useState, useEffect, useContext } from 'react';
import {
    Container,
    Divider,
    Paper,
    Box,
    Slider,
    Stack,
    TextField,
    Typography,
} from '@mui/material';
import PauseRounded from '@mui/icons-material/PauseRounded';
import PlayArrowRounded from '@mui/icons-material/PlayArrowRounded';
import { Context } from "./App"
import TimeDisplay from './TimeDisplay';
import { styled } from '@mui/system';
import { useSpring, animated } from 'react-spring'; // Import the necessary components from react-spring

const TimelineDate = styled(Typography)(({ theme }) => ({
    color: `gray`,
}));

const AnimatedTimelineDate = animated(TimelineDate)

const StyledTextField = styled(TextField)`
    width: 100px;
  & label.MuiFormLabel-root {
    color: white;
  }

  & .MuiInputBase-root {

    color: #AEAEAE;
  }

  .MuiInput-underline:before {
    border-bottom: 2px solid gray;
  }
  /* hover (double-ampersand needed for specificity reasons. */
  && .MuiInput-underline:hover:before {
    border-bottom: 2px solid white;
  }
  /* focused */
  .MuiInput-underline:after {
    border-bottom: 2px solid white;
  }

`;


function Timeline() {
    const { resources, media } = useContext(Context)
    const [timeMedia, setTimeMedia] = useState({ ...media })
    const [sliderValue, setSliderValue] = useState(0); // Initialize with an initial value
    const [currentDate, setCurrentDate] = useState(null)
    const [isPlaying, setIsPlaying] = useState(false);
    const [isReseting, setIsReset] = useState(false);
    const [duration, setDuration] = useState(15)
    const milesecondsDateSpring = useSpring({
        to: { milesecondsDate: currentDate },
        from: { milesecondsDate: 0 }, // You can change the initial value
        config: { duration: (duration / resources.length) * 1000 }, 

    });


    const handleSliderChange = (event, newValue) => {
        setSliderValue(newValue); // Update the state when the slider value changes
    };


    const togglePlayPause = () => {
        setIsPlaying(!isPlaying);
        if (sliderValue == resources.length && !isPlaying) {
            setIsReset(true)
        }
    };

    useEffect(() => {
        const currentResource = resources[sliderValue]
        if (currentResource) {
            timeMedia.archiveDate = currentResource.archiveDate
            timeMedia.score = currentResource.scoreValue
            timeMedia.rank = currentResource.ranked
            timeMedia.members = currentResource.members
            timeMedia.popularity = currentResource.popularity
            timeMedia.scored_by = currentResource.totalVotes
            setTimeMedia({ ...timeMedia })
            setCurrentDate(new Date(currentResource.archiveDate).getTime())
        }

    }, [sliderValue])
    useEffect(() => {
        if (isReseting) {
            const interval = setInterval(() => {
                setSliderValue((prevValue) => (prevValue > 0 ? prevValue - 5 : 0));
            }, 10);

            if (sliderValue === 0) {
                setIsReset(false)
                clearInterval(interval); // Clear the interval when sliderValue reaches 0
            }

            return () => {
                clearInterval(interval); // Clear the interval on component unmount
            };
        }
    }, [isReseting, sliderValue]);

    useEffect(() => {
        let interval;

        if (isPlaying && sliderValue < resources.length) {
            interval = setInterval(() => {
                setSliderValue((prevValue) =>
                    prevValue + 1 <= resources.length ? prevValue + 1 : resources.length
                );
            }, (duration / resources.length) * 1000); 
        } else {
            if (isPlaying) {
                togglePlayPause()
            }
            clearInterval(interval);
        }

        return () => clearInterval(interval);
    }, [isPlaying, sliderValue]);


    return (
        <Container
            sx={{
                mt: `100px`,
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                height: '100vh',
            }}
        >
            <Divider
                flexItem
                sx={{
                    color: 'white',
                    "&::before, &::after": {
                        borderColor: 'white',
                    },
                    letterSpacing: '8px',
                    fontSize: {
                        xs: '24px',
                        sm: '32px',
                    },
                }}
            >
                Timeline
            </Divider>
            <Box
                sx={{
                    width: `100%`,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                }}
            >
                <Paper
                    sx={{
                        padding: `15px`,
                        mt: `50px`,
                        backgroundColor: `#24292f`,
                        width: '100%',
                    }}
                >
                    <Stack >
                        <Slider

                            valueLabelDisplay="test"
                            marks
                            max={resources.length}
                            sx={{
                                color: `primary`,
                                width: `100%`,
                                borderRadius: 0,

                                '& .MuiSlider-thumb': {
                                    color: `white`,
                                    borderRadius: 0,
                                    width: '10px',
                                    height: '20px',
                                    transition: 'all 0.5s ease',

                                },
                                '& .MuiSlider-track': {
                                    borderRadius: 0,
                                    transition: 'all 0.5s ease',

                                },
                                '& .MuiSlider-rail': {
                                    borderRadius: 0,
                                    color: 'gray',
                                },
                                '& .MuiSlider-thumb.Mui-active': {
                                    boxShadow: 'none',
                                    borderRadius: '10px',
                                },
                                '& .MuiSlider-thumb.Mui-focusVisible': {
                                    boxShadow: 'none',
                                },
                                '& .MuiSlider-thumb:hover': {
                                    boxShadow: 'none',
                                    borderRadius: '10px',
                                },
                            }}
                            valueLabelDisplay="auto"
                            onChange={handleSliderChange}
                            value={sliderValue}
                            disabled={false}
                        />
                        <Stack
                            direction="row"
                            justifyContent="space-between"
                            alignItems="flex-start"
                            spacing={2}>
                            <AnimatedTimelineDate>
                                {milesecondsDateSpring.milesecondsDate.to((value) =>
                                    `${new Date(value).toLocaleDateString("en-US", {
                                        year: "numeric",
                                        month: "2-digit",
                                        day: "2-digit",
                                    })}`
                                )}
                            </AnimatedTimelineDate>
                            <div>
                                {isPlaying ? (
                                    <PauseRounded
                                        sx={{
                                            fontSize: '3rem',
                                            color: 'white',
                                            cursor: 'pointer',
                                            '&:hover': {
                                                transform: 'scale(1.1)', // Add hover effect
                                            },
                                        }}
                                        onClick={togglePlayPause}
                                    />
                                ) : (
                                    <PlayArrowRounded
                                        sx={{
                                            fontSize: '3rem',
                                            color: 'white',
                                            cursor: 'pointer',
                                            '&:hover': {
                                                transform: 'scale(1.1)', // Add hover effect
                                            },
                                        }}
                                        onClick={togglePlayPause}
                                    />
                                )}
                            </div>
                            <StyledTextField
                                label="Player duration"
                                id="distance"
                                type="number"
                                variant="standard"
                                onChange={(e) => setDuration(e.target.value)}
                                value={duration}
                            />
                        </Stack>
                    </Stack>
                </Paper>
                <Paper
                    sx={{
                        mt: `50px`,
                        color: `#fff`,
                        height: `500px`,
                        minWidth: 275,
                        backgroundColor: '#1f2938',
                        width: '100%',
                        padding: 2,
                        borderRadius: 2,
                        boxShadow: '0px 4px 6px rgba(0, 0, 0, 0.1)',
                    }}
                >
                    <TimeDisplay timeMedia={timeMedia} />
                    {/* Content inside the Paper component */}
                </Paper>
            </Box>
        </Container >
    );
}

export default Timeline;
