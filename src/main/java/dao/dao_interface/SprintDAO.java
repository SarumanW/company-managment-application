package dao.dao_interface;

import domain.Sprint;

public interface SprintDAO {
    boolean insertSprint(Sprint sprint);
    Sprint findSprint(long key);
    boolean updateSprint(Sprint sprint);
    boolean deleteSprint(long key);
}
