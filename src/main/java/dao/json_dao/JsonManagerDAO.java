package dao.json_dao;

import caching.SingletonCache;
import dao.dao_interface.ManagerDAO;
import domain.Manager;
import generator.JsonParser;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class JsonManagerDAO implements ManagerDAO {
    private static final String FILE_NAME = "F:\\save\\netcracker\\kozlovalab2\\src\\main\\resources\\json\\json-manager.txt";

    private Manager parseJson(JSONObject jsonObject){
        Manager manager = new Manager();

        manager.setManagerID(jsonObject.getLong("managerID"));
        manager.setName(jsonObject.getString("name"));
        manager.setSurname(jsonObject.getString("surname"));
        manager.setSalary(jsonObject.getDouble("salary"));
        List<Long> projectList= new ArrayList<>();
        for(Object projectId : jsonObject.getJSONArray("projectList")){
            projectList.add(Long.parseLong(projectId.toString()));
        }
        manager.setProjectList(projectList);

        return manager;
    }

    @Override
    public boolean insertManager(Manager manager) {
        String stringJson = new JSONObject(manager).toString();

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
    public Manager findManager(long key) {
        Manager manager = (Manager) SingletonCache.getInstance().get(key);

        if(manager != null)
            return manager;

        manager = parseJson(JsonParser.parseFile
                (key, Manager.class, FILE_NAME));
        SingletonCache.getInstance().put(key, manager);

        return manager;
    }

    @Override
    public boolean updateManager(Manager manager) {
        SingletonCache.getInstance().put(manager.getManagerID(), manager);
        deleteManager(manager.getManagerID());
        insertManager(manager);
        return true;
    }

    @Override
    public boolean deleteManager(long key) {
        JsonParser.removeLineFromFile(FILE_NAME,
                JsonParser.parseFile(key, Manager.class, FILE_NAME).toString());
        SingletonCache.getInstance().remove(key);
        return true;
    }
}
