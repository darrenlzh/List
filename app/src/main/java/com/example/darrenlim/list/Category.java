package com.example.darrenlim.list;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;
import java.util.Objects;

@ParseClassName("CategoryObj")
public class Category extends ParseObject implements Serializable{

    public Category() {

    }

    public void setCategory(String category) { put("category",category);}
    public String getCategory() { return getString("category");}
    public void setUser(String user) { put("user",user);}
    public String getUser() { return getString("user");}
}
