package com.example.coursework.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.coursework.R;
import com.example.coursework.model.User;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends ArrayAdapter<User> {

    public UsersAdapter(Context context, List<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.userLvItemTV);
        // Populate the data into the template view using the data object
        tvName.setText(user.getUserName());
        tvName.setTag(user.getId());
        // Return the completed view to render on screen
        return convertView;
    }
}