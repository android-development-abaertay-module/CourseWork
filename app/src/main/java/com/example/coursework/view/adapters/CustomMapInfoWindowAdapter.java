package com.example.coursework.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.coursework.R;
import com.example.coursework.model.helper.PrintNull;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

public class CustomMapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private View window;
    private Context context;

    public CustomMapInfoWindowAdapter(Context context) {
        this.context = context;
        window = LayoutInflater.from(context).inflate(R.layout.custom_map_marker_info,null);
    }

    private void renderWindowText(Marker marker,  View view){
        String title = marker.getTitle();
        TextView titleTV = (TextView) view.findViewById(R.id.mapMarkerTitle);
        titleTV.setText(PrintNull.Print(title));

        String snippet = marker.getSnippet();
        TextView snippetTV = (TextView) view.findViewById(R.id.mapMarkerSnippetTV);
        snippetTV.setText(PrintNull.Print(snippet));
    }
    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker,window);
        return window;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker,window);
        return window;
    }
}
