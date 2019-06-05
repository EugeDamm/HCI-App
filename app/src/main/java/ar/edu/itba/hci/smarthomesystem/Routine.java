package ar.edu.itba.hci.smarthomesystem;

import org.json.JSONArray;

public class Routine {

    String id;
    String name;
    JSONArray actions;

    public Routine(String id, String name, JSONArray actions) {
        this.id = id;
        this.name = name;
        this.actions = actions;
    }


    @Override
    public String toString() {
        return id + " " + name;
    }
}
