import React from 'react';
import { Button } from '@mui/material';
import { styled } from '@mui/system';

const SubmitButton = ({ working, resources, onClick }) => {
    return (
        <Button variant="contained" sx={{ width: '100%' }} onClick={() => onClick()}>
            {!working && resources.length > 0 ? `VIEW TIMELINE` : `CREATE TIMELINE`}
        </Button>
    );
};

export default SubmitButton;
