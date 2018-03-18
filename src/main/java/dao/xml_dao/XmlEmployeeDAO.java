package dao.xml_dao;

import dao.dao_interface.EmployeeDAO;
import domain.Department;
import domain.Employee;
import generator.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XmlEmployeeDAO implements EmployeeDAO {
    private static final String FILE_NAME = "F:\\save\\netcracker\\kozlovalab2\\src\\main\\resources\\xml\\xml-employee.xml";

    private Employee extractEmployeeFromXML(Element element){
        Employee employee = new Employee();
        employee.setEmployeeID(Long.parseLong(element.getElementsByTagName("EmployeeID").item(0).getTextContent()));
        employee.setName(element.getElementsByTagName("Name").item(0).getTextContent());
        employee.setSurname(element.getElementsByTagName("Surname").item(0).getTextContent());
        employee.setDepartmentID(Long.parseLong(element.getElementsByTagName("DepartmentID").item(0).getTextContent()));
        employee.setSalary(Double.parseDouble(element.getElementsByTagName("Salary").item(0).getTextContent()));

        Node node = element.getElementsByTagName("TaskList").item(0);
        NodeList nodeList = node.getChildNodes();
        List<Long> taskList = new ArrayList<>();

        for(int i=0; i<nodeList.getLength(); i++){
            Node taskID = nodeList.item(i);
            if(taskID.getTextContent().replaceAll("\\n", "").
                    replaceAll("\\s", "").isEmpty())
                continue;
            taskList.add(Long.parseLong(taskID.getTextContent().replaceAll("\\s", "")));
        }

        employee.setTaskList(taskList);
        return employee;
    }

    @Override
    public boolean insertEmployee(Employee employee) {
        Document document = DomHelper.getDocument(FILE_NAME);
        Map<String, Method> methodMap = DomHelper.getClassGetters(Employee.class);

        Element employees = document.getDocumentElement();
        Element employeeElement = document.createElement(Employee.class.getSimpleName());

        for(Map.Entry<String, Method> entry : methodMap.entrySet()) {
            try {
                Element element = document.createElement(entry.getKey());

                if (entry.getKey().equals("TaskList")) {
                    for (long id : employee.getTaskList()) {
                        Element taskID = document.createElement("TaskID");
                        taskID.setTextContent(String.valueOf(id));
                        element.appendChild(taskID);
                    }
                } else {
                    element.appendChild(document.createTextNode(String.valueOf(entry.getValue().invoke(employee))));
                }

                employeeElement.appendChild(element);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        employees.appendChild(employeeElement);
        DomHelper.saveXMLContent(document, FILE_NAME);
        return true;
    }

    @Override
    public Employee findEmployee(long key) {
        Employee employee = new Employee();
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Employee");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            if(element.getElementsByTagName("EmployeeID").item(0).getTextContent().
                    equals(String.valueOf(key))){
                employee = extractEmployeeFromXML(element);
            }
        }

        return employee;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Employee");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);

            if(element.getElementsByTagName("EmployeeID").item(0).getTextContent().
                    equals(String.valueOf(employee.getEmployeeID()))){

                element.getElementsByTagName("Name").item(0).setTextContent(employee.getName());
                element.getElementsByTagName("Surname").item(0).setTextContent(employee.getSurname());
                element.getElementsByTagName("Salary").item(0).setTextContent(String.valueOf(employee.getSalary()));
                element.getElementsByTagName("DepartmentID").item(0).setTextContent(String.valueOf(employee.getDepartmentID()));

                Node taskEl = element.getElementsByTagName("TaskList").item(0);
                NodeList taskList = taskEl.getChildNodes();

                for(int k = 0; k<taskList.getLength(); k++){
                    Node taskID = taskList.item(k);
                    if(taskID.getTextContent().replaceAll("\\n", "").
                            replaceAll("\\s", "").isEmpty())
                        continue;

                    System.out.println(taskList.item(k).getTextContent().replaceAll("\\s", ""));
                    taskEl.removeChild(taskList.item(k));
                }

                for(int j = 0; j<employee.getTaskList().size(); j++){
                    Element newTask = document.createElement("TaskID");
                    newTask.setTextContent(String.valueOf(employee.getTaskList().get(j)));
                    taskEl.appendChild(newTask);
                }
            }
        }

        DomHelper.saveXMLContent(document, FILE_NAME);
        return true;
    }

    @Override
    public boolean deleteEmployee(long key) {
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Employee");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            if(element.getElementsByTagName("EmployeeID").item(0).getTextContent().
                    equals(String.valueOf(key)))
                element.getParentNode().removeChild(element);
        }

        DomHelper.saveXMLContent(document, FILE_NAME);
        return true;
    }
}
