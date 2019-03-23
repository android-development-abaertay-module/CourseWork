package com.example.coursework.model.helper;

import com.example.coursework.model.Session;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;

public class MapMediator {
    private List<Session> recentSessions;
    private GoogleMap map;

    public List<Session> getRecentSessions() {
        return recentSessions;
    }

    public void setRecentSessions(List<Session> recentSessions) {
        this.recentSessions = recentSessions;
    }

    public GoogleMap getMap() {
        return map;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    public MapMediator(List<Session> recentSessions, GoogleMap map) {
        this.recentSessions = recentSessions;
        this.map = map;
    }
}
