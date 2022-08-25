import axios from 'axios';
import React, { useEffect } from 'react'

const localStoragePowerStationKey: string = "localStoragePowerStationKey";

interface Props {
  selectedPowerStation: string;
  setSelectedPowerStation: React.Dispatch<React.SetStateAction<string>>;

  availablePowerStations: string[];
  setAvailablePowerStations: React.Dispatch<React.SetStateAction<string[]>>;
}

const PowerStationSelector: React.FC<Props> = ({
  selectedPowerStation, setSelectedPowerStation,
  availablePowerStations, setAvailablePowerStations
}) => {

  // const [availablePowerStations, setAvailablePowerStations] = useState<string[]>([]);

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
    if (availablePowerStations.length !== 0) {
      setSelectedPowerStation(localStorage.getItem(localStoragePowerStationKey) || availablePowerStations[0]);
    }
  }, [availablePowerStations, setSelectedPowerStation]);

  const handlePowerStationSelectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    let powerStation = e.target.value;
    setSelectedPowerStation(powerStation);
    localStorage.setItem(localStoragePowerStationKey, powerStation);
  }

  return (
    <select
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
