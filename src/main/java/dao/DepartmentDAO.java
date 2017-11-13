package dao;

import domain.Department;

import java.util.List;

public interface DepartmentDAO {
    public int insertDepartment();
    public Department findDepartment(int key);
    public boolean updateDepartment(Department department);
    public boolean deleteDepartment(Department department);
    public List<Department> getAllDepatments();
}
