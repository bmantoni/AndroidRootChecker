package com.example.bobby.rootchecker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;

/**
 * Created by Bobby on 12/5/2014.
 */
public class RootCheckArrayAdapter extends ArrayAdapter<RootCheckResult> {
    private final Context context;
    private ArrayList<RootCheckResult> results;

    public RootCheckArrayAdapter(Context context, ArrayList<RootCheckResult> results) {
        super(context, R.layout.root_check_list_item, results);
        this.context = context;
        this.results = results;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.root_check_list_item, null);
        }

        RootCheckResult c = results.get(position);
        if (c != null) {
            TextView name = (TextView) v.findViewById(R.id.checkName);
            CheckBox passed = (CheckBox) v.findViewById(R.id.checkPassed);

            if (name != null) {
                name.setText(c.Name);
            }

            if(passed != null) {
                passed.setChecked(c.Passed);
            }
        }
        return v;
    }
}