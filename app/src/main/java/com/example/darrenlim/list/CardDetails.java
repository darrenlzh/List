package com.example.darrenlim.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardDetails extends AppCompatActivity{

    private android.support.v7.widget.Toolbar _toolbar;
    private int _position;
    private boolean _isNotification;
    protected static ArrayList<CheckBox> listOfCheckBox = new ArrayList<>();
    private Boolean _isChecklist = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listOfCheckBox.clear();
        setContentView(R.layout.activity_card_details);
        setUpToolbar();
        _isNotification = getIntent().getBooleanExtra("notify", false);
        TextView detailsTitle = (TextView) findViewById(R.id.details_title);
        TextView detailsNotes = (TextView) findViewById(R.id.details_notes);
        TextView detailsCategory = (TextView) findViewById(R.id.details_category);
        if(_isNotification){
            detailsTitle.setText(getIntent().getStringExtra("title"));
            detailsNotes.setText(getIntent().getStringExtra("notes"));
            String s = getIntent().getStringExtra("category");
            if(!s.equals("")) {
                detailsCategory.setText("Category: " + getIntent().getStringExtra("category"));
            }
        }
        else {
            _position = getIntent().getIntExtra("intPos", 0);
            System.out.println(_position);
            detailsTitle.setText(MainActivity._data.get(_position).getTitle());
            detailsNotes.setText(MainActivity._data.get(_position).getNotes());
            String s = MainActivity._data.get(_position).getCategory();
            if(!s.equals("")) {
                detailsCategory.setText("Category: " + MainActivity._data.get(_position).getCategory());
            }
            if(MainActivity._data.get(_position).isChecklist()) {
                _isChecklist = true;
                RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.checklist_recycler_view);
                recyclerView.setHasFixedSize(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                JSONArray jsonItems = MainActivity._data.get(_position).getItems();
                JSONArray jsonItemsTruth = MainActivity._data.get(_position).getItemsTruth();
                ArrayList<String> items = getStringArray(jsonItems);
                ArrayList<Boolean> itemsTruth = getBoolArray(jsonItemsTruth);
                ChecklistItemAdapter chAdapter = new ChecklistItemAdapter(this, items, itemsTruth, true);
                recyclerView.setAdapter(chAdapter);
            }
        }
    }

    public ArrayList<String> getStringArray(JSONArray jsonArray){
        ArrayList<String> arrayList = new ArrayList<>();
        int length = jsonArray.optJSONArray(0).length();
        if(jsonArray!=null){
            for(int i=0;i<length;i++){
                arrayList.add(jsonArray.optJSONArray(0).optString(i));
            }
        }
        return arrayList;
    }

    public ArrayList<Boolean> getBoolArray(JSONArray jsonArray){
        ArrayList<Boolean> arrayList = new ArrayList<>();
        int length = jsonArray.optJSONArray(0).length();
        if(jsonArray!=null){
            for(int i=0;i<length;i++){
                arrayList.add(jsonArray.optJSONArray(0).optBoolean(i));
            }
        }
        return arrayList;
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
                    if( _isChecklist) {
                        System.out.println("THIS");
                        final ArrayList<Boolean> newItemsTruth = new ArrayList<>();
                        for(int i=0; i<listOfCheckBox.size(); i++) {
                            Boolean itemBool = listOfCheckBox.get(i).isChecked();
                            newItemsTruth.add(itemBool);
                        }
//            newItemsTruth.set(0, true);
                        Reminder reminder = MainActivity._data.get(_position);
                        JSONArray jsonNewItemsTruth = new JSONArray(Arrays.asList(newItemsTruth));
                        reminder.setItemsTruth(jsonNewItemsTruth);
                        reminder.saveInBackground();
                        _isChecklist = false;
                    }
                    if (!_isNotification) {
                        setResult(Activity.RESULT_CANCELED, new Intent());
                    }
                    else if(MainActivity._currentUser == null) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    finish();
                }
            });
        }
    }
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event){
        if(keycode == KeyEvent.KEYCODE_BACK && _isNotification){
            if(MainActivity._currentUser == null) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            }
        }
        return super.onKeyDown(keycode,event);
    }
}
