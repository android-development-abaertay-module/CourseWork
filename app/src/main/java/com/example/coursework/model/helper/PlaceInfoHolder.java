package com.example.coursework.model.helper;

import android.net.Uri;
import android.text.Html;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.Place.Type;

import java.util.List;

/**
 * Created by User on 10/2/2017.
 */

public class PlaceInfoHolder {

    private String name;
    private String address;
    private String phoneNumber;
    private String id;
    private Uri websiteUri;
    private OpeningHours openingHours;
    private LatLng latLng;
    private double rating;
    private List<Type> types;
    private LatLngBounds viewPort;
    private Marker marker;

    public PlaceInfoHolder(String name, String address, String phoneNumber, String id, Uri websiteUri,
                           LatLng latLng, float rating, String attributions, OpeningHours openingHours, List<Type> types, LatLngBounds viewPort, Marker marker) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.id = id;
        this.websiteUri = websiteUri;
        this.latLng = latLng;
        this.rating = rating;
        this.openingHours = openingHours;
        this.types = types;
        this.viewPort = viewPort;
        this.marker = marker;
    }

    public PlaceInfoHolder() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(Uri websiteUri) {
        this.websiteUri = websiteUri;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latlng) {
        this.latLng = latlng;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }
    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public LatLngBounds getViewPort() {
        return viewPort;
    }

    public void setViewPort(LatLngBounds viewPort) {
        this.viewPort = viewPort;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
    public String displaySnippetDetails(){
        String details = "Address: " + address + "\n" +
                "Phone Number: " + phoneNumber + "\n" +
                "Website: " + websiteUri + " \n" ;
        return details;
    }
    @Override
    public String toString() {
        return "PlaceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", id='" + id + '\'' +
                ", websiteUri=" + websiteUri +
                ", latLng=" + latLng +
                ", rating=" + rating +
                ", openingHours= " + openingHours +
                ", types= "+  types +
                ", view Port= " + viewPort +
                ", Marker = " + marker +
                '}';
    }
}
