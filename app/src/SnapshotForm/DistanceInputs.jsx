import React from 'react';
import { Grid,FormControl,TextField,InputLabel,Select,MenuItem } from '@mui/material';
import { styled } from '@mui/system';

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

const DistanceInputs = ({ formData, setFormData }) => {
    return (
        <>
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
        </>
    );
};

export default DistanceInputs;
