package org.gotaatr.controller;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.gotaatr.services.TimeRecordDTO;
import org.gotaatr.services.TimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
@AllArgsConstructor
@Validated
public class TimeTableController {
    private final TimeService timeService;

    @GetMapping
    public ResponseEntity<List<TimeRecordDTO>> getTimeRecord(
            @RequestParam Optional<Boolean> fetchAll,
            @RequestParam(required = false)
            @Min(value = 0, message = "The minimum value for pageNumber should be greater than 0")
            Integer pageNumber,

            @RequestParam(required = false)
            @Min(value = 1, message = "The minimum value for pageSize should be greater than 1")
            Integer pageSize) {
        return ResponseEntity.ok(
                timeService.fetch(fetchAll, Optional.ofNullable(pageNumber), Optional.ofNullable(pageSize))
        );
    }
}
