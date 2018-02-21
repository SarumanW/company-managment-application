package dao.xml_dao;

import dao.dao_interface.DepartmentDAO;
import domain.Department;

public class XmlDepartmentDAO implements DepartmentDAO {
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
