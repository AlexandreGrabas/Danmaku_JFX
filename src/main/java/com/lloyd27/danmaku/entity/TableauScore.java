package com.lloyd27.danmaku.entity;

import java.io.Serializable;

public class TableauScore implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private double score;

    public TableauScore(String name, double score) {
        this.name = name;
        this.score = score;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
}
