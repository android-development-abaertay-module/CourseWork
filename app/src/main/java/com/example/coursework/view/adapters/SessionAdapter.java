package com.example.coursework.view.adapters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.coursework.R;
import com.example.coursework.model.Session;
import com.example.coursework.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class SessionAdapter extends ArrayAdapter<Session> {

    private Geocoder geocoder;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String knownName;

    public SessionAdapter(Context context, List<Session> sessions) {
        super(context, 0, sessions);
        geocoder = new Geocoder(context, Locale.getDefault());
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Session session = getItem(position);
        if (session.getLat() != 0 && session.getLon() !=0){
            //get address from location
            getAddressFromLocation(session);
        }
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.session_list_item, parent, false);
        }
        // Lookup view for data population
        TextView sessionDateTxt = (TextView) convertView.findViewById(R.id.sessionDateTimeTxt);
        TextView sessionLocation = convertView.findViewById(R.id.sessionLocationTxt);
        // Populate the data into the template view using the data object
        String startTime = session.getStartTime().toLocalDate().toString();
        String endTime = "";
        if (session.getEndTime() != null)
            endTime =  " - " + session.getEndTime().toLocalDate().toString();
        String display = startTime + endTime;

        sessionDateTxt.setText(display);
        sessionDateTxt.setTag(session.getId());
        convertView.setTag(session.getId());
        sessionLocation.setText(address);
        // Return the completed view to render on screen
        return convertView;
    }

    private void getAddressFromLocation(Session session) {
        try {
            List<Address> localAddress = geocoder.getFromLocation(session.getLat(), session.getLon(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = localAddress.get(0).getAddressLine(0);
            city = localAddress.get(0).getLocality();
            state = localAddress.get(0).getAdminArea();
            country = localAddress.get(0).getCountryName();
            postalCode = localAddress.get(0).getPostalCode();
            knownName = localAddress.get(0).getFeatureName();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
