import React, { useState } from 'react'
import DateSelector from './DateSelector';
import PowerStationSelector from './PowerStationSelector'
import Table from './Table';

const Show: React.FC = () => {

    const [selectedPowerStation, setSelectedPowerStation] = useState("");
    const [selectedDate, setSelectedDate] = useState("");

  return (
    <div>
        <PowerStationSelector setSelectedPowerStation={setSelectedPowerStation}/>
        <DateSelector selectedPowerStation={selectedPowerStation} setSelectedDate={setSelectedDate}/>
        <Table selectedPowerStation={selectedPowerStation} selectedDate={selectedDate} />
    </div>
  )
}

export default Show