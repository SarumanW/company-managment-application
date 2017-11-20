package dao;

import domain.Department;

import java.util.List;

public interface DepartmentDAO {
    public boolean insertDepartment(Department department);
    public Department findDepartment(long key);
    public boolean updateDepartment(Department department);
    public boolean deleteDepartment(long key);
    public List<Department> getAllDepartments();
}
