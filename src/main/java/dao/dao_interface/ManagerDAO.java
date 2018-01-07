package dao.dao_interface;

import domain.Manager;

public interface ManagerDAO {
    boolean insertManager(Manager manager);
    Manager findManager(long key);
    boolean updateManager(Manager manager);
    boolean deleteManager(long key);
}
