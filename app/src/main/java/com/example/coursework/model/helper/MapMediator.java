package com.example.coursework.model.helper;

import com.example.coursework.model.Session;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

public class MapMediator {
    private List<Session> recentSessions;
    private GoogleMap map;
    private PlacesClient placesClient;
    private PlaceInfoHoulder customPlace;

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

    public PlacesClient getPlacesClient() {
        return placesClient;
    }

    public void setPlacesClient(PlacesClient placesClient) {
        this.placesClient = placesClient;
    }

    public PlaceInfoHoulder getCustomPlace() {
        return customPlace;
    }

    public void setCustomPlace(PlaceInfoHoulder customPlace) {
        this.customPlace = customPlace;
    }

    public MapMediator(List<Session> recentSessions, GoogleMap map, PlacesClient client, PlaceInfoHoulder customPlace) {
        this.recentSessions = recentSessions;
        this.map = map;
        this.placesClient = client;
        this.customPlace = customPlace;
    }
    public MapMediator(){

    }
}
