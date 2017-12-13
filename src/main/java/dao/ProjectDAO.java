package dao;

import domain.Project;

public interface ProjectDAO {
    boolean insertProject(Project project);
    Project findProject(long key);
    boolean updateProject(Project project);
    boolean deleteProject(long key);
}
