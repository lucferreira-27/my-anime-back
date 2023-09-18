import React, { useState, useEffect } from 'react';
import { Paper, Slider, Stack, TextField, Typography } from '@mui/material';
import { PauseRounded, PlayArrowRounded } from '@mui/icons-material';
import styled from '@mui/system/styled';
import { useSpring, animated } from 'react-spring'; // Assuming you forgot to import this based on the original code
import TimelinePlayer from "./TimelinePlayer"
const StyledPaper = styled(Paper)({
    padding: '15px',
    marginTop: '50px',
    backgroundColor: '#24292f',
    width: '100%',
});

const StyledSlider = styled(Slider)({
    color: 'primary',
    width: '100%',
    borderRadius: 0,
    '& .MuiSlider-thumb': {
        color: 'white',
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
});

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


const TimelineDate = styled(Typography)({
    fontSize: '1rem',
    color: `gray`,
});

const AnimatedTimelineDate = animated(TimelineDate)


export default function TimelineSlider({ resources, timeMedia, setTimeMedia }) {
    const [sliderValue, setSliderValue] = useState(0);
    const [currentDate, setCurrentDate] = useState(null);
    const [isPlaying, setIsPlaying] = useState(false);
    const [isReseting, setIsReset] = useState(false);
    const [duration, setDuration] = useState(15);

    const milesecondsDateSpring = useSpring({
        to: { milesecondsDate: currentDate },
        from: { milesecondsDate: currentDate - 1 },
        config: { duration: (duration / resources.length) * 1000 },
    });

    const handleSliderChange = (event, newValue) => {
        setSliderValue(newValue);
    };

    const togglePlayPause = () => {
        setIsPlaying((prev) => !prev);

        if (sliderValue === resources.length && !isPlaying) {
            setIsReset(true);
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
        let interval;

        if (isReseting) {
            interval = setInterval(() => {
                setSliderValue((prevValue) => (prevValue > 0 ? prevValue - 5 : 0));
                if (sliderValue === 0) {
                    setIsReset(false);
                    clearInterval(interval);
                }
            }, 10);
        }

        return () => clearInterval(interval);
    }, [isReseting, sliderValue]);

    useEffect(() => {
        let interval;

        if (isPlaying && sliderValue < resources.length) {
            interval = setInterval(() => {
                setSliderValue((prevValue) =>
                    prevValue + 1 <= resources.length ? prevValue + 1 : resources.length
                );
            }, (duration / resources.length) * 1000);
        }

        if (sliderValue === resources.length && isPlaying) {
            togglePlayPause();
            clearInterval(interval);
        }

        return () => clearInterval(interval);
    }, [isPlaying, sliderValue]);

    return (
        <StyledPaper>
            <Stack>
                <StyledSlider
                    valueLabelDisplay="on"
                    marks
                    max={resources.length}
                    onChange={handleSliderChange}
                    value={sliderValue}
                    disabled={false}
                />
                <TimelinePlayer isPlaying={isPlaying}
                    resources={resources}
                    togglePlayPause={togglePlayPause}
                    currentDate={currentDate}
                    duration={duration}
                    setDuration={setDuration} 
                />
            </Stack>
        </StyledPaper>
    );
}
