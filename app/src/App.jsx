import { useState, createContext,useEffect } from 'react';
import SearchBar from "./SearchBar";
import { styled } from '@mui/system';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import { Container } from '@mui/material';
import InfoPanel from './InfoPanel';
import Timeline from './Timeline';

const StyledAppBar = styled(AppBar)({
  backgroundColor: '#1450A3',
  justifyContent: 'center', // Center horizontally
  alignItems: 'center',     // Center vertically
});

const NavbarTitle = styled('div')({
  flexGrow: 1,
  color: 'white',

});

const AppName = styled('span')({
  fontWeight: 'bold',
  fontSize: 36,
  fontFamily: 'Montserrat, sans-serif', // Apply Montserrat font

});

export const Context = createContext()

function App() {


  const [media, setMedia] = useState()

  const [resources, setResources] = useState([])
  const [isShowTimeLine, setShowTimeline] = useState(false)
  return (
    <Context.Provider value={{ media, setMedia, resources, setResources, isShowTimeLine, setShowTimeline }}>

      <StyledAppBar position="sticky">
        <Toolbar>
          <NavbarTitle>
            <AppName>MyAnimeBack</AppName>
          </NavbarTitle>
        </Toolbar>
      </StyledAppBar>
      <SearchBar />
      {
        media && <InfoPanel media={media} />
      }
      {
        isShowTimeLine && <Timeline />
      }


    </Context.Provider>
  );
}

export default App;