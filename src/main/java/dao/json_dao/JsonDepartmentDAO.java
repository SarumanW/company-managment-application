package dao.json_dao;

import dao.dao_interface.DepartmentDAO;
import domain.Department;

public class JsonDepartmentDAO implements DepartmentDAO {
    @Override
    public boolean insertDepartment(Department department) {
        return false;
    }

    @Override
    public Department findDepartment(long key) {
        return null;
    }

    @Override
    public boolean updateDepartment(Department department) {
        return false;
    }

    @Override
    public boolean deleteDepartment(long key) {
        return false;
    }
}
