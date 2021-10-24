package database;

import bean.Event;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventDAO {

    private final Executor executor;

    public EventDAO(Connection connection) {
        this.executor = new Executor(connection);
    }

    public void insert(Event event) throws SQLException {
        executor.execUpdate(
                "INSERT INTO Events (Message, DateTime) " +
                "VALUES ('" + event.getMsg().replace("'","") + "', '"
                        + dateToSqlDateTimeString(event.getDate()) + "');"
        );
    }

    public Event get(long id) throws SQLException {
        return executor.execQuery("SELECT * FROM Events WHERE Id = " + id, result -> {
            result.next();
            Event event = new Event(result.getDate(3), new SimpleDateFormat());
            event.setId(result.getInt(1));
            event.setMsg(result.getString(2));
            return event;
        });
    }

    public void createTable() throws SQLException {
        executor.execUpdate(
                "CREATE TABLE IF NOT EXISTS Events" +
                "(" +
                "Id INT AUTO_INCREMENT," +
                "Message VARCHAR(256)," +
                "DateTime DATETIME," +
                "PRIMARY KEY (Id)" +
                ");"
        );
    }

    private String dateToSqlDateTimeString(Date date) {
        DateFormat df = new SimpleDateFormat();
        String year = date.toString().substring(25, 29);
        String month = df.format(date).substring(0, 2);
        String day = date.toString().substring(8, 10);
        String time = date.toString().substring(11, 19);
        return year + "-" + month + "-" + day + " " + time;
    }
}
