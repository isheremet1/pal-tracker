package io.pivotal.pal.tracker;

import java.util.*;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    Map<Long, TimeEntry> timeEntryMap;

    public InMemoryTimeEntryRepository(){

        this.timeEntryMap = new HashMap<>();

    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {

        if (timeEntry.getId() <= 0)
            timeEntry.setId(getNextId());

        timeEntryMap.put(timeEntry.getId(), timeEntry);

        return timeEntry;
    }

    @Override
    public TimeEntry find(long id) {
        return timeEntryMap.get(id);
    }

    @Override
    public List<TimeEntry> list() {

        return new ArrayList<TimeEntry>(timeEntryMap.values());
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        if (timeEntryMap.containsKey(id)) {
            timeEntry.setId(id);
            timeEntryMap.put(id, timeEntry);
            return timeEntry;
        }
        return null;
    }

    @Override
    public void delete(long id) {
        if(timeEntryMap.containsKey(id))
            timeEntryMap.remove(id);

    }

    private Long getNextId() {
        return new Long(timeEntryMap.size() + 1);
    }

}
