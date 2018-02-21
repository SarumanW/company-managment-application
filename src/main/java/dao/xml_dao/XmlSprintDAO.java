package dao.xml_dao;

import dao.dao_interface.SprintDAO;
import domain.Sprint;

public class XmlSprintDAO implements SprintDAO {
    @Override
    public boolean insertSprint(Sprint sprint) {
        return false;
    }

    @Override
    public Sprint findSprint(long key) {
        return null;
    }

    @Override
    public boolean updateSprint(Sprint sprint) {
        return false;
    }

    @Override
    public boolean deleteSprint(long key) {
        return false;
    }
}
