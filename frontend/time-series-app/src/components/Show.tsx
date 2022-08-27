import React, { useState } from 'react'
import DateSelector from './selectorComponents/DateSelector';
import PowerStationSelector from './selectorComponents/PowerStationSelector'
import Table from './tableComponents/Table';
import './styles.css'

const Show: React.FC = () => {

  const [selectedPowerStation, setSelectedPowerStation] = useState("");
  const [selectedDate, setSelectedDate] = useState("");
  const [readyToFetchTimeSeries, setReadyToFetchTimeSeries] = useState(false);

  return (
    <div>
      <PowerStationSelector
        selectedPowerStation={selectedPowerStation}
        setSelectedPowerStation={setSelectedPowerStation}
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