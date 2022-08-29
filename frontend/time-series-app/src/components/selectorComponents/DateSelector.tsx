import axios from 'axios';
import React, { useEffect, useState } from 'react'

const localStorageDateKey: string = "localStorageDateKey";

interface Props {
  selectedPowerStation: string;

  selectedDate: string;
  setSelectedDate: React.Dispatch<React.SetStateAction<string>>;

  setReadyToFetchTimeSeries: React.Dispatch<React.SetStateAction<boolean>>
}

const DateSelector: React.FC<Props> = ({
  selectedPowerStation,
  selectedDate,
  setSelectedDate,
  setReadyToFetchTimeSeries
}) => {

  const [availableDates, setAvailableDates] = useState<string[]>([]);

  useEffect(() => {
    if (!selectedPowerStation) {
      return;
    }
    axios.get(`/available-dates/${selectedPowerStation}`)
      .then((response) => {
        const dates: string[] = response.data;
        setAvailableDates(dates);
      })
      .catch((error) => {
        console.log(error);
      });
  }, [selectedPowerStation, setAvailableDates]);

  useEffect(() => {
    const savedDate = localStorage.getItem(localStorageDateKey);
    if (savedDate !== null && availableDates.includes(savedDate)) {
      setSelectedDate(savedDate);
    } else {
      const firstAvailableDate = availableDates[0];
      setSelectedDate(firstAvailableDate);
      localStorage.setItem(localStorageDateKey, firstAvailableDate);
    }
  }, [availableDates, setSelectedDate]);

  useEffect(() => {
    if (availableDates.includes(selectedDate)) {
      setReadyToFetchTimeSeries(true);
    }
  }, [selectedDate, availableDates, setReadyToFetchTimeSeries])

  const handleDateSelectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    let date = e.target.value;
    setSelectedDate(date);
    localStorage.setItem(localStorageDateKey, date);
  }

  return (
    <select
      onChange={handleDateSelectChange}
      value={selectedDate}
    >
      {!selectedDate ? <option>No Date Available:</option> : ""}
      {availableDates.map(date => (
        <option key={date}>
          {date}
        </option>
      ))}
    </select>
  )
}

export default DateSelector