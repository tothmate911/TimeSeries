import axios from 'axios';
import React, { useEffect, useState } from 'react'
import PeriodColumn from './PeriodColumn'

interface Props {
    selectedPowerStation: string,
    selectedDate: string
}

const Table: React.FC<Props> = ({selectedPowerStation, selectedDate}) => {

    const [timeSeries, setTimeSeries] = useState();

    useEffect (() => {
        if (!selectedPowerStation || !selectedDate) {
            return;
        }
        axios.get(`/power-station-day-data?power-station=${selectedPowerStation}&date=${selectedDate}`)
        .then((response) => {
            setTimeSeries(response.data);
        })
        .catch((error) => {
            console.log(error);
        })
    }, [selectedPowerStation, selectedDate]);

  return (
    <div>
          {selectedPowerStation} - {selectedDate}
          <PeriodColumn />
          
    </div>
  )
}

export default Table