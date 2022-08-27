import React from 'react'
import { TimeSeries } from './PowerStationDateData'

interface Props {
  timeSeries: TimeSeries;
}

const TimeSeriesColumn: React.FC<Props> = ({ timeSeries }) => {
  return (
    <div className="column">
      <div className="cell">ver{timeSeries.version}</div>
      <div className="cell">{timeSeries.timestamp}</div>
      {timeSeries.series.map((data, index) => (
        <div key={index} className={`cell ${index < timeSeries.noChangeIndexLimit ? "noChange" : ""}`}>
          {data}
        </div>
      ))}
    </div>
  )
}

export default TimeSeriesColumn