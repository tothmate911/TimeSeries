import React, { useState } from 'react'
import DateSelector from './selectorComponents/DateSelector';
import PowerStationSelector from './selectorComponents/PowerStationSelector'
import Table from './tableComponents/Table';
import './styles.css'

interface Props {
  availablePowerStations: string[];
  setAvailablePowerStations: React.Dispatch<React.SetStateAction<string[]>>
}

const Show: React.FC<Props> = ({ availablePowerStations, setAvailablePowerStations }) => {

  const [selectedPowerStation, setSelectedPowerStation] = useState("");
  const [selectedDate, setSelectedDate] = useState("");
  const [readyToFetchTimeSeries, setReadyToFetchTimeSeries] = useState(false);

  return (
    <div>
      <PowerStationSelector
        selectedPowerStation={selectedPowerStation}
        setSelectedPowerStation={setSelectedPowerStation}
        availablePowerStations={availablePowerStations}
        setAvailablePowerStations={setAvailablePowerStations}
      />
      <DateSelector
        selectedPowerStation={selectedPowerStation}
        selectedDate={selectedDate}
        setSelectedDate={setSelectedDate}
        setReadyToFetchTimeSeries={setReadyToFetchTimeSeries}
      />
      <Table
        selectedPowerStation={selectedPowerStation}
        selectedDate={selectedDate}
        readyToFetchTimeSeries={readyToFetchTimeSeries}
        setReadyToFetchTimeSeries={setReadyToFetchTimeSeries}
      />

    </div>
  )
}

export default Show