import React, { useState, useRef, useEffect } from 'react';
import {
    Container,
    InputAdornment,
    TextField,
    Box,
    Typography,
    createTheme,
} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import { styled } from '@mui/system';


const theme = createTheme({
    palette: {
        primary: {
            main: '#1976D2',
            dark: '#115293',
            light: '#4791DB',
        },
        text: {
            secondary: '#B0B0B0',
        },
    },
});

const SearchTextField = styled(TextField)({
    width: '100%',
    backgroundColor: 'white',
    borderRadius: '10px',
    color: 'primary',
});



export default function SearchBar() {
    const [searchTerm, setSearchTerm] = useState('');
    const [isValidUrl, setIsValidUrl] = useState(true);
    const [isShowError, setShowError] = useState(false);
    const inputRef = useRef(null);

    useEffect(() => {
        const urlPattern = /^https:\/\/myanimelist\.net\/anime|manga\/\d+(\/.*)?$/;
        const isUrlValid = urlPattern.test(searchTerm);
        setIsValidUrl(isUrlValid);
        setShowError(!isUrlValid && searchTerm.length > 0);
    }, [searchTerm]);

    const handleChange = (event) => {
        setSearchTerm(event.target.value);
    };

    const handleSearch = () => {
        try {
            new URL(searchTerm);
            // URL is valid, perform your search logic here
        } catch (error) {
            setIsValidUrl(false);
        }
    };


    return (
        <Container maxWidth="md" sx={{ mt: 10 }}>
            <Box>
                <SearchTextField
                    label="Search"
                    variant="filled"
                    value={searchTerm}
                    onChange={handleChange}
                    inputRef={inputRef}
                    error={isShowError}
                    helperText={isShowError && 'Please enter a valid MyAnimeList URL'}
                    InputProps={{
                        endAdornment: (
                            <InputAdornment position="end">
                                <SearchIcon color="primary"
                                    disabled={!isValidUrl}
                                    onClick={handleSearch}
                                    sx={
                                        {
                                            color: isValidUrl ? theme.palette.primary.main : theme.palette.text.secondary,
                                            cursor: isValidUrl ? 'pointer' : 'not-allowed',
                                            transition: 'color 0.3s ease-in-out',
                                            '&:hover': {
                                                color: isValidUrl ? theme.palette.primary.dark : theme.palette.text.secondary,
                                            },
                                            '&:active': {
                                                color: isValidUrl ? theme.palette.primary.light : theme.palette.text.secondary,
                                            },
                                        }
                                    } />
                            </InputAdornment>
                        ),
                        disableUnderline: true,
                    }}
                />
                {!searchTerm && <Typography
                    variant="body2"
                    color="#FFF5"
                    sx={{
                        mt: 1,
                        ml: 2,
                        fontSize: { xs: '12px', sm: '13px' },
                        whiteSpace: { xs: 'normal', sm: 'nowrap' },
                        overflow: 'hidden',
                        textOverflow: 'ellipsis',
                        maxWidth: { xs: 'calc(100% - 200px)' },
                    }}
                >
                    You can search by directly entering a MyAnimeList URL
                </Typography>}
                
            </Box>
        </Container>
    );
}
