package com.accolite.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExplorerItem {
    private String name;
    private String type; // "folder" or "file"
    private String absolutePath;
    private List<ExplorerItem> children; // Subitems for folders

    // Constructors, getters, and setters

    public ExplorerItem(String name, String type) {
        this.name = name;
        this.type = type;
    }
    public ExplorerItem(String name, String type,String path) {
        this.name = name;
        this.type = type;
        this.absolutePath = path;
    }

    public ExplorerItem(String name, String type, List<ExplorerItem> children) {
        this.name = name;
        this.type = type;
        this.children = children;
    }
    public ExplorerItem(String name, String type, String path,List<ExplorerItem> children) {
        this.name = name;
        this.type = type;
        this.absolutePath=path;
        this.children = children;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ExplorerItem> getChildren() {
        return children;
    }

    public void setChildren(List<ExplorerItem> children) {
        this.children = children;
    }
}
