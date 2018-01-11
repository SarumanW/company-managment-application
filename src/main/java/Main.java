import dao.DAOFactory;
import dao.dao_interface.CustomerDAO;
import dao.dao_interface.EmployeeDAO;
import dao.dao_interface.TaskDAO;
import dao.oracle_dao.OracleDAOFactory;
import domain.Employee;
import domain.Project;
import domain.Task;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) throws SQLException, ParseException {
        System.out.println("Compiled");

        DAOFactory daoFactory = new OracleDAOFactory();
        TaskDAO taskDAO = daoFactory.getTaskDAO();
        EmployeeDAO employeeDAO = daoFactory.getEmployeeDAO();

        Employee employee1 = new Employee("Leila", "Kentuki", 5000);
        Employee employee2 = new Employee("Mary", "MacDonald", 6000);
        Task task = new Task("Creating picture", 40000);
        task.addEmployee(employee1);
        task.addEmployee(employee2);

        employeeDAO.insertEmployee(employee1);
        employeeDAO.insertEmployee(employee2);
        taskDAO.insertTask(task);
    }

}
