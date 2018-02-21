package dao.xml_dao;

import dao.dao_interface.ProjectDAO;
import domain.Project;

public class XmlProjectDAO implements ProjectDAO {
    @Override
    public boolean insertProject(Project project) {
        return false;
    }

    @Override
    public Project findProject(long key) {
        return null;
    }

    @Override
    public boolean updateProject(Project project) {
        return false;
    }

    @Override
    public boolean deleteProject(long key) {
        return false;
    }
}
