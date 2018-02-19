package dao.dao_interface;

import domain.Employee;

public interface EmployeeDAO {
    boolean insertEmployee(Employee employee);
    Employee findEmployee(long key);
    boolean updateEmployee(Employee employee);
    boolean deleteEmployee(long key);
}
