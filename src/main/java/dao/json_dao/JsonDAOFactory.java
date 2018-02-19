package dao.json_dao;

import dao.DAOFactory;
import dao.dao_interface.*;

public class JsonDAOFactory extends DAOFactory {
    @Override
    public EmployeeDAO getEmployeeDAO() {
        return new JsonEmployeeDAO();
    }

    @Override
    public DepartmentDAO getDepartmentDAO() {
        return new JsonDepartmentDAO();
    }

    @Override
    public ManagerDAO getManagerDAO() {
        return new JsonManagerDAO();
    }

    @Override
    public CustomerDAO getCustomerDAO() {
        return new JsonCustomerDAO();
    }

    @Override
    public ProjectDAO getProjectDAO() {
        return new JsonProjectDAO();
    }

    @Override
    public SprintDAO getSprintDAO() {
        return new JsonSprintDAO();
    }

    @Override
    public TaskDAO getTaskDAO() {
        return new JsonTaskDAO();
    }
}
