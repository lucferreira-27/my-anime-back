import React from 'react';
import { Grid,FormControl,TextField } from '@mui/material';
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


const DateInputs = ({ formData, setFormData }) => {
    return (
        <>
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
        </>
    );
};

export default DateInputs;
