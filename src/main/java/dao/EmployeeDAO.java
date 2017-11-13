package dao;

import domain.Employee;

import java.util.List;

public interface EmployeeDAO {
    public int insertEmployee();
    public Employee findEmployee(int key);
    public boolean updateEmployee(Employee employee);
    public boolean deleteEmployee(Employee employee);
    public List<Employee> getAllEmployees();
}
