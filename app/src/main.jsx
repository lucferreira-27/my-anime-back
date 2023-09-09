import React from 'react';
import ReactDOM from 'react-dom';
import App from './App.jsx';
import './index.css';

// Wrap your App component with MainProvider
ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
      <App />
  </React.StrictMode>,
);
