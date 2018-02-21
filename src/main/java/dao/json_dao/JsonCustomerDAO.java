package dao.json_dao;

import caching.SingletonCache;
import dao.dao_interface.CustomerDAO;
import domain.Customer;
import generator.Parser;
import org.json.JSONObject;

import java.io.*;

public class JsonCustomerDAO implements CustomerDAO {
    private static final String FILE_NAME = "F:\\save\\netcracker\\kozlovalab2\\src\\main\\resources\\json-customer.txt";

    private Customer parseJson(JSONObject jsonObject){
        Customer customer = new Customer();

        customer.setCustomerID(jsonObject.getLong("customerID"));
        customer.setName(jsonObject.getString("name"));
        customer.setSurname(jsonObject.getString("surname"));
        customer.setProject(jsonObject.getLong("projectID"));

        return customer;
    }

    @Override
    public boolean insertCustomer(Customer customer) {
        String stringJson = new JSONObject(customer).toString();

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
    public Customer findCustomer(long key) {
        Customer customer = (Customer) SingletonCache.getInstance().get(key);

        if(customer != null)
            return customer;

        customer = parseJson(Parser.parseFile
                (key, Customer.class, FILE_NAME));
        SingletonCache.getInstance().put(key, customer);

        return customer;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        SingletonCache.getInstance().put(customer.getCustomerID(), customer);
        deleteCustomer(customer.getCustomerID());
        insertCustomer(customer);
        return true;
    }

    @Override
    public boolean deleteCustomer(long key) {
        Parser.removeLineFromFile(FILE_NAME,
                Parser.parseFile(key, Customer.class, FILE_NAME).toString());
        SingletonCache.getInstance().remove(key);
        return true;
    }
}
