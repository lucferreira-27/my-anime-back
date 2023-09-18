import React, { useState, useContext, useEffect, useRef } from 'react';
import { Box, Button, Typography, Modal, Grid, CircularProgress, TextField, FormControlLabel, Switch, InputAdornment, Divider } from '@mui/material';
import TimeDisplay from '../TimeDisplay';
import { styled } from '@mui/material/styles';
import useGifGenerator from '../hooks/useGifGenerator';



const StyledBox = styled('div')(({ theme }) => ({
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'space-between',
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    height: "450px",
    [theme.breakpoints.down('xs')]: { // Medium devices (tablets, 768px and up)
        width: "90%",
        height: "80%",
    },
    borderRadius: `15px`,
    backgroundColor: '#24292f',
    color: '#FFFFFF',
    boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)',
    padding: '20px',
    overflowY: 'auto',
}));


const StyledTextField = styled(TextField)`
    width: 100px;
  & label.MuiFormLabel-root {
    color: white;
  }

  & .MuiOutlinedInput-root {
    &.Mui-focused fieldset {
      border-color: white;
    }
    color: #AEAEAE;
    border-color: #0288d1;

    &:hover fieldset {
      border-color: #ffffff; /* Change the hover border color */
    }

    & .MuiSvgIcon-root { // Style for the icon
      fill: white; /* Change the icon color */
    }
  }
`;


const ConvertGif = ({ timeDisplayRef, resources, setTimeMedia, timeMedia }) => {
    const [open, setOpen] = useState(false);
    const [config, setConfig] = useState({ delay: 500, dither: false, quality: 10 })

    const { isGenerating, createGif } = useGifGenerator(config, timeDisplayRef, resources, setTimeMedia, timeMedia?.title);

    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    return (
        <div>
            <Button onClick={handleOpen} variant="outlined" color="primary">Convert to GIF</Button>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <StyledBox>
                    <Typography id="modal-modal-title" variant="h5" component="h2">
                        Customize GIF Settings
                    </Typography>
                    <Grid container spacing={2} >
                        <Grid item xs={12} md={10}>
                            <Typography variant="p" component="h3">
                                Preview
                            </Typography>
                            <TimeDisplay timeMedia={timeMedia} />
                            {!isGenerating ?
                                <Button sx={{ mt: 2 }} variant="contained" color="primary" onClick={() => createGif()}>Download GIF</Button>
                                :
                                <CircularProgress sx={{ mt: 2 }} />
                            }
                        </Grid>

                        <Grid item container xs={12} md={2} direction="column" spacing={2}>
                            <Grid item>
                                <StyledTextField
                                    sx={{
                                        width: {
                                            xs: '100%', // For extra small devices and up
                                            sm: 'auto', // Adjust as necessary for small devices and up
                                        },
                                        mt: {
                                            xs: 1, // For extra small devices and up
                                            sm: 5, // Adjust as necessary for small devices and up
                                        },
                                    }}
                                    label="Quality"
                                    value={config.quality}
                                    onChange={(e) => setConfig((prev) => ({ ...prev, quality: e.target.value }))}
                                    type="number"
                                />
                            </Grid>

                            <Grid item >
                                <StyledTextField
                                    sx={{
                                        width: {
                                            xs: '100%', // For extra small devices and up
                                            sm: 'auto', // Adjust as necessary for small devices and up
                                        },
                                        mt: {
                                            xs: 1, // For extra small devices and up
                                            sm: 1, // Adjust as necessary for small devices and up
                                        },
                                    }}
                                    label="Delay"
                                    value={config.delay}
                                    onChange={(e) => setConfig((prev) => ({ ...prev, delay: e.target.value }))}
                                    type="number"
                                />
                            </Grid>

                            <Grid item>
                                <FormControlLabel
                                    control={
                                        <Switch
                                            checked={config.dither}
                                            onChange={() => setConfig((prev) => ({ ...prev, dither: !config.dither }))}
                                            name="dither"
                                            color="primary"
                                        />
                                    }
                                    label="Dither"
                                />
                            </Grid>
                        </Grid>
                    </Grid>
                </StyledBox>
            </Modal>
        </div>
    );
}

export default ConvertGif;

