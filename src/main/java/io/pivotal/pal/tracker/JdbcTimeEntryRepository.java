package io.pivotal.pal.tracker;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;


public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;
    private TimeEntryRepository timeEntryRepository;

    public JdbcTimeEntryRepository(DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement("INSERT INTO time_entries (project_id, user_id, date, hours) " +
                                        "VALUES (?, ?, ?, ?)", RETURN_GENERATED_KEYS);
                        ps.setLong(1, timeEntry.getProjectId());
                        ps.setLong(2, timeEntry.getUserId());
                        ps.setDate(3, Date.valueOf(timeEntry.getDate()));
                        ps.setInt(4, timeEntry.getHours());
                        return ps;
                    }
                }, keyHolder);

        timeEntry.setId(keyHolder.getKey().longValue());

        return timeEntry;

    }

    private RowMapper<TimeEntry> mapper = (rs, num) -> {
        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setId(rs.getLong("id"));
        timeEntry.setProjectId(rs.getLong("project_id"));
        timeEntry.setUserId(rs.getLong("user_id"));
        timeEntry.setDate(rs.getDate("date").toLocalDate());
        timeEntry.setHours(rs.getInt("hours"));

        return timeEntry;
    };

    @Override
    public TimeEntry find(long id) {
        try{
            return  jdbcTemplate.queryForObject("select * from time_entries where id = ? limit 1", mapper, id);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query("select * from time_entries", mapper);
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry te = find(id);
        if(te != null ) {
            jdbcTemplate.update("update time_entries set project_id = ? ,user_id = ? , date = ? , hours = ? where id = ? ", timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours(), id);
            return find(id);
        }
        else {
            return null;
        }
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("delete from time_entries where id = ?", id);
    }
}
