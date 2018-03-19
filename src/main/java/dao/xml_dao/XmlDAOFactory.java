package dao.xml_dao;

import dao.DAOFactory;
import dao.dao_interface.*;

public class XmlDAOFactory extends DAOFactory {
    @Override
    public EmployeeDAO getEmployeeDAO() {
        return new XmlEmployeeDAO();
    }

    @Override
    public DepartmentDAO getDepartmentDAO() {
        return new XmlDepartmentDAO();
    }

    @Override
    public ManagerDAO getManagerDAO() {
        return new XmlManagerDAO();
    }

    @Override
    public CustomerDAO getCustomerDAO() {
        return new XmlCustomerDAO();
    }

    @Override
    public ProjectDAO getProjectDAO() {
        return new XmlProjectDAO();
    }

    @Override
    public SprintDAO getSprintDAO() {
        return new XmlSprintDAO();
    }

    @Override
    public TaskDAO getTaskDAO() {
        return new XmlTaskDAO();
    }
}
