package dao.dao_interface;

import domain.Customer;

public interface CustomerDAO {
    boolean insertCustomer(Customer customer);
    Customer findCustomer(long key);
    boolean updateCustomer(Customer customer);
    boolean deleteCustomer(long key);
}
