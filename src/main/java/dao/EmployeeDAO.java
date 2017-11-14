package dao;

import domain.Employee;

import java.util.List;

public interface EmployeeDAO {
    public boolean insertEmployee(Employee employee);
    public Employee findEmployee(int key);
    public boolean updateEmployee(Employee employee);
    public boolean deleteEmployee(int key);
    public List<Employee> getAllEmployees();
}
