package com.reg.time_series.service;

import com.reg.time_series.entity.PowerStationDateData;
import com.reg.time_series.entity.TimeSeriesEntity;
import com.reg.time_series.model.TimeSeriesInputModel;
import com.reg.time_series.repository.PowerStationDayDataRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles(profiles = "test")
class TimeSeriesServiceImplTest {

    @MockBean
    private PowerStationDayDataRepository mockPowerStationDayDataRepository;

    @Captor
    ArgumentCaptor<PowerStationDateData> powerStationDayDataArgumentCaptor;

    @Autowired
    private TimeSeriesService timeSeriesService;

    @Test
    public void processTimeSeries_newTimeSeries() {
        when(mockPowerStationDayDataRepository.findByPowerStationAndDate(anyString(), any())).thenReturn(null);
        timeSeriesService.processTimeSeries(getTimeSeriesInputModel_ps_83_20210628_032953());

        verify(mockPowerStationDayDataRepository).save(powerStationDayDataArgumentCaptor.capture());
        PowerStationDateData capturedPowerStationDateData = powerStationDayDataArgumentCaptor.getValue();
        assertEquals(1, capturedPowerStationDateData.getLatestTimeSeries().getVersion());
        assertNull(capturedPowerStationDateData.getPreviousTimeSeriesList());
        assertEquals(LocalDateTime.of(2021, 6, 28, 3, 29, 53),
                capturedPowerStationDateData.getLatestTimeSeries().getTimestamp());
        assertEquals(getSeries_ps_83_20210628_032953(), capturedPowerStationDateData.getLatestTimeSeries().getSeries());
    }

    @Test
    public void processTimeSeries_mergeTimeSeries() {
        PowerStationDateData powerStationDateData = getPowerStationDayData();
        when(mockPowerStationDayDataRepository.findByPowerStationAndDate("Naper\\u0151m\\u0171 2021 Kft. Iborfia",
                LocalDate.of(2021, 6, 28))).thenReturn(powerStationDateData);
        timeSeriesService.processTimeSeries(getTimeSeriesInputModel_ps_83_20210628_115951());

        verify(mockPowerStationDayDataRepository).save(powerStationDayDataArgumentCaptor.capture());
        PowerStationDateData capturedPowerStationDateData = powerStationDayDataArgumentCaptor.getValue();
        TimeSeriesEntity latestTimeSeries = capturedPowerStationDateData.getLatestTimeSeries();
        assertEquals(getMergedSeries(), latestTimeSeries.getSeries());
        assertEquals(2, latestTimeSeries.getVersion());
        assertEquals(getSeries_ps_83_20210628_032953(),
                capturedPowerStationDateData.getPreviousTimeSeriesList().get(0).getSeries());
    }

    @Test
    public void processTimeSeries_mergeTimeSeries_noUpdateBecauseTooLate() {
        PowerStationDateData powerStationDateData = getPowerStationDayData();
        when(mockPowerStationDayDataRepository.findByPowerStationAndDate("Naper\\u0151m\\u0171 2021 Kft. Iborfia",
                LocalDate.of(2021, 6, 28))).thenReturn(powerStationDateData);
        TimeSeriesInputModel timeSeriesInputModel = getTimeSeriesInputModel_ps_83_20210628_115951();
        timeSeriesInputModel.setTimestamp(convertBudToUtcLocalDateTime("2021-06-28T22:35:53"));
        timeSeriesService.processTimeSeries(timeSeriesInputModel);

        verify(mockPowerStationDayDataRepository).save(powerStationDayDataArgumentCaptor.capture());
        PowerStationDateData capturedPowerStationDateData = powerStationDayDataArgumentCaptor.getValue();
        assertEquals(getSeries_ps_83_20210628_032953(), capturedPowerStationDateData.getLatestTimeSeries().getSeries());
    }

    @Test
    public void processTimeSeries_mergeTimeSeries_differentSizeOfSeries() {
        PowerStationDateData powerStationDateData = getPowerStationDayData();
        when(mockPowerStationDayDataRepository.findByPowerStationAndDate("Naper\\u0151m\\u0171 2021 Kft. Iborfia",
                LocalDate.of(2021, 6, 28))).thenReturn(powerStationDateData);
        TimeSeriesInputModel timeSeriesInputModel = getTimeSeriesInputModel_ps_83_20210628_115951();
        timeSeriesInputModel.setSeries(getShorterSeries());
        assertThrows(IllegalArgumentException.class, () -> timeSeriesService.processTimeSeries(timeSeriesInputModel));
    }

    private PowerStationDateData getPowerStationDayData() {
        TimeSeriesEntity timeSeriesEntity = TimeSeriesEntity.builder()
                .version(1)
                .period("PT15M")
                .timestamp(LocalDateTime.of(2021, 6, 28, 3, 29, 53))
                .series(getSeries_ps_83_20210628_032953())
                .build();

        return PowerStationDateData.builder()
                .powerStation("Naper\\u0151m\\u0171 2021 Kft. Iborfia")
                .date(LocalDate.of(2021, 6, 28))
                .zone(TimeZone.getTimeZone("Europe/Budapest"))
                .latestTimeSeries(timeSeriesEntity)
                .previousTimeSeriesList(new ArrayList<>())
                .build();
    }

    private TimeSeriesInputModel getTimeSeriesInputModel_ps_83_20210628_032953() {
        return TimeSeriesInputModel.builder()
                .powerStation("Naper\\u0151m\\u0171 2021 Kft. Iborfia")
                .date(LocalDate.of(2021, 6, 28))
                .zone(TimeZone.getTimeZone("Europe/Budapest"))
                .period("PT15M")
                .timestamp(LocalDateTime.of(2021, 6, 28, 3, 29, 53))
                .series(getSeries_ps_83_20210628_032953())
                .build();
    }

    private List<Integer> getSeries_ps_83_20210628_032953() {
        return List.of(0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                3160,
                6861,
                11336,
                16806,
                23171,
                34751,
                57154,
                81201,
                105757,
                131570,
                157159,
                182121,
                207386,
                232296,
                256428,
                279393,
                301824,
                323420,
                344083,
                363067,
                380667,
                397886,
                413210,
                426305,
                437351,
                446688,
                454164,
                460393,
                464035,
                465306,
                466377,
                463921,
                458288,
                451457,
                445296,
                437518,
                428141,
                418211,
                406459,
                393051,
                378487,
                361539,
                343178,
                324647,
                304919,
                284240,
                261583,
                237907,
                213367,
                188902,
                163630,
                137883,
                112914,
                89161,
                65772,
                43821,
                28602,
                22205,
                16330,
                11027,
                6291,
                2668,
                561,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0);
    }

    private TimeSeriesInputModel getTimeSeriesInputModel_ps_83_20210628_115951() {
        return TimeSeriesInputModel.builder()
                .powerStation("Naper\\u0151m\\u0171 2021 Kft. Iborfia")
                .date(LocalDate.of(2021, 6, 28))
                .zone(TimeZone.getTimeZone("Europe/Budapest"))
                .period("PT15M")
                .timestamp(convertBudToUtcLocalDateTime("2021-06-28T11:59:51"))
                .series(getSeries_ps_83_20210628_115951())
                .build();
    }

    private LocalDateTime convertBudToUtcLocalDateTime(String dateTimeText) {
        // This conversion is necessary because hardcoded dateTimes will not work if DST changes
        return LocalDateTime.parse(dateTimeText)
                .atZone(TimeZone.getTimeZone("Europe/Budapest").toZoneId())
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();
    }

    private List<Integer> getSeries_ps_83_20210628_115951() {
        return List.of(0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                3698,
                7698,
                12464,
                17933,
                24336,
                36210,
                58564,
                82951,
                110836,
                136593,
                163109,
                188606,
                212740,
                237128,
                262155,
                285243,
                308034,
                329233,
                350596,
                369573,
                386250,
                402759,
                417616,
                431041,
                442817,
                453011,
                457598,
                461497,
                464096,
                465306,
                466606,
                464007,
                462817,
                456700,
                449900,
                442227,
                433577,
                423777,
                412791,
                400232,
                386274,
                370209,
                352441,
                333398,
                313435,
                292316,
                269569,
                243835,
                219916,
                192625,
                166633,
                137941,
                115940,
                90994,
                66645,
                43757,
                28334,
                21932,
                15951,
                10673,
                6129,
                2589,
                555,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0);
    }

    private List<Integer> getMergedSeries() {
        return List.of(0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                3160,
                6861,
                11336,
                16806,
                23171,
                34751,
                57154,
                81201,
                105757,
                131570,
                157159,
                182121,
                207386,
                232296,
                256428,
                279393,
                301824,
                323420,
                344083,
                363067,
                380667,
                397886,
                413210,
                426305,
                437351,
                446688,
                454164,
                460393,
                464035,
                465306,
                466377,
                463921,
                458288,
                451457,
                449900,
                442227,
                433577,
                423777,
                412791,
                400232,
                386274,
                370209,
                352441,
                333398,
                313435,
                292316,
                269569,
                243835,
                219916,
                192625,
                166633,
                137941,
                115940,
                90994,
                66645,
                43757,
                28334,
                21932,
                15951,
                10673,
                6129,
                2589,
                555,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0);
    }

    private List<Integer> getShorterSeries() {
        return List.of(0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                3698,
                7698,
                12464,
                17933,
                24336,
                36210,
                58564,
                82951,
                110836,
                136593,
                163109,
                188606,
                212740,
                237128,
                262155,
                285243,
                308034,
                329233,
                350596,
                369573,
                386250,
                402759,
                417616,
                431041,
                442817,
                453011,
                457598,
                461497,
                464096,
                465306,
                466606,
                464007,
                462817,
                456700,
                449900,
                442227,
                433577,
                423777,
                412791,
                400232,
                386274,
                370209,
                352441,
                333398,
                313435,
                292316,
                269569,
                243835,
                219916,
                192625,
                166633,
                137941,
                115940,
                90994,
                66645,
                43757,
                28334,
                21932,
                15951,
                10673,
                6129,
                2589,
                555,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0);
    }

}