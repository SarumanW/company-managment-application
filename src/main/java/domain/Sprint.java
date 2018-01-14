package domain;

import generator.UniqueID;

import java.util.ArrayList;
import java.util.List;

public class Sprint {
    private long sprintID;
    private String name;
    private List<Long> taskList;
    private long projectID;

    public Sprint(){
        this.setSprintID(UniqueID.generateID(this));
        taskList = new ArrayList<>();
    }

    public Sprint(String name, Project project){
        this();
        this.name = name;
        this.projectID = project.getProjectID();
    }

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

    public List<Long> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Long> taskList) {
        this.taskList = taskList;
    }

    public long getProjectID() {
        return projectID;
    }

    public void setProjectID(long projectID) {
        this.projectID = projectID;
    }
}
