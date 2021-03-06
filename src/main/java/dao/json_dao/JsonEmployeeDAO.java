package dao.json_dao;

import caching.SingletonCache;
import dao.dao_interface.EmployeeDAO;
import domain.Employee;
import generator.JsonParser;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class JsonEmployeeDAO implements EmployeeDAO {
    private static final String FILE_NAME = "F:\\save\\netcracker\\kozlovalab2\\src\\main\\resources\\json\\json-employee.txt";

    private Employee parseJson(JSONObject jsonObject){
        Employee employee = new Employee();
        employee.setEmployeeID(jsonObject.getLong("employeeID"));
        employee.setName(jsonObject.getString("name"));
        employee.setSurname(jsonObject.getString("surname"));
        employee.setSalary(jsonObject.getDouble("salary"));
        employee.setDepartmentID(jsonObject.getLong("departmentID"));
        List<Long> taskList = new ArrayList<>();
        for(Object taskId : jsonObject.getJSONArray("taskList")){
            taskList.add(Long.parseLong(taskId.toString()));
        }
        employee.setTaskList(taskList);
        return employee;
    }
    @Override
    public boolean insertEmployee(Employee employee) {
        String stringJson = new JSONObject(employee).toString();

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
    public Employee findEmployee(long key) {
        Employee employee = (Employee) SingletonCache.getInstance().get(key);

        if(employee != null)
            return employee;

        employee = parseJson(JsonParser.parseFile
                (key, Employee.class, FILE_NAME));
        SingletonCache.getInstance().put(key, employee);

        return employee;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        SingletonCache.getInstance().put(employee.getEmployeeID(), employee);
        deleteEmployee(employee.getEmployeeID());
        insertEmployee(employee);
        return true;
    }

    @Override
    public boolean deleteEmployee(long key) {
        JsonParser.removeLineFromFile(FILE_NAME,
                JsonParser.parseFile(key, Employee.class, FILE_NAME).toString());
        SingletonCache.getInstance().remove(key);
        return true;
    }
}
