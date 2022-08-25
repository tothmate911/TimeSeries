import internal from "stream";

export interface PowerStationDateData {
    latestTimeSeries: TimeSeries;
    previousTimeSeriesList: TimeSeries[];
}

export interface TimeSeries {
    id: number;
    series: number[];
    timestamp: string;
    version: number;
    period: string;
    noChangeIndexLimit: number;
}