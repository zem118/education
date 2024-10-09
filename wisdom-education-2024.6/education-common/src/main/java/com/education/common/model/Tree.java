package com.education.common.model; 

import lombok.Data;

import java.util.List;


@Data
public class Tree {

    private Integer parentId;
    private Integer id;
    private List<Tree> children;
    private String label;
    private Integer value;

    public Tree() {

    }

    public Tree(int id, String label, int parentId) {
       this.label = label;
       this.id = id;
       this.parentId = parentId;
    }
}
