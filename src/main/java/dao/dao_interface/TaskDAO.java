package dao.dao_interface;

import domain.Task;

public interface TaskDAO {
    boolean insertTask(Task task);
    Task findTask(long key);
    boolean updateTask(Task task);
    boolean deleteTask(long key);
}
