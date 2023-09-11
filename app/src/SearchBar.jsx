import React, { useState, useRef, useEffect, useContext } from 'react';
import {
    Container,
    InputAdornment,
    TextField,
    Box,
    Typography,
    createTheme,
    CircularProgress,

} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import { styled } from '@mui/system';
import useJikanSearch from './hooks/useJikanSearch'; // Import the custom hook
import useDebounce from './hooks/useDebounce'; // Import the custom hook
import { Context } from "./App"
const theme = createTheme({
    palette: {
        primary: {
            main: '#1976D2',
            dark: '#115293',
            light: '#4791DB',
        },
        secondary: {
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
    const { media, setMedia, isShowTimeLine, setShowTimeline,resources,setResources } = useContext(Context)
    const [searchTerm, setSearchTerm] = useState('');
    const [isValidUrl, setIsValidUrl] = useState(true);
    const [isShowError, setShowError] = useState(false);
    const { searchData, setSearchData, loading, error, setError, setLoading, performSearch } = useJikanSearch();

    const inputRef = useRef(null);


    useEffect(() => {
        if (!media) {
            if(isShowTimeLine){
                setShowTimeline(false)
            }
            if(resources){
                setResources([])
            }
        }
    }, [media])

    useEffect(() => {
        console.log(searchData)
        setMedia(searchData)
    }, [searchData])

    useEffect(() => {
        const urlPattern = /^https:\/\/myanimelist\.net\/anime|manga\/\d+(\/.*)?$/;
        const isUrlValid = urlPattern.test(searchTerm);
        setIsValidUrl(isUrlValid);
        setShowError(!isUrlValid && searchTerm.length > 0);
    }, [searchTerm]);

    useEffect(() => {
        if (isValidUrl && searchTerm) {
            handleDebouncedSearch()
        }
    }, [isValidUrl])

    const handleChange = (event) => {
        if (!searchData) {
            setSearchTerm(event.target.value);
        }
    };

    const handleSearch = () => {
        performSearch(searchTerm);
    };

    const resetSearch = () => {
        setSearchTerm('');
        setSearchData(null);
        setError(null);
        setIsValidUrl(true);
        setMedia(null)
    };
    const DEBOUNCE_DELAY = 300; // milliseconds
    const handleDebouncedSearch = useDebounce(handleSearch, DEBOUNCE_DELAY);

    return (
        <Container maxWidth="md" sx={{ mt: 6 }}>
            <Box>
                <SearchTextField
                    fontColor="green"
                    label="Search"
                    variant="filled"
                    value={!searchData ? searchTerm : searchData.titles[0].title}
                    onChange={handleChange}
                    inputRef={inputRef}
                    error={isShowError}
                    helperText={isShowError && 'Please enter a valid MyAnimeList URL'}
                    InputProps={{
                        style: {
                            color: '#555555'
                        },
                        endAdornment: (
                            <InputAdornment position="end">

                                {loading ? (
                                    <CircularProgress color="primary" size={20} />
                                ) : searchData ? (
                                    <HighlightOffIcon
                                        color="primary"
                                        onClick={resetSearch}
                                        sx={{ cursor: 'pointer' }}
                                    />
                                ) : (
                                    <SearchIcon color="primary"
                                        disabled={!isValidUrl}
                                        sx={
                                            {
                                                color: isValidUrl ? theme.palette.primary.main : theme.palette.text.secondary,
                                                cursor: 'pointer',
                                                transition: 'color 0.3s ease-in-out',
                                                '&:hover': {
                                                    color: isValidUrl ? theme.palette.primary.dark : theme.palette.text.secondary,
                                                },
                                                '&:active': {
                                                    color: isValidUrl ? theme.palette.primary.light : theme.palette.text.secondary,
                                                },
                                            }
                                        } />
                                )
                                }

                            </InputAdornment>
                        ),
                        disableUnderline: true,
                    }}
                />
                {error && (
                    <Typography
                        variant="body2"
                        color="error"
                        sx={{ mt: 1, ml: 2 }}
                    >
                        {error}
                    </Typography>
                )}
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
