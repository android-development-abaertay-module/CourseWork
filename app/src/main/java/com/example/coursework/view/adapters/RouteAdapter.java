package com.example.coursework.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.coursework.R;
import com.example.coursework.model.Route;

import java.util.List;

public class RouteAdapter extends ArrayAdapter<Route> {
    public RouteAdapter(Context context, List<Route> routes) {
        super(context, 0, routes);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Route route = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.route_list_item, parent, false);
            convertView.setTag(route.getId());
        }
        // Lookup view for data population
        TextView routeTimeDone =  convertView.findViewById(R.id.routeItemTimeDone);
        TextView routeItemType =  convertView.findViewById(R.id.routeItemTypeTxt);
        TextView routeItemGrade =  convertView.findViewById(R.id.routeItemGradeTxt);
        TextView routeItemStyle =  convertView.findViewById(R.id.routeItemStyleTxt);
        // Populate the data into the template view using the data object
        routeTimeDone.setText(route.getTimeDone().toString());
        routeItemType.setText(route.getRouteType().toString());
        routeItemGrade.setText(route.getGrade().toString());
        routeItemStyle.setText(route.getStyleDone().toString());
        // Return the completed view to render on screen
        return convertView;
    }
}
