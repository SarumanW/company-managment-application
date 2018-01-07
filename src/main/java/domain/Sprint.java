package domain;

import java.util.List;

public class Sprint {
    private long sprintID;
    private String name;
    private List<Task> taskList;

    public long getSprintID() {
        return sprintID;
    }

    public void setSprintID(long sprintID) {
        this.sprintID = sprintID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
