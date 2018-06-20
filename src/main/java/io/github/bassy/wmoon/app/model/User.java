package io.github.bassy.wmoon.app.model;

public class User {
    int id;
    String name;
    Sex sex;

    public enum Sex {
        MALE,
        FEMALE
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }
}
