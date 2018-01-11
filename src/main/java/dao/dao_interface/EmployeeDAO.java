package dao.dao_interface;

import domain.Employee;

import java.util.List;

public interface EmployeeDAO {
    boolean insertEmployee(Employee employee);
    Employee findEmployee(long key);
    boolean updateEmployee(Employee employee);
    boolean deleteEmployee(long key);
}
