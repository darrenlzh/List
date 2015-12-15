package com.example.darrenlim.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by darrenlim on 12/11/15.
 */
public class CardDetails extends AppCompatActivity{

    private android.support.v7.widget.Toolbar _toolbar;
    private int _position;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _position = getIntent().getIntExtra("intPos", 0);
        setContentView(R.layout.activity_card_details);
        setUpToolbar();
        TextView detailsTitle = (TextView) findViewById(R.id.details_title);
        TextView detailsNotes = (TextView) findViewById(R.id.details_notes);
        detailsTitle.setText(MainActivity._data.get(_position).getTitle());
        detailsNotes.setText(MainActivity._data.get(_position).getNotes());

    }

    private void setUpToolbar() {
        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (_toolbar != null) {
            setSupportActionBar(_toolbar);
            getSupportActionBar().setTitle("");
            _toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            _toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(Activity.RESULT_CANCELED, new Intent());
                    finish();
                }
            });
        }
    }
}
