import axios from 'axios';
import React, { useState } from 'react';
import Show from './components/Show';
import Upload from './components/Upload';

const backendUrl = "http://localhost:8080/api";
axios.defaults.baseURL = backendUrl;

export const App: React.FC = () => {

  const [availablePowerStations, setAvailablePowerStations] = useState<string[]>([]);

  return (
    <div>
      <Upload/>
      <Show
        availablePowerStations={availablePowerStations}
        setAvailablePowerStations={setAvailablePowerStations}
      />
    </div>
  );
};
