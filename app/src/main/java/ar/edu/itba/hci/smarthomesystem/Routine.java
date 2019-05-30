package ar.edu.itba.hci.smarthomesystem;

import java.lang.reflect.Array;

public class Routine {

    String id;
    String name;
    String[] actions;

    public Routine(String id, String name, String[] actions) {
        this.id = id;
        this.name = name;
        this.actions = actions;
    }
}
