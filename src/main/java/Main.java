import dao.DAOFactory;
import dao.dao_interface.CustomerDAO;
import dao.dao_interface.DepartmentDAO;
import dao.dao_interface.EmployeeDAO;
import dao.dao_interface.TaskDAO;
import dao.oracle_dao.OracleDAOFactory;
import domain.Department;
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
        DepartmentDAO departmentDAO = daoFactory.getDepartmentDAO();

//        Department department = new Department("PaintingDep");
//
//        departmentDAO.insertDepartment(department);
//
//        Employee employee1 = new Employee("Leila", "Kentuki", 5000, department);
//        Employee employee2 = new Employee("Mary", "MacDonald", 6000, department);
//
//        employeeDAO.insertEmployee(employee1);
//        employeeDAO.insertEmployee(employee2);
//
//        Task task = new Task("Creating picture", 40000);
//        task.addEmployee(employee1);
//        task.addEmployee(employee2);
//
//        taskDAO.insertTask(task);

//        Task task = taskDAO.findTask(19);
//        task.setEstimate(800);
//        taskDAO.updateTask(task);

//        System.out.println(department);
//        System.out.println(task);
    }

}
