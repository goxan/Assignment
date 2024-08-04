package unit;

import org.gotaatr.services.TimeRecordDTO;
import org.gotaatr.services.TimeService;
import org.gotaatr.controller.TimeTableController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TimeTableControllerTest {

    @Mock
    private TimeService timeService;

    @InjectMocks
    private TimeTableController timeTableController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTimeRecord() {
        List<TimeRecordDTO> mockRecords = List.of(new TimeRecordDTO("2023-10-01T10:00:00"));
        when(timeService.fetch(Optional.of(true), Optional.empty(), Optional.empty())).thenReturn(mockRecords);

        ResponseEntity<List<TimeRecordDTO>> response = timeTableController.getTimeRecord(Optional.of(true), null, null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockRecords, response.getBody());
    }

    @Test
    public void testGetTimeRecordFetchAllFalse() {
        List<TimeRecordDTO> mockRecords = List.of(new TimeRecordDTO("2023-10-01T10:00:00"));
        when(timeService.fetch(Optional.of(false), Optional.empty(), Optional.empty())).thenReturn(mockRecords);

        ResponseEntity<List<TimeRecordDTO>> response = timeTableController.getTimeRecord(Optional.of(false), null, null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockRecords, response.getBody());
    }

    @Test
    public void testGetTimeRecordWithPagination() {
        List<TimeRecordDTO> mockRecords = List.of(new TimeRecordDTO("2023-10-01T10:00:00"));
        when(timeService.fetch(Optional.empty(), Optional.of(1), Optional.of(10))).thenReturn(mockRecords);

        ResponseEntity<List<TimeRecordDTO>> response = timeTableController.getTimeRecord(Optional.empty(), 1, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockRecords, response.getBody());
    }

    @Test
    public void testGetTimeRecordNoParams() {
        List<TimeRecordDTO> mockRecords = List.of(new TimeRecordDTO("2023-10-01T10:00:00"));
        when(timeService.fetch(Optional.empty(), Optional.empty(), Optional.empty())).thenReturn(mockRecords);

        ResponseEntity<List<TimeRecordDTO>> response = timeTableController.getTimeRecord(Optional.empty(), null, null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockRecords, response.getBody());
    }
}