package com.example.darrenlim.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.parse.ParseUser;

import org.json.JSONArray;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by darrenlim on 12/15/15.
 */
public class CheckListMenu extends AppCompatActivity implements View.OnClickListener{

    private Toolbar _toolbar;
    private RecyclerView _recyclerView;
    private ChecklistItemAdapter _chAdapter;
    private ArrayList<String> _items = new ArrayList<>();
    private ArrayList<Boolean> _itemsTruth = new ArrayList<>();
    public static ArrayList<EditText> listOfEditText = new ArrayList<>();
    public static ArrayList<CheckBox> listOfCheckBox = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list_menu);
        setUpToolbar();

        _items.clear(); _itemsTruth.clear(); listOfEditText.clear(); listOfCheckBox.clear();
        _items.add("");
        _itemsTruth.add(false);
        _recyclerView = (RecyclerView) this.findViewById(R.id.checklist_recycler_view);
        _recyclerView.setHasFixedSize(false);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
        _recyclerView.setItemAnimator(new DefaultItemAnimator());
        _chAdapter = new ChecklistItemAdapter(this, _items, _itemsTruth, false);
        _recyclerView.setAdapter(_chAdapter);


    }

    @Override
    public void onClick(View v) {

        String title;
        EditText text = (EditText) findViewById(R.id.checklist_title);
        title = text.getText().toString();
        if(title.equals("")) {
            Toast.makeText(CheckListMenu.this, R.string.noTitleError, Toast.LENGTH_LONG).show();
            ScrollView scrollView = (ScrollView) findViewById(R.id.checklist_scrollview);
            scrollView.smoothScrollTo(0,0);
            text.setFocusableInTouchMode(true);
            text.requestFocus();
            return;
        }

        Reminder reminder = new Reminder();
        reminder.setTitle(title);
        reminder.setCategory("");
        reminder.setUser(ParseUser.getCurrentUser().getUsername());

        for(int i=0; i<listOfEditText.size(); i++) {
            String itemString = listOfEditText.get(i).getText().toString();
            _items.set(i, itemString);
        }

        for(int i=0; i<listOfCheckBox.size(); i++) {
            Boolean itemBool = listOfCheckBox.get(i).isChecked();
            _itemsTruth.set(i, itemBool);
        }

        JSONArray items = new JSONArray(Arrays.asList(_items));
        JSONArray itemsTruth = new JSONArray(Arrays.asList(_itemsTruth));
        reminder.setItems(items);
        reminder.setItemsTruth(itemsTruth);
        reminder.setIsChecklist(true);
        reminder.setUser(ParseUser.getCurrentUser().getUsername());
        reminder.saveInBackground();
        MainActivity._data.add(0, reminder);

        setResult(MainActivity.RESULT_OK, new Intent());
        finish();

    }

    private void setUpToolbar() {
        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (_toolbar != null) {
            setSupportActionBar(_toolbar);
            getSupportActionBar().setTitle("New Checklist");
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

    public void onAddChecklistItem(View v) {
        ImageButton imageButton = (ImageButton) v.findViewById(R.id.add_item_button);
        imageButton.setImageResource(R.drawable.ic_remove_black_24dp);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(_recyclerView.getChildAdapterPosition((View)v.getParent()));
            }
        });
        _items.add("");
        _itemsTruth.add(false);
        _chAdapter.updateChecklistAdapter();
        _chAdapter.notifyItemInserted(_items.size()-1);
    }

    public void delete(int position) {
        _items.remove(position);
        _itemsTruth.remove(position);
        _chAdapter.notifyItemRemoved(position);
        listOfEditText.remove(position);
        listOfCheckBox.remove(position);
    }

}
