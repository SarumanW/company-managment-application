package dao.xml_dao;

import dao.dao_interface.ManagerDAO;
import domain.Employee;
import domain.Manager;
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

public class XmlManagerDAO implements ManagerDAO {
    private static final String FILE_NAME = "F:\\save\\netcracker\\kozlovalab2\\src\\main\\resources\\xml\\xml-manager.xml";

    private Manager extractManagerFromXML(Element element){
        Manager manager = new Manager();
        manager.setManagerID(Long.parseLong(element.getElementsByTagName("ManagerID").item(0).getTextContent()));
        manager.setName(element.getElementsByTagName("Name").item(0).getTextContent());
        manager.setSurname(element.getElementsByTagName("Surname").item(0).getTextContent());
        manager.setSalary(Double.parseDouble(element.getElementsByTagName("Salary").item(0).getTextContent()));

        Node node = element.getElementsByTagName("ProjectList").item(0);
        NodeList nodeList = node.getChildNodes();
        List<Long> projectList = new ArrayList<>();

        for(int i=0; i<nodeList.getLength(); i++){
            Node projectID = nodeList.item(i);
            if(projectID.getTextContent().replaceAll("\\n", "").
                    replaceAll("\\s", "").isEmpty())
                continue;
            projectList.add(Long.parseLong(projectID.getTextContent().replaceAll("\\s", "")));
        }

        manager.setProjectList(projectList);
        return manager;
    }

    @Override
    public boolean insertManager(Manager manager) {
        Document document = DomHelper.getDocument(FILE_NAME);
        Map<String, Method> methodMap = DomHelper.getClassGetters(Manager.class);

        Element managers = document.getDocumentElement();
        Element managerElement = document.createElement(Manager.class.getSimpleName());

        for(Map.Entry<String, Method> entry : methodMap.entrySet()) {
            try {
                Element element = document.createElement(entry.getKey());

                if (entry.getKey().equals("ProjectList")) {
                    for (long id : manager.getProjectList()) {
                        Element projectID = document.createElement("ProjectID");
                        projectID.setTextContent(String.valueOf(id));
                        element.appendChild(projectID);
                    }
                } else {
                    element.appendChild(document.createTextNode(String.valueOf(entry.getValue().invoke(manager))));
                }

                managerElement.appendChild(element);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        managers.appendChild(managerElement);
        DomHelper.saveXMLContent(document, FILE_NAME);
        return true;
    }

    @Override
    public Manager findManager(long key) {
        Manager manager = new Manager();
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Manager");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            if(element.getElementsByTagName("ManagerID").item(0).getTextContent().
                    equals(String.valueOf(key))){
                manager = extractManagerFromXML(element);
            }
        }
        return manager;
    }

    @Override
    public boolean updateManager(Manager manager) {
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Manager");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);

            if(element.getElementsByTagName("ManagerID").item(0).getTextContent().
                    equals(String.valueOf(manager.getManagerID()))){

                element.getElementsByTagName("Name").item(0).setTextContent(manager.getName());
                element.getElementsByTagName("Surname").item(0).setTextContent(manager.getSurname());
                element.getElementsByTagName("Salary").item(0).setTextContent(String.valueOf(manager.getSalary()));

                Node projectEl = element.getElementsByTagName("ProjectList").item(0);
                NodeList projectList = projectEl.getChildNodes();

                for(int k = 0; k<projectList.getLength(); k++){
                    Node projectID = projectList.item(k);
                    if(projectID.getTextContent().replaceAll("\\n", "").
                            replaceAll("\\s", "").isEmpty())
                        continue;

                    System.out.println(projectList.item(k).getTextContent().replaceAll("\\s", ""));
                    projectEl.removeChild(projectList.item(k));
                }

                for(int j = 0; j<manager.getProjectList().size(); j++){
                    Element newProject = document.createElement("ProjectID");
                    newProject.setTextContent(String.valueOf(manager.getProjectList().get(j)));
                    projectEl.appendChild(newProject);
                }
            }
        }

        DomHelper.saveXMLContent(document, FILE_NAME);
        return true;
    }

    @Override
    public boolean deleteManager(long key) {
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Manager");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            if(element.getElementsByTagName("ManagerID").item(0).getTextContent().
                    equals(String.valueOf(key)))
                element.getParentNode().removeChild(element);
        }

        DomHelper.saveXMLContent(document, FILE_NAME);
        return true;
    }
}
