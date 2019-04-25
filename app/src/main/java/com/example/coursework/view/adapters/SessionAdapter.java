package com.example.coursework.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.coursework.R;
import com.example.coursework.model.Session;
import java.util.List;


public class SessionAdapter extends ArrayAdapter<Session> {

    public SessionAdapter(Context context, List<Session> sessions) {
        super(context, 0, sessions);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Session session = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.session_list_item, parent, false);
        }
        // Lookup view items for data population
        TextView sessionDateTxt = convertView.findViewById(R.id.sessionDateTimeTxt);
        TextView sessionLocation = convertView.findViewById(R.id.sessionLocationTxt);

        // Populate the data into the template view using the data object
        String startTime;
        String endTime = "";
        String display = "";
        if (session != null){
            startTime = session.getStartTime().toLocalDate().toString();
            if (session.getEndTime() != null)
                endTime =  " - " + session.getEndTime().toLocalDate().toString();
            display = startTime + endTime;
        }


        sessionDateTxt.setText(display);
        sessionDateTxt.setTag(session.getId());
        convertView.setTag(session.getId());
        sessionLocation.setText(session.getLocation());
        // Return the completed view to render on screen
        return convertView;
    }
}
