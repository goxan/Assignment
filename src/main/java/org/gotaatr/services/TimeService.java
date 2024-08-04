package org.gotaatr.services;

import lombok.AllArgsConstructor;
import org.gotaatr.database.TimeRecordEntity;
import org.gotaatr.database.TimeRecordRepository;
import org.gotaatr.infra.properties.TimeServiceProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TimeService {
    private final TimeServiceProperties timeServiceProperties;
    private final TimeRecordRepository timeRepository;

    public List<TimeRecordDTO> fetch(Optional<Boolean> fetchAll,
                                     Optional<Integer> pageNumber,
                                     Optional<Integer> pageSize) {
        List<TimeRecordEntity> timeRecordEntities;
        int defaultPageNumber = timeServiceProperties.getDefaultPageNumber();
        int defaultPageSize = timeServiceProperties.getDefaultPageSize();
        boolean fetchIsPresentAndEqTrue = fetchAll.isPresent() && fetchAll.get();
        boolean allParamsAreEmpty = (fetchAll.isEmpty() || !fetchAll.get()) && pageNumber.isEmpty() && pageSize.isEmpty();

        timeRecordEntities = (fetchIsPresentAndEqTrue || allParamsAreEmpty)
                ? timeRepository.fetchAll()
                : timeRepository.fetch(pageNumber.orElse(defaultPageNumber), pageSize.orElse(defaultPageSize)
        );
        return timeRecordEntities
                .stream()
                .map(row -> new TimeRecordDTO(row.time()))
                .toList();

    }

}
