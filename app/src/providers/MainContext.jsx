import React, { createContext, useContext, useState } from 'react';

const MainContext = createContext();

function MainProvider({ children }) {
  const [media, setMedia] = useState();
  const [resources, setResources] = useState([]);
  const [isShowTimeline, setShowTimeline] = useState(false);

  return (
    <MainContext.Provider value={{ media, setMedia, resources, setResources, isShowTimeline, setShowTimeline }}>
      {children}
    </MainContext.Provider>
  );
}

export function useMainContext() {
  return useContext(MainContext);
}
