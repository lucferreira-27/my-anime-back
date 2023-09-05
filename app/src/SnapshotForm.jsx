import React from 'react';
import { Button, Typography, Divider, Box, FormControl, InputLabel, Select, MenuItem, Grid, TextField } from '@mui/material';
import { styled } from '@mui/system';

const FormContainer = styled('form')`
  display: grid;
  gap: 16px;
  background-color: #24292f;
  padding: 20px;
  border-radius: 5px
`;

const StyledFormControl = styled(FormControl)`
  && {
    width: 100%;
  }
  
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
  }
`;

const StyledTextField = styled(TextField)`
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

export default function SnapshotForm({ formData, setFormData, handleData, rangeSnapshots }) {
    return (
        <>
            <FormContainer onSubmit={handleData}>
                <Grid container spacing={2}>
                    <Grid item xs={6}>
                        <StyledFormControl>
                            <StyledTextField
                                InputLabelProps={{ shrink: true }}
                                id="startDate"
                                label="Start Date"
                                type="date"
                                variant="outlined"
                                onChange={(e) => setFormData({ ...formData, startDate: e.target.value })}
                                value={formData.startDate}
                                required
                            />
                        </StyledFormControl>
                    </Grid>
                    <Grid item xs={6}>
                        <StyledFormControl>
                            <StyledTextField
                                InputLabelProps={{ shrink: true }}
                                label="End Date"
                                id="endDate"
                                type="date"
                                variant="outlined"
                                onChange={(e) => setFormData({ ...formData, endDate: e.target.value })}
                                value={formData.endDate}
                                required
                            />
                        </StyledFormControl>
                    </Grid>
                    <Grid item xs={6}>
                        <StyledFormControl>
                            <StyledTextField
                                label="Distance"
                                id="distance"
                                type="number"
                                variant="outlined"
                                onChange={(e) => setFormData({ ...formData, distance: e.target.value })}
                                value={formData.distance}
                                required
                            />
                        </StyledFormControl>
                    </Grid>
                    <Grid item xs={6}>
                        <StyledFormControl>
                            <InputLabel htmlFor="distanceType">Distance Type</InputLabel>
                            <Select
                                label="Distance Type"
                                id="distanceType"
                                variant="outlined"
                                onChange={(e) => setFormData({ ...formData, distanceType: e.target.value })}
                                value={formData.distanceType}
                                required
                            >
                                <MenuItem value="days">Days</MenuItem>
                                <MenuItem value="months">Months</MenuItem>
                                <MenuItem value="years">Years</MenuItem>
                            </Select>
                        </StyledFormControl>
                    </Grid>
                    <Grid item xs={12} style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                        <Box>
                            <Typography
                                variant="caption"
                                sx={{
                                    color: 'white',
                                    padding: '3px 6px',
                                    borderRadius: '5px',
                                    backgroundColor: '#1c2025',
                                    alignSelf: 'center',
                                }}
                            >
                                {`It's projected that ${rangeSnapshots?.length || 3000} snapshots will be part of this timeline.`}
                            </Typography>
                        </Box>
                    </Grid>
                    <Grid item xs={12}>
                        <Button variant="contained" type="submit" sx={{ width: '100%' }}>
                            CREATE TIMELINE
                        </Button>
                    </Grid>

                </Grid>
            </FormContainer>
        </>

    );
}
