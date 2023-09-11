import React from 'react';
import { Button } from '@mui/material';
import { styled } from '@mui/system';

const SubmitButton = ({ onClick,text }) => {
    return (
        <Button variant="contained" sx={{ width: '100%' }} onClick={onClick}>
            {text}
        </Button>
    );
};

export default SubmitButton;
