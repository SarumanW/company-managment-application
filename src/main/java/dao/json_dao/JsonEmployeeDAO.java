package dao.json_dao;

import dao.dao_interface.EmployeeDAO;
import domain.Employee;

public class JsonEmployeeDAO implements EmployeeDAO {
    @Override
    public boolean insertEmployee(Employee employee) {
        return false;
    }

    @Override
    public Employee findEmployee(long key) {
        return null;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        return false;
    }

    @Override
    public boolean deleteEmployee(long key) {
        return false;
    }
}
