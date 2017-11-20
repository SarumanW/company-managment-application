package dao;

import domain.Employee;

import java.util.List;

public interface EmployeeDAO {
    public boolean insertEmployee(Employee employee);
    public Employee findEmployee(long key);
    public boolean updateEmployee(Employee employee);
    public boolean deleteEmployee(long key);
    public List<Employee> getAllEmployees();
}
