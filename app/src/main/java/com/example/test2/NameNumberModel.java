package com.example.test2;

import java.io.Serializable;
import java.util.ArrayList;

public class NameNumberModel implements Serializable {
    String name;
    String number;
    ArrayList<String> pictures = new ArrayList<>();

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public ArrayList<String> getPictures() { return pictures; };

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }
}
