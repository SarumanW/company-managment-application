package dao.xml_dao;

import dao.dao_interface.CustomerDAO;
import domain.Customer;
import generator.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class XmlCustomerDAO implements CustomerDAO {
    private static final String FILE_NAME = "F:\\save\\netcracker\\kozlovalab2\\src\\main\\resources\\xml\\xml-customer.xml";

    private Customer extractCustomerFromXML(Element element){
        Customer customer = new Customer();
        customer.setName(element.getElementsByTagName("Name").item(0).getTextContent());
        customer.setSurname(element.getElementsByTagName("Surname").item(0).getTextContent());
        customer.setProject(Long.parseLong(element.getElementsByTagName("ProjectID").item(0).getTextContent()));
        return customer;
    }

    @Override
    public boolean insertCustomer(Customer customer) {
        Document document = DomHelper.getDocument(FILE_NAME);
        Map<String, Method> methodMap = DomHelper.getClassGetters(Customer.class);

        Element customers = document.getDocumentElement();
        Element customerElement = document.createElement(Customer.class.getSimpleName());
        for(Map.Entry<String, Method> entry : methodMap.entrySet()){
            try {
                Element element = document.createElement(entry.getKey());
                element.appendChild(document.createTextNode(String.valueOf(entry.getValue().invoke(customer))));
                customerElement.appendChild(element);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        customers.appendChild(customerElement);
        DomHelper.saveXMLContent(document, FILE_NAME);
        return true;
    }

    @Override
    public Customer findCustomer(long key) {
        Customer customer = new Customer();
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Customer");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            if(element.getElementsByTagName("CustomerID").item(0).getTextContent().
                    equals(String.valueOf(key))){
                customer = extractCustomerFromXML(element);
            }
        }

        return customer;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Customer");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            if(element.getElementsByTagName("CustomerID").item(0).getTextContent().
                    equals(String.valueOf(customer.getCustomerID()))){
                element.getElementsByTagName("Name").item(0).setTextContent(customer.getName());
                element.getElementsByTagName("Surname").item(0).setTextContent(customer.getSurname());
                element.getElementsByTagName("ProjectID").item(0).setTextContent(String.valueOf(customer.getProjectID()));
            }
        }

        DomHelper.saveXMLContent(document, FILE_NAME);
        return true;
    }

    @Override
    public boolean deleteCustomer(long key) {
        Document document = DomHelper.getDocument(FILE_NAME);
        NodeList nodeList = document.getElementsByTagName("Customer");

        for(int i = 0; i<nodeList.getLength(); i++){
            Element element = (Element) nodeList.item(i);
            if(element.getElementsByTagName("CustomerID").item(0).getTextContent().
                    equals(String.valueOf(key)))
                element.getParentNode().removeChild(element);
        }

        DomHelper.saveXMLContent(document, FILE_NAME);
        return true;
    }
}
