import axios from 'axios';
import React, { useEffect, useState } from 'react'

const localStoragePowerStationKey: string = "localStoragePowerStationKey";

interface Props {
  selectedPowerStation: string;
  setSelectedPowerStation: React.Dispatch<React.SetStateAction<string>>;
}

const PowerStationSelector: React.FC<Props> = ({
  selectedPowerStation, setSelectedPowerStation
}) => {

  const [availablePowerStations, setAvailablePowerStations] = useState<string[]>([]);

  useEffect(() => {
    axios.get("/power-stations")
      .then((response) => {
        const powerStations: string[] = response.data;
        setAvailablePowerStations(powerStations);
      })
      .catch((error) => {
        console.log(error);
      });
  }, [setAvailablePowerStations]);

  useEffect(() => {
    if (availablePowerStations.length === 0) {
      return;
    }

    const storedPowerStation: string = localStorage.getItem(localStoragePowerStationKey) || "";
    if (availablePowerStations.includes(storedPowerStation)) {
      setSelectedPowerStation(storedPowerStation);
    } else {
      const firstAvailablePowerStation = availablePowerStations[0];
      setSelectedPowerStation(firstAvailablePowerStation);
      localStorage.setItem(localStoragePowerStationKey, firstAvailablePowerStation);
    }
  }, [availablePowerStations, setSelectedPowerStation]);

  const handlePowerStationSelectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    let powerStation = e.target.value;
    setSelectedPowerStation(powerStation);
    localStorage.setItem(localStoragePowerStationKey, powerStation);
  }

  return (
    <select
    className="power-station-select"
      onChange={handlePowerStationSelectChange}
      value={selectedPowerStation}
    >
      {!selectedPowerStation ? <option>No PowerStation Available:</option> : ""}
      {availablePowerStations.map(powerStation => (
        <option key={powerStation}>
          {powerStation}
        </option>
      ))}
    </select>
  )
}

export default PowerStationSelector
