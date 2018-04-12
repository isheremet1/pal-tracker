package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class TimeEntryControllerV2 {


    private TimeEntryRepository timeEntryRepository;

    public TimeEntryControllerV2(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping(value = "/time-entries", consumes = "application/io.pivotal.pal.tracker.v2+json", produces = "application/io.pivotal.pal.tracker.v2+json")
    public ResponseEntity create(@RequestBody TimeEntry timeEntry) {
        return new ResponseEntity(timeEntryRepository.create(timeEntry), HttpStatus.CREATED);
    }

    @GetMapping(value = "/time-entries/{id}", consumes = "application/io.pivotal.pal.tracker.v2+json", produces = "application/io.pivotal.pal.tracker.v2+json")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        TimeEntry timeEntry = timeEntryRepository.find(id);

        return new ResponseEntity(timeEntry, timeEntry != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/time-entries", consumes = "application/io.pivotal.pal.tracker.v2+json", produces = "application/io.pivotal.pal.tracker.v2+json")
    public ResponseEntity<List<TimeEntry>> list() {
        return new ResponseEntity(timeEntryRepository.list(), HttpStatus.OK);
    }

    @PutMapping(value = "/time-entries/{id}", consumes = "application/io.pivotal.pal.tracker.v2+json", produces = "application/io.pivotal.pal.tracker.v2+json")
    public ResponseEntity update(@PathVariable long id,@RequestBody TimeEntry timeEntry) {

//        if (timeEntryRepository.find(id) == null)
//            return new ResponseEntity(null, HttpStatus.NOT_FOUND);

        TimeEntry timeEntryUpdated = timeEntryRepository.update(id, timeEntry);

        return new ResponseEntity(timeEntryUpdated, timeEntryUpdated != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/time-entries/{id}", consumes = "application/io.pivotal.pal.tracker.v2+json", produces = "application/io.pivotal.pal.tracker.v2+json")
    public ResponseEntity<TimeEntry> delete(@PathVariable long id) {
        timeEntryRepository.delete(id);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);

         //(timeEntryRepository.find(id) == null)
           // return new ResponseEntity(null, HttpStatus.NOT_FOUND);



    }

}
