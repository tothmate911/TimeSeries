import React from 'react'

interface Props {
  period: string;
  length: number
}

const PeriodColumn: React.FC<Props> = ({ period, length }) => {

  const arr = /\d./.exec(period);
  let periodNum: number = 0;
  if (arr !== null) {
    periodNum = +arr[0];
  }

  const getHHmmFormatFromMinutes = (totalMinutes: number) => {
    const minutes: number = totalMinutes % 60;
    const hours: number = (totalMinutes - minutes) / 60;
    return `${hours < 10 ? "0" : ""}${hours}:${minutes < 10 ? "0" : ""}${minutes}`;
  }

  const cells = [];
  for (let i = 1; i <= length; i++) {
    cells.push(<div className="cell" key={i}>{getHHmmFormatFromMinutes(i * periodNum)}</div>);
  }

  return (
    <div className="column">
      <div className="cell">period end</div>
      <div className="cell"></div>
      {cells}
    </div>
  )
}

export default PeriodColumn