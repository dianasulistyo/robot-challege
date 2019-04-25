package com.technical.challenge.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Robot {

    @NonNull
    private String id;

    private int x;

    private int y;

    private Direction direction;

    private int yInUserPerspective;

    public String printForUser(final int boardSize) {
        return x + ", " + (boardSize-1-y) + ", " + direction;
    }
}
