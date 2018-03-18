package dao.xml_dao;

import dao.dao_interface.ProjectDAO;
import domain.Manager;
import domain.Project;
import generator.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class XmlProjectDAO implements ProjectDAO {
    private static final String FILE_NAME = "F:\\save\\netcracker\\kozlovalab2\\src\\main\\resources\\xml\\xml-project.xml";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd");

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
        return null;
    }

    @Override
    public boolean updateProject(Project project) {
        return false;
    }

    @Override
    public boolean deleteProject(long key) {
        return false;
    }
}
