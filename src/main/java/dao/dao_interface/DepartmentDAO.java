package dao.dao_interface;

import domain.Department;

import java.util.List;

public interface DepartmentDAO {
    boolean insertDepartment(Department department);
    Department findDepartment(long key);
    boolean updateDepartment(Department department);
    boolean deleteDepartment(long key);
}
