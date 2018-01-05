import connections.OracleConnection;
import dao.OracleDepartmentDAO;
import dao.OracleEmployeeDAO;
import domain.Department;
import domain.Employee;
import domain.Project;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) throws SQLException, ParseException {
        System.out.println("Compiled");

        Project project = new Project();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        project.setEnd(toCalendar(format.parse("13-05-99")));

        System.out.println(project.getEnd().getTime());
    }

    public static Calendar toCalendar(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
