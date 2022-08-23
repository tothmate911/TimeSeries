import axios from 'axios';
import React, { useEffect, useState } from 'react'

interface Props {
    selectedPowerStation: string,
    setSelectedDate: React.Dispatch<React.SetStateAction<string>>
}

const DateSelector: React.FC<Props> = ({selectedPowerStation, setSelectedDate}) => {

    const [availableDates, setAvailableDates] = useState([]);

    useEffect(() => {
        if (!selectedPowerStation) {
            return;
        }
        axios.get(`/available-dates/${selectedPowerStation}`)
        .then((response) => {
          setAvailableDates(response.data);
      })
        .catch((error) => {
          console.log(error);
      });
      }, [selectedPowerStation])
      
  return (
    <select 
        onChange={e => setSelectedDate(e.target.value)}
      >
        {availableDates.map(date => (
          <option key={date}>
            {date}
          </option>
        ))}
      </select>
  )
}

export default DateSelector