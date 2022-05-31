package com.campus02.todocheckliste.model;

public class ToDoModel {
    private int id, status, orginatorID, lastModifiedID;
    private int isPublic;
    private String task, taskDescription;
    private String lastModified;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription= taskDescription;
    }

    public int getOrginatorID() {
        return orginatorID;
    }

    public void setOrginatorID(int orginatorID) {
        this.orginatorID = orginatorID;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int is) {

        this.isPublic = is;
    }

    public int getLastModifiedID() {
        return lastModifiedID;
    }

    public void setLastModifiedID(int lastModifiedID) {
        this.lastModifiedID = lastModifiedID;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

}

