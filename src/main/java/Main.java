import dao.DAOFactory;
import dao.dao_interface.CustomerDAO;
import dao.dao_interface.DepartmentDAO;
import dao.dao_interface.EmployeeDAO;
import dao.dao_interface.TaskDAO;
import dao.json_dao.JsonDAOFactory;
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

        DAOFactory daoFactory = new JsonDAOFactory();
        CustomerDAO customerDAO = daoFactory.getCustomerDAO();

        


    }

}
