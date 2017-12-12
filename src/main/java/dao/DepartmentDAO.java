package dao;

import domain.Department;

import java.util.List;

public interface DepartmentDAO {
    boolean insertDepartment(Department department);
    Department findDepartment(long key);
    boolean updateDepartment(Department department);
    boolean deleteDepartment(long key);
    List<Department> getAllDepartments();
}
