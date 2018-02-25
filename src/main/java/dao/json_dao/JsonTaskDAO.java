package dao.json_dao;

import caching.SingletonCache;
import dao.dao_interface.TaskDAO;
import domain.Task;
import generator.JsonParser;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class JsonTaskDAO implements TaskDAO {
    private static final String FILE_NAME = "F:\\save\\netcracker\\kozlovalab2\\src\\main\\resources\\json\\json-task.txt";

    private Task parseJson(JSONObject jsonObject){
        Task task = new Task();

        task.setTaskID(jsonObject.getLong("taskID"));
        task.setName(jsonObject.getString("name"));
        task.setSprint(jsonObject.getLong("sprintID"));
        task.setEstimate(jsonObject.getLong("estimate"));
        List<Long> employees= new ArrayList<>();
        for(Object employeeId : jsonObject.getJSONArray("employees")){
            employees.add(Long.parseLong(employeeId.toString()));
        }
        task.setEmployees(employees);

        return task;
    }

    @Override
    public boolean insertTask(Task task) {
        String stringJson = new JSONObject(task).toString();

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
    public Task findTask(long key) {
        Task task = (Task) SingletonCache.getInstance().get(key);

        if(task != null)
            return task;

        task = parseJson(JsonParser.parseFile
                (key, Task.class, FILE_NAME));
        SingletonCache.getInstance().put(key, task);

        return task;
    }

    @Override
    public boolean updateTask(Task task) {
        SingletonCache.getInstance().put(task.getTaskID(), task);
        deleteTask(task.getTaskID());
        insertTask(task);
        return true;
    }

    @Override
    public boolean deleteTask(long key) {
        JsonParser.removeLineFromFile(FILE_NAME,
                JsonParser.parseFile(key, Task.class, FILE_NAME).toString());
        SingletonCache.getInstance().remove(key);
        return true;
    }
}
