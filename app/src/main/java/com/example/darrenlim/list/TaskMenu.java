package com.example.darrenlim.list;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.parse.ParseObject;

/**
 * Created by darrenlim on 12/1/15.
 */
public class TaskMenu extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        setTitle(R.string.newReminderTitle);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED, new Intent());
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {

        String title, notes;
        EditText text = (EditText) findViewById(R.id.title);
        title = text.getText().toString();
        if(title.equals("")) {
            Toast.makeText(TaskMenu.this, R.string.noTitleError, Toast.LENGTH_LONG).show();
            ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
            scrollView.smoothScrollTo(0,0);
            text.setFocusableInTouchMode(true);
            text.requestFocus();
            return;
        }
        text = (EditText) findViewById(R.id.notes);
        notes = text.getText().toString();

        ParseObject reminderObj = new ParseObject("ReminderObj");
        reminderObj.put("title", title);
        reminderObj.put("notes", notes);
//        reminderObj.put("label", "Payment");
//        reminderObj.put("priority", 0);
//        reminderObj.put("remindOnDay", true);
//        reminderObj.put("remindAtLocation", false);
//        reminderObj.put("date", 151206);
//        reminderObj.put("time", 900);
        reminderObj.saveInBackground();
        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

}
