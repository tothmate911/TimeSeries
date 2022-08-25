import axios from 'axios';
import React, { useEffect, useState } from 'react'
import PeriodColumn from './PeriodColumn'
import { PowerStationDateData, TimeSeries } from './PowerStationDateData';
import TimeSeriesColumn from './TimeSeriesColumn';

interface Props {
  selectedPowerStation: string;
  selectedDate: string;

  readyToFetchTimeSeries: boolean;
  setReadyToFetchTimeSeries: React.Dispatch<React.SetStateAction<boolean>>;
}

const Table: React.FC<Props> = ({
  selectedPowerStation,
  selectedDate,
  readyToFetchTimeSeries,
  setReadyToFetchTimeSeries
}) => {

  const [timeSeriesArr, setTimeSeriesArr] = useState<TimeSeries[]>([]);

  useEffect(() => {
    if (!selectedPowerStation || !selectedDate || !readyToFetchTimeSeries) {
      return;
    }
    setReadyToFetchTimeSeries(false);
    axios.get(`/power-station-date-data?power-station=${selectedPowerStation}&date=${selectedDate}`)
      .then((response) => {
        const powerStationDateData: PowerStationDateData = (response.data);
        setTimeSeriesArr([...powerStationDateData.previousTimeSeriesList, powerStationDateData.latestTimeSeries])
      })
      .catch((error) => {
        console.log(error);
      })
  }, [selectedPowerStation, selectedDate, readyToFetchTimeSeries, setReadyToFetchTimeSeries]);

  return (
    <div>
      <div>{
        selectedPowerStation ?
          `${selectedPowerStation} - ${selectedDate}`
          : "Please upload TimeSeries"}</div>
      {timeSeriesArr.length !== 0 ? (
        <div className="table-container">
          <PeriodColumn period={timeSeriesArr[0].period} length={timeSeriesArr[0].series.length} />
          {timeSeriesArr.map(timeSeries => (
            <TimeSeriesColumn timeSeries={timeSeries} key={timeSeries.id} />
          ))}
        </div>
      ) : <div />}


    </div>
  )
}

export default Table