import { useState } from 'react';
import SearchBar from "./SearchBar";
import { styled } from '@mui/system';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import { Container } from '@mui/material';
import InfoPanel from './InfoPanel';

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

function App() {

  const [result, setResult] = useState()

  return (
    <div>
      <StyledAppBar position="sticky">
        <Toolbar>
          <NavbarTitle>
            <AppName>MyAnimeBack</AppName>
          </NavbarTitle>
        </Toolbar>
      </StyledAppBar>
      <SearchBar result={result} setResult={setResult} />
      {
        result && (
          <InfoPanel result={result} />
        )
      }

    </div>
  );
}

export default App;