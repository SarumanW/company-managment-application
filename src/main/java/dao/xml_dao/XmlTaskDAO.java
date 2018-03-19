package dao.xml_dao;

import dao.dao_interface.TaskDAO;
import domain.Manager;
import domain.Task;
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

public class XmlTaskDAO implements TaskDAO {
    private static final String FILE_NAME = "F:\\save\\netcracker\\kozlovalab2\\src\\main\\resources\\xml\\xml-task.xml";

    private Task extractTaskFromXML(Element element){
        Task task = new Task();
        task.setTaskID(Long.parseLong(element.getElementsByTagName("TaskID").item(0).getTextContent()));
        task.setEstimate(Long.parseLong(element.getElementsByTagName("Estimate").item(0).getTextContent()));
        task.setSprintID(Long.parseLong(element.getElementsByTagName("SprintID").item(0).getTextContent()));
        task.setName(element.getElementsByTagName("Name").item(0).getTextContent());

        Node node = element.getElementsByTagName("EmployeeList").item(0);
        NodeList nodeList = node.getChildNodes();
        List<Long> employeeList = new ArrayList<>();

        for(int i=0; i<nodeList.getLength(); i++){
            Node employeeID = nodeList.item(i);
            if(employeeID.getTextContent().replaceAll("\\n", "").
                    replaceAll("\\s", "").isEmpty())
                continue;
            employeeList.add(Long.parseLong(employeeID.getTextContent().replaceAll("\\s", "")));
        }

        task.setEmployeeList(employeeList);
        return task;
    }

    @Override
    public boolean insertTask(Task task) {
        Document document = DomHelper.getDocument(FILE_NAME);
        Map<String, Method> methodMap = DomHelper.getClassGetters(Task.class);

        Element tasks = document.getDocumentElement();
        Element taskElement = document.createElement(Task.class.getSimpleName());

        for(Map.Entry<String, Method> entry : methodMap.entrySet()) {
            try {
                Element element = document.createElement(entry.getKey());

                if (entry.getKey().equals("EmployeeList")) {
                    for (long id : task.getEmployeeList()) {
                        Element employeeID = document.createElement("EmployeeID");
                        employeeID.setTextContent(String.valueOf(id));
                        element.appendChild(employeeID);
                    }
                } else {
                    element.appendChild(document.createTextNode(String.valueOf(entry.getValue().invoke(task))));
                }

                taskElement.appendChild(element);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        tasks.appendChild(taskElement);
        DomHelper.saveXMLContent(document, FILE_NAME);
        return true;
    }

    @Override
    public Task findTask(long key) {
        Task task = new Task();
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Task");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            if(element.getElementsByTagName("TaskID").item(0).getTextContent().
                    equals(String.valueOf(key))){
                task = extractTaskFromXML(element);
            }
        }

        return task;
    }

    @Override
    public boolean updateTask(Task task) {
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Task");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);

            if(element.getElementsByTagName("TaskID").item(0).getTextContent().
                    equals(String.valueOf(task.getTaskID()))){

                element.getElementsByTagName("Name").item(0).setTextContent(task.getName());
                element.getElementsByTagName("Estimate").item(0).setTextContent(String.valueOf(task.getEstimate()));
                element.getElementsByTagName("SprintID").item(0).setTextContent(String.valueOf(task.getSprintID()));

                Node employeeEl = element.getElementsByTagName("EmployeeList").item(0);
                NodeList employeeList = employeeEl.getChildNodes();

                for(int k = 0; k<employeeList.getLength(); k++){
                    Node employeeID = employeeList.item(k);
                    if(employeeID.getTextContent().replaceAll("\\n", "").
                            replaceAll("\\s", "").isEmpty())
                        continue;

                    employeeList.item(k).getTextContent().replaceAll("\\s", "");
                    employeeEl.removeChild(employeeList.item(k));
                }

                for(int j = 0; j<task.getEmployeeList().size(); j++){
                    Element newEmployee = document.createElement("EmployeeID");
                    newEmployee.setTextContent(String.valueOf(task.getEmployeeList().get(j)));
                    employeeEl.appendChild(newEmployee);
                }
            }
        }
        return false;
    }

    @Override
    public boolean deleteTask(long key) {
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Task");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            if(element.getElementsByTagName("TaskID").item(0).getTextContent().
                    equals(String.valueOf(key)))
                element.getParentNode().removeChild(element);
        }

        DomHelper.saveXMLContent(document, FILE_NAME);
        return true;
    }
}
