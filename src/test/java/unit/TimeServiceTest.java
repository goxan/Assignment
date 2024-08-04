package unit;

import org.gotaatr.services.TimeRecordDTO;
import org.gotaatr.services.TimeService;
import org.gotaatr.database.TimeRecordEntity;
import org.gotaatr.database.TimeRecordRepository;
import org.gotaatr.infra.properties.TimeServiceProperties;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TimeServiceTest {

    @Mock
    private TimeServiceProperties timeServiceProperties;

    @Mock
    private TimeRecordRepository timeRepository;

    @InjectMocks
    private TimeService timeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFetchAll() {
        List<TimeRecordEntity> mockEntities = Instancio.ofList(TimeRecordEntity.class).size(50).create();
        when(timeRepository.fetchAll()).thenReturn(mockEntities);
        List<TimeRecordDTO> result = timeService.fetch(Optional.of(true), Optional.empty(), Optional.empty());

        assertEquals(50, result.size());
    }

    @Test
    public void testFetchWithPagination() {
        List<TimeRecordEntity> mockEntities = Instancio.ofList(TimeRecordEntity.class).size(10).create();
        when(timeServiceProperties.getDefaultPageNumber()).thenReturn(0);
        when(timeServiceProperties.getDefaultPageSize()).thenReturn(10);
        when(timeRepository.fetch(0, 10)).thenReturn(mockEntities);

        List<TimeRecordDTO> result = timeService.fetch(Optional.empty(), Optional.of(0), Optional.of(10));
        assertEquals(10, result.size());
    }

    @Test
    public void testFetchAllFalse() {
        List<TimeRecordEntity> mockEntities = List.of(new TimeRecordEntity("3", "2023-10-01T10:00:00"));
        when(timeRepository.fetchAll()).thenReturn(mockEntities);

        List<TimeRecordDTO> result = timeService.fetch(Optional.of(false), Optional.empty(), Optional.empty());
        assertEquals(1, result.size());
    }

    @Test
    public void testFetchWithPageNumberOnly() {
        List<TimeRecordEntity> mockEntities = List.of(new TimeRecordEntity("4", "2023-10-01T10:00:00"));
        when(timeServiceProperties.getDefaultPageSize()).thenReturn(10);
        when(timeRepository.fetch(1, 10)).thenReturn(mockEntities);

        List<TimeRecordDTO> result = timeService.fetch(Optional.empty(), Optional.of(1), Optional.empty());

        assertEquals(1, result.size());
        assertEquals("2023-10-01T10:00:00", result.get(0).time());
    }

    @Test
    public void testFetchWithPageSizeOnly() {
        List<TimeRecordEntity> mockEntities = List.of(new TimeRecordEntity("5", "2023-10-01T10:00:00"));
        when(timeServiceProperties.getDefaultPageNumber()).thenReturn(0);
        when(timeRepository.fetch(0, 5)).thenReturn(mockEntities);

        List<TimeRecordDTO> result = timeService.fetch(Optional.empty(), Optional.empty(), Optional.of(5));

        assertEquals(1, result.size());
        assertEquals("2023-10-01T10:00:00", result.get(0).time());
    }

    @Test
    public void testFetchAllTrueWithPagination() {
        List<TimeRecordEntity> mockEntities = List.of(new TimeRecordEntity("6", "2023-10-01T10:00:00"));
        when(timeRepository.fetchAll()).thenReturn(mockEntities);

        List<TimeRecordDTO> result = timeService.fetch(Optional.of(true), Optional.of(1), Optional.of(10));

        assertEquals(1, result.size());
        assertEquals("2023-10-01T10:00:00", result.get(0).time());
    }

    @Test
    public void testFetchAllFalseWithPagination() {
        List<TimeRecordEntity> mockEntities = List.of(new TimeRecordEntity("7", "2023-10-01T10:00:00"));
        when(timeRepository.fetch(1, 10)).thenReturn(mockEntities);

        List<TimeRecordDTO> result = timeService.fetch(Optional.of(false), Optional.of(1), Optional.of(10));

        assertEquals(1, result.size());
        assertEquals("2023-10-01T10:00:00", result.get(0).time());
    }

    @Test
    public void testFetchAllTrueNoParams() {
        List<TimeRecordEntity> mockEntities = List.of(new TimeRecordEntity("8", "2023-10-01T10:00:00"));
        when(timeRepository.fetchAll()).thenReturn(mockEntities);

        List<TimeRecordDTO> result = timeService.fetch(Optional.of(true), Optional.empty(), Optional.empty());

        assertEquals(1, result.size());
        assertEquals("2023-10-01T10:00:00", result.get(0).time());
    }

    @Test
    public void testFetchAllFalseNoParams() {
        List<TimeRecordEntity> mockEntities = List.of(new TimeRecordEntity("9", "2023-10-01T10:00:00"));
        when(timeServiceProperties.getDefaultPageNumber()).thenReturn(0);
        when(timeServiceProperties.getDefaultPageSize()).thenReturn(10);
        when(timeRepository.fetchAll()).thenReturn(mockEntities);

        List<TimeRecordDTO> result = timeService.fetch(Optional.of(false), Optional.empty(), Optional.empty());

        assertEquals(1, result.size());
        assertEquals("2023-10-01T10:00:00", result.get(0).time());
    }
}