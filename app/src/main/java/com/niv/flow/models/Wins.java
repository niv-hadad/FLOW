package com.niv.flow.models;

import java.util.ArrayList;

public class Wins extends ArrayList<Win> {
    private static Wins instance;

    private Wins() { super(); }

    public static Wins getInstance() {
        if (instance==null) {
            instance = new Wins();
        }
        return instance;
    }
}
