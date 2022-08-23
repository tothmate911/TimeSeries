import axios from 'axios';
import React, { useEffect, useState } from 'react'

const backendUrl = "http://localhost:8080/api";
axios.defaults.baseURL = backendUrl;

interface Props {
  setSelectedPowerStation: React.Dispatch<React.SetStateAction<string>>
}

const PowerStationSelector: React.FC<Props> = ({ setSelectedPowerStation }) => {

  const [availablePowerStations, setAvailablePowerStations] = useState([]);

  useEffect(() => {
    axios.get("/power-stations")
      .then((response) => {
        setAvailablePowerStations(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  }, []);

  return (
    <select
      name="PowerStations" id="PowerStations"
      onChange={e => setSelectedPowerStation(e.target.value)}
    >
      {availablePowerStations.map(powerStation => (
        <option key={powerStation} value={powerStation}>
          {powerStation}
        </option>
      ))}
    </select>
  )
}

export default PowerStationSelector