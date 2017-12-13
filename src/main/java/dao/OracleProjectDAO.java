package dao;

import connections.OracleConnection;
import domain.Project;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OracleProjectDAO implements ProjectDAO {
    private OracleConnection oracleConnection = new OracleConnection();

    private Project extractProjectFromResultSet(ResultSet resultSet) throws SQLException {
        Project project = new Project();
        project.setProjectID(resultSet.getLong(2));

        while(resultSet.next()){
            int i = resultSet.getInt(1);
            switch(i){
                case 110:
                    project.setName(resultSet.getString(3));
                    break;
                case 111:
                    project.getStart().setTime(resultSet.getDate(4));
                    break;
                case 112:
                    project.getEnd().setTime(resultSet.getDate(4));
                    break;
            }
        }

        return project;
    }

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
