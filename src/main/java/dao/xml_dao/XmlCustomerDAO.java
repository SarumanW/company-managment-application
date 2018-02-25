package dao.xml_dao;

import dao.dao_interface.CustomerDAO;
import domain.Customer;
import generator.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class XmlCustomerDAO implements CustomerDAO {
    private static final String FILE_NAME = "F:\\save\\netcracker\\kozlovalab2\\src\\main\\resources\\xml\\xml-customer.xml";

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

        return customer;
    }

    @Override
    public boolean updateCustomer(Customer customer) {

        return false;
    }

    @Override
    public boolean deleteCustomer(long key) {
        return false;
    }
}
