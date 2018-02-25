package dao.json_dao;

import caching.SingletonCache;
import dao.dao_interface.ProjectDAO;
import domain.Project;
import generator.JsonParser;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class JsonProjectDAO implements ProjectDAO {
    private static final String FILE_NAME = "F:\\save\\netcracker\\kozlovalab2\\src\\main\\resources\\json\\json-project.txt";
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    private Project parseJson(JSONObject jsonObject){
        Project project = new Project();

        project.setProjectID(jsonObject.getLong("projectID"));
        project.setName(jsonObject.getString("name"));
        project.setManagerID(jsonObject.getLong("managerID"));
        project.setCustomerID(jsonObject.getLong("customerID"));
        List<Long> sprintList = new ArrayList<>();
        for(Object sprintId : jsonObject.getJSONArray("sprintList")){
            sprintList.add(Long.parseLong(sprintId.toString()));
        }
        project.setSprints(sprintList);
        try {
            project.setStart(formatter.parse(jsonObject.getString("start")));
            project.setEnd(formatter.parse(jsonObject.getString("end")));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return project;
    }

    @Override
    public boolean insertProject(Project project) {
        JSONObject projectJson = new JSONObject(project);
        projectJson.put("start", formatter.format(project.getStart().getTime()));
        projectJson.put("end", formatter.format(project.getEnd().getTime()));
        String stringJson = projectJson.toString();

        try(FileWriter fw = new FileWriter(FILE_NAME, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            out.write(stringJson);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public Project findProject(long key) {
        Project project = (Project) SingletonCache.getInstance().get(key);

        if(project != null)
            return project;

        project = parseJson(JsonParser.parseFile
                (key, Project.class, FILE_NAME));
        SingletonCache.getInstance().put(key, project);

        return project;
    }

    @Override
    public boolean updateProject(Project project) {
        SingletonCache.getInstance().put(project.getProjectID(), project);
        deleteProject(project.getProjectID());
        insertProject(project);
        return true;
    }

    @Override
    public boolean deleteProject(long key) {
        JsonParser.removeLineFromFile(FILE_NAME,
                JsonParser.parseFile(key, Project.class, FILE_NAME).toString());
        SingletonCache.getInstance().remove(key);
        return true;
    }
}
