import React from 'react'
import styled from '@mui/system/styled';
import { Paper, Slider, Stack, TextField, Typography } from '@mui/material';
import { PauseRounded, PlayArrowRounded } from '@mui/icons-material';
import { useSpring, animated } from 'react-spring'; // Assuming you forgot to import this based on the original code

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

export default function TimelinePlayer({ isPlaying, resources, togglePlayPause, currentDate, duration, setDuration }) {

    const milesecondsDateSpring = useSpring({
        to: { milesecondsDate: currentDate },
        from: { milesecondsDate: currentDate - 1 },
        config: { duration: (duration / resources.length) * 1000 },
    });


    return (
        <Stack
            direction="row"
            justifyContent="space-between"
            alignItems="flex-start"
            spacing={2}
        >
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
                            fontSize: "3rem",
                            color: "white",
                            cursor: "pointer",
                            '&:hover': { transform: 'scale(1.1)' }
                        }}
                        onClick={togglePlayPause}
                    />
                ) : (
                    <PlayArrowRounded
                        sx={{
                            fontSize: "3rem",
                            color: "white",
                            cursor: "pointer",
                            '&:hover': { transform: 'scale(1.1)' }
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
    )
}
