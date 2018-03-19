package dao.xml_dao;

import dao.dao_interface.ProjectDAO;
import domain.Manager;
import domain.Project;
import generator.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class XmlProjectDAO implements ProjectDAO {
    private static final String FILE_NAME = "F:\\save\\netcracker\\kozlovalab2\\src\\main\\resources\\xml\\xml-project.xml";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd");

    private Project extractProjectFromXML(Element element){
        Project project = new Project();
        project.setProjectID(Long.parseLong(element.getElementsByTagName("ProjectID").item(0).getTextContent()));
        project.setName(element.getElementsByTagName("Name").item(0).getTextContent());
        project.setManagerID(Long.parseLong(element.getElementsByTagName("ManagerID").item(0).getTextContent()));
        project.setCustomerID(Long.parseLong(element.getElementsByTagName("CustomerID").item(0).getTextContent()));
        try {
            Calendar end = Calendar.getInstance();
            Calendar start = Calendar.getInstance();
            end.setTime(sdf.parse(element.getElementsByTagName("End").item(0).getTextContent()));
            start.setTime(sdf.parse(element.getElementsByTagName("Start").item(0).getTextContent()));
            project.setEnd(end);
            project.setStart(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Node node = element.getElementsByTagName("SprintList").item(0);
        NodeList nodeList = node.getChildNodes();
        List<Long> sprintList = new ArrayList<>();

        for(int i=0; i<nodeList.getLength(); i++){
            Node sprintID = nodeList.item(i);
            if(sprintID.getTextContent().replaceAll("\\n", "").
                    replaceAll("\\s", "").isEmpty())
                continue;
            sprintList.add(Long.parseLong(sprintID.getTextContent().replaceAll("\\s", "")));
        }

        project.setSprintList(sprintList);
        return project;
    }

    @Override
    public boolean insertProject(Project project) {
        Document document = DomHelper.getDocument(FILE_NAME);
        Map<String, Method> methodMap = DomHelper.getClassGetters(Project.class);

        Element projects = document.getDocumentElement();
        Element projectElement = document.createElement(Project.class.getSimpleName());

        for(Map.Entry<String, Method> entry : methodMap.entrySet()) {
            try {
                Element element = document.createElement(entry.getKey());

                if (entry.getKey().equals("SprintList")) {
                    for (long id : project.getSprintList()) {
                        Element sprintID = document.createElement("SprintID");
                        sprintID.setTextContent(String.valueOf(id));
                        element.appendChild(sprintID);
                    }
                } else if (entry.getKey().equals("End")){
                    element.appendChild(document.createTextNode(String.valueOf(sdf.format(project.getStart().getTime()))));
                } else if (entry.getKey().equals("Start")){
                    element.appendChild(document.createTextNode(String.valueOf(sdf.format(project.getEnd().getTime()))));
                } else {
                    element.appendChild(document.createTextNode(String.valueOf(entry.getValue().invoke(project))));
                }

                projectElement.appendChild(element);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        projects.appendChild(projectElement);
        DomHelper.saveXMLContent(document, FILE_NAME);
        return true;
    }

    @Override
    public Project findProject(long key) {
        Project project = new Project();
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Project");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            if(element.getElementsByTagName("ProjectID").item(0).getTextContent().
                    equals(String.valueOf(key))){
                project = extractProjectFromXML(element);
            }
        }
        return project;
    }

    @Override
    public boolean updateProject(Project project) {
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Project");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);

            if(element.getElementsByTagName("ProjectID").item(0).getTextContent().
                    equals(String.valueOf(project.getProjectID()))){

                element.getElementsByTagName("Name").item(0).setTextContent(project.getName());
                element.getElementsByTagName("ManagerID").item(0).setTextContent(String.valueOf(project.getManagerID()));
                element.getElementsByTagName("CustomerID").item(0).setTextContent(String.valueOf(project.getCustomerID()));
                element.getElementsByTagName("End").item(0).setTextContent(String.valueOf(sdf.format(project.getEnd().getTime())));
                element.getElementsByTagName("Start").item(0).setTextContent(String.valueOf(sdf.format(project.getStart().getTime())));

                Node sprintEl = element.getElementsByTagName("SprintList").item(0);
                NodeList sprintList = sprintEl.getChildNodes();

                for(int k = 0; k<sprintList.getLength(); k++){
                    Node projectID = sprintList.item(k);
                    if(projectID.getTextContent().replaceAll("\\n", "").
                            replaceAll("\\s", "").isEmpty())
                        continue;

                    sprintList.item(k).getTextContent().replaceAll("\\s", "");
                    sprintEl.removeChild(sprintList.item(k));
                }

                for(int j = 0; j<project.getSprintList().size(); j++){
                    Element newSprint = document.createElement("SprintID");
                    newSprint.setTextContent(String.valueOf(project.getSprintList().get(j)));
                    sprintEl.appendChild(newSprint);
                }
            }
        }

        DomHelper.saveXMLContent(document, FILE_NAME);
        return true;
    }

    @Override
    public boolean deleteProject(long key) {
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Project");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            if(element.getElementsByTagName("ProjectID").item(0).getTextContent().
                    equals(String.valueOf(key)))
                element.getParentNode().removeChild(element);
        }

        DomHelper.saveXMLContent(document, FILE_NAME);
        return true;
    }
}
