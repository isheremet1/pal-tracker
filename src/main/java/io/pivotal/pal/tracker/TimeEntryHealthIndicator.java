package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TimeEntryHealthIndicator implements HealthIndicator{

    private final TimeEntryRepository timeEntryRepository;
    private static final int MIN_ENTRIES = 5;


    public TimeEntryHealthIndicator(TimeEntryRepository timeEntryRepository){
        this.timeEntryRepository = timeEntryRepository;
    }
    @Override
    public Health health() {

        if(timeEntryRepository.list().size() < MIN_ENTRIES)
            return Health.up().build();


        return Health.down().build();
    }
}
