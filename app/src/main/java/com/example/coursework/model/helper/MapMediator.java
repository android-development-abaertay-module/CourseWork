package com.example.coursework.model.helper;

import com.example.coursework.model.Session;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;
//MapMediator class:
//used in maps activity ViewModel to notify Maps activity about any changes to data that relates to the map
public class MapMediator {
    private List<Session> recentSessions;
    private GoogleMap map;
    private PlacesClient placesClient;
    private PlaceInfoHolder customPlace;

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

    public PlaceInfoHolder getCustomPlace() {
        return customPlace;
    }

    public void setCustomPlace(PlaceInfoHolder customPlace) {
        this.customPlace = customPlace;
    }

    public MapMediator(List<Session> recentSessions, GoogleMap map, PlacesClient client, PlaceInfoHolder customPlace) {
        this.recentSessions = recentSessions;
        this.map = map;
        this.placesClient = client;
        this.customPlace = customPlace;
    }
    public MapMediator(){

    }
}
