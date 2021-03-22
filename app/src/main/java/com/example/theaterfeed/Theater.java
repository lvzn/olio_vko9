package com.example.theaterfeed;

class Theater {
    int id;
    String location;
    Theater(int tID, String loc) {
        id = tID;
        location = loc;
    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }
}
