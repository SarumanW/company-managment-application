package dao.json_dao;

import caching.SingletonCache;
import dao.dao_interface.DepartmentDAO;
import domain.Department;
import generator.JsonParser;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class JsonDepartmentDAO implements DepartmentDAO {
    private static final String FILE_NAME = "F:\\save\\netcracker\\kozlovalab2\\src\\main\\resources\\json\\json-department.txt";

    private Department parseJson(JSONObject jsonObject){
        Department department = new Department();

        department.setDepartmentID(jsonObject.getLong("departmentID"));
        department.setName(jsonObject.getString("name"));
        List<Long> employees= new ArrayList<>();
        for(Object employeeId : jsonObject.getJSONArray("employees")){
            employees.add(Long.parseLong(employeeId.toString()));
        }
        department.setEmployees(employees);

        return department;
    }

    @Override
    public boolean insertDepartment(Department department) {
        String stringJson = new JSONObject(department).toString();

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
    public Department findDepartment(long key) {
        Department department = (Department) SingletonCache.getInstance().get(key);

        if(department != null)
            return department;

        department = parseJson(JsonParser.parseFile
                (key, Department.class, FILE_NAME));
        SingletonCache.getInstance().put(key, department);

        return department;
    }

    @Override
    public boolean updateDepartment(Department department) {
        SingletonCache.getInstance().put(department.getDepartmentID(), department);
        deleteDepartment(department.getDepartmentID());
        insertDepartment(department);
        return true;
    }

    @Override
    public boolean deleteDepartment(long key) {
        JsonParser.removeLineFromFile(FILE_NAME,
                JsonParser.parseFile(key, Department.class, FILE_NAME).toString());
        SingletonCache.getInstance().remove(key);
        return true;
    }
}
