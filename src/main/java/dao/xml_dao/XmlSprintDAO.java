package dao.xml_dao;

import dao.dao_interface.SprintDAO;
import domain.Manager;
import domain.Sprint;
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

public class XmlSprintDAO implements SprintDAO {
    private static final String FILE_NAME = "F:\\save\\netcracker\\kozlovalab2\\src\\main\\resources\\xml\\xml-sprint.xml";

    private Sprint extractSprintFromXML(Element element){
        Sprint sprint = new Sprint();
        sprint.setSprintID(Long.parseLong(element.getElementsByTagName("SprintID").item(0).getTextContent()));
        sprint.setProjectID(Long.parseLong(element.getElementsByTagName("ProjectID").item(0).getTextContent()));
        sprint.setName((element.getElementsByTagName("Name").item(0).getTextContent()));

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

        sprint.setTaskList(taskList);
        return sprint;
    }

    @Override
    public boolean insertSprint(Sprint sprint) {
        Document document = DomHelper.getDocument(FILE_NAME);
        Map<String, Method> methodMap = DomHelper.getClassGetters(Sprint.class);

        Element sprints = document.getDocumentElement();
        Element sprintElement = document.createElement(Sprint.class.getSimpleName());

        for(Map.Entry<String, Method> entry : methodMap.entrySet()) {
            try {
                Element element = document.createElement(entry.getKey());

                if (entry.getKey().equals("TaskList")) {
                    for (long id : sprint.getTaskList()) {
                        Element taskID = document.createElement("TaskID");
                        taskID.setTextContent(String.valueOf(id));
                        element.appendChild(taskID);
                    }
                } else {
                    element.appendChild(document.createTextNode(
                            String.valueOf(entry.getValue().invoke(sprint))));
                }

                sprintElement.appendChild(element);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        sprints.appendChild(sprintElement);
        DomHelper.saveXMLContent(document, FILE_NAME);
        return true;
    }

    @Override
    public Sprint findSprint(long key) {
        Sprint sprint = new Sprint();
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Sprint");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            if(element.getElementsByTagName("SprintID").item(0).getTextContent().
                    equals(String.valueOf(key))){
                sprint = extractSprintFromXML(element);
            }
        }

        return sprint;
    }

    @Override
    public boolean updateSprint(Sprint sprint) {
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Sprint");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);

            if(element.getElementsByTagName("SprintID").item(0).getTextContent().
                    equals(String.valueOf(sprint.getSprintID()))){

                element.getElementsByTagName("Name").item(0).setTextContent(sprint.getName());
                element.getElementsByTagName("ProjectID").item(0).setTextContent(String.valueOf(sprint.getProjectID()));

                Node taskEl = element.getElementsByTagName("TaskList").item(0);
                NodeList taskList = taskEl.getChildNodes();

                for(int k = 0; k<taskList.getLength(); k++){
                    Node taskID = taskList.item(k);
                    if(taskID.getTextContent().replaceAll("\\n", "").
                            replaceAll("\\s", "").isEmpty())
                        continue;

                    taskList.item(k).getTextContent().replaceAll("\\s", "");
                    taskEl.removeChild(taskList.item(k));
                }

                for(int j = 0; j<sprint.getTaskList().size(); j++){
                    Element newTask = document.createElement("TaskID");
                    newTask.setTextContent(String.valueOf(sprint.getTaskList().get(j)));
                    taskEl.appendChild(newTask);
                }
            }
        }

        DomHelper.saveXMLContent(document, FILE_NAME);
        return true;
    }

    @Override
    public boolean deleteSprint(long key) {
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Sprint");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            if(element.getElementsByTagName("SprintID").item(0).getTextContent().
                    equals(String.valueOf(key)))
                element.getParentNode().removeChild(element);
        }

        DomHelper.saveXMLContent(document, FILE_NAME);
        return true;
    }
}
