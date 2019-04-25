package com.technical.challenge.model;

public enum Direction {
    NORTH("WEST", "EAST"),
    EAST("NORTH", "SOUTH"),
    SOUTH("EAST", "WEST"),
    WEST("SOUTH", "NORTH");

    private String left;
    private String right;

    Direction(String left, String right) {
        this.left = left;
        this.right = right;
    }

    public String getLeft() {
        return this.left;
    }

    public String getRight() {
        return this.right;
    }

}
