package dao.xml_dao;

import dao.dao_interface.DepartmentDAO;
import domain.Customer;
import domain.Department;
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

public class XmlDepartmentDAO implements DepartmentDAO {
    private static final String FILE_NAME = "F:\\save\\netcracker\\kozlovalab2\\src\\main\\resources\\xml\\xml-department.xml";

    private Department extractDepartmentFromXML(Element element){
        Department department = new Department();
        department.setName(element.getElementsByTagName("Name").item(0).getTextContent());
        System.out.println("created");
        Node node = element.getElementsByTagName("Employees").item(0);
        NodeList nodeList = node.getChildNodes();
        List<Long> employees = new ArrayList<>();

        for(int i=0; i<nodeList.getLength(); i++){
            Node employeeID = nodeList.item(i);
            if(employeeID.getTextContent().replaceAll("\\n", "").
                    replaceAll("\\s", "").isEmpty())
                continue;
            System.out.println("- " + employeeID.getTextContent());
            employees.add(Long.parseLong(employeeID.getTextContent().replaceAll("\\s", "")));
        }
        department.setEmployees(employees);
        return department;
    }

    @Override
    public boolean insertDepartment(Department department) {
        Document document = DomHelper.getDocument(FILE_NAME);
        Map<String, Method> methodMap = DomHelper.getClassGetters(Department.class);

        Element departments = document.getDocumentElement();
        Element departmentElement = document.createElement(Department.class.getSimpleName());
        for(Map.Entry<String, Method> entry : methodMap.entrySet()){
            try {
                Element element = document.createElement(entry.getKey());
                if(entry.getKey().equals("Employees")){
                    for(long id : department.getEmployees()){
                        Element employeeID = document.createElement("EmployeeID");
                        employeeID.setTextContent(String.valueOf(id));
                        element.appendChild(employeeID);
                    }
                } else {
                    element.appendChild(document.createTextNode(String.valueOf(entry.getValue().invoke(department))));
                }
                departmentElement.appendChild(element);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        departments.appendChild(departmentElement);
        DomHelper.saveXMLContent(document, FILE_NAME);
        return true;
    }

    @Override
    public Department findDepartment(long key) {
        Department department = new Department();
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Department");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            if(element.getElementsByTagName("DepartmentID").item(0).getTextContent().
                    equals(String.valueOf(key))){
                department = extractDepartmentFromXML(element);
            }
        }

        return department;
    }

    @Override
    public boolean updateDepartment(Department department) {
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Department");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            if(element.getElementsByTagName("DepartmentID").item(0).getTextContent().
                    equals(String.valueOf(department.getDepartmentID()))){

                element.getElementsByTagName("Name").item(0).setTextContent(department.getName());
                Element employeesEl = (Element) element.getElementsByTagName("Employees");
                NodeList empList = employeesEl.getElementsByTagName("EmployeeID");

                for(int k = 0; k<empList.getLength(); k++){
                    employeesEl.removeChild(empList.item(k));
                }

                for(int j = 0; j<department.getEmployees().size(); j++){
                    Element newEmployee = document.createElement("EmployeeID");
                    newEmployee.setTextContent(String.valueOf(department.getEmployees().get(j)));
                    employeesEl.appendChild(newEmployee);
                }
            }
        }

        DomHelper.saveXMLContent(document, FILE_NAME);
        return true;
    }

    @Override
    public boolean deleteDepartment(long key) {
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Department");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            if(element.getElementsByTagName("DepartmentID").item(0).getTextContent().
                    equals(String.valueOf(key)))
                element.getParentNode().removeChild(element);
        }

        DomHelper.saveXMLContent(document, FILE_NAME);
        return true;
    }
}
