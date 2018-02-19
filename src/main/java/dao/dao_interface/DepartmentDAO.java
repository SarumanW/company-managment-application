package dao.dao_interface;

import domain.Department;

public interface DepartmentDAO {
    boolean insertDepartment(Department department);
    Department findDepartment(long key);
    boolean updateDepartment(Department department);
    boolean deleteDepartment(long key);
}
