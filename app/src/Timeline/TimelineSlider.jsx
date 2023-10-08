import React, { useState, useEffect, useCallback } from 'react';
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
        transition: '',
    },
    '& .MuiSlider-track': {
        borderRadius: 0,
        transition: '',
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

function debounce(func, delay) {
    let debounceTimer;
    return function () {
        const context = this;
        const args = arguments;
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => func.apply(context, args), delay);
    };
}

export default function TimelineSlider({ resources, timeMedia, setTimeMedia }) {
    const [sliderValue, setSliderValue] = useState(0);
    const [currentDate, setCurrentDate] = useState(null);
    const [isPlaying, setIsPlaying] = useState(false);
    const [isReseting, setIsReset] = useState(false);
    const [duration, setDuration] = useState(15);
    const [isChangeCommitted, setChangeCommited] = useState(false)
    const milesecondsDateSpring = useSpring({
        to: { milesecondsDate: currentDate },
        from: { milesecondsDate: currentDate - 1 },
        config: { duration: (duration / resources.length) * 1000 },
    });

    const handleSliderChange = (event, newValue) => {
        setSliderValue(newValue);
    };

    const togglePlayPause = useCallback(() => {
        setIsPlaying((prev) => !prev);

        if (sliderValue === resources.length && !isPlaying) {
            setIsReset(true);
        }
    }, [isPlaying, resources.length, sliderValue]);

    useEffect(() => {
        const currentResource = resources[sliderValue - 1];
        if (!currentResource) {
            setChangeCommited(false)
            return
        }
        if (isChangeCommitted) {

            setTimeMedia((prevTimeMedia) => ({
                ...prevTimeMedia,
                archiveDate: currentResource.archiveDate,
                score: currentResource.scoreValue,
                rank: currentResource.ranked,
                members: currentResource.members,
                popularity: currentResource.popularity,
                scored_by: currentResource.totalVotes,
            }));
            setChangeCommited(false)
        }
    }, [isChangeCommitted])

    useEffect(() => {
        const currentResource = resources[sliderValue];
        if (currentResource) {
            setCurrentDate(currentResource.responseSnapshot.timestamp.dateInMillis);
        }
        if (isPlaying && currentResource) {
            const currentResource = resources[sliderValue];

            setTimeMedia((prevTimeMedia) => ({
                ...prevTimeMedia,
                archiveDate: currentResource.archiveDate,
                score: currentResource.scoreValue,
                rank: currentResource.ranked,
                members: currentResource.members,
                popularity: currentResource.popularity,
                scored_by: currentResource.totalVotes,
            }));
        }
    }, [sliderValue, resources, setTimeMedia]);

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
    }, [isPlaying, sliderValue, resources.length, duration, togglePlayPause]);

    return (
        <StyledPaper>
            <Stack>
                <StyledSlider
                    max={resources.length}
                    onChange={handleSliderChange}
                    onChangeCommitted={() => setChangeCommited(true)}
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
