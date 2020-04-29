package com.pansala;

/*Model class for recycler view*/

public class NModelRecord {

    //variables
    String id, name, image, history, start, monk, templocation, phone, addedTime, updatedTime;

    public NModelRecord(String id, String name, String image, String history, String start, String monk, String templocation, String phone, String addedTime, String updatedTime) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.history = history;
        this.start = start;
        this.monk = monk;
        this.templocation = templocation;
        this.phone = phone;
        this.addedTime = addedTime;
        this.updatedTime = updatedTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getMonk() {
        return monk;
    }

    public void setMonk(String monk) {
        this.monk = monk;
    }

    public String getTemplocation() {
        return templocation;
    }

    public void setTemplocation(String templocation) {
        this.templocation = templocation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(String addedTime) {
        this.addedTime = addedTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

}
