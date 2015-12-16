package com.example.darrenlim.list;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.media.Image;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskMenu extends AppCompatActivity implements View.OnClickListener{

    private CollapsingToolbarLayout _collapsingToolbarLayout;
    private android.support.v7.widget.Toolbar _toolbar;
    private ArrayList<String> _categories;
    private int _priority;
    private Switch _switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_menu);
        setUpToolbar();
//        setupCollapsingToolbarLayout();
        _switch = (Switch) findViewById(R.id.reminderSwitch);
        _switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
                TimePicker tp = (TimePicker) findViewById(R.id.timePicker);
                if (!isChecked) {
                    dp.setVisibility(View.GONE);
                    tp.setVisibility(View.GONE);
                } else {
                    dp.setVisibility(View.VISIBLE);
                    tp.setVisibility(View.VISIBLE);
                }
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.taskPrioritySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.prioritySpinnerArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _priority = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                _priority = 0;
            }
        });


        _categories = new ArrayList<>();
        ParseQuery<Category> query = ParseQuery.getQuery(Category.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<Category>() {
            @Override
            public void done(List<Category> categories, ParseException e) {
                if (e == null) {
                    for (Category c : categories) {
                        _categories.add(c.getCategory());
                    }
                }
            }
        });
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,_categories);
        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.categoryText);
        actv.setThreshold(1);
        actv.setAdapter(autoCompleteAdapter);
    }

    @Override
    public void onClick(View v) {

        String title, notes, category;
        EditText text = (EditText) findViewById(R.id.title);
        title = text.getText().toString();
        if(title.equals("")) {
            Toast.makeText(TaskMenu.this, R.string.noTitleError, Toast.LENGTH_LONG).show();
            ScrollView scrollView = (ScrollView) findViewById(R.id.taskScrollView);
            scrollView.smoothScrollTo(0,0);
            text.setFocusableInTouchMode(true);
            text.requestFocus();
            return;
        }
        text = (EditText) findViewById(R.id.notes);
        notes = text.getText().toString();
        text = (EditText) findViewById(R.id.categoryText);
        category = text.getText().toString();

        Reminder reminder = new Reminder();
        reminder.setTitle(title);
        reminder.setNotes(notes);

        if(!category.equals("")) {
            if (!_categories.contains(category)) {
                Category newCategory = new Category();
                newCategory.setCategory(category);
                newCategory.setUser(ParseUser.getCurrentUser().getUsername());
                newCategory.saveInBackground();
                Toast.makeText(TaskMenu.this, "Category \""+category+"\" added to your categories!" , Toast.LENGTH_LONG).show();
            }
        }

        reminder.setCategory(category);
        reminder.setUser(ParseUser.getCurrentUser().getUsername());
        reminder.setPriority(_priority);

        reminder.saveInBackground();
        MainActivity._data.add(0, reminder);
        if(_switch.isChecked()) {
            DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
            TimePicker tp = (TimePicker) findViewById(R.id.timePicker);
            Calendar cal = MainActivity._calendar;
            cal.set(Calendar.YEAR, dp.getYear());
            cal.set(Calendar.MONTH,dp.getMonth());
            cal.set(Calendar.DAY_OF_MONTH, dp.getDayOfMonth());
            cal.set(Calendar.HOUR,tp.getHour());
            cal.set(Calendar.MINUTE, tp.getMinute());
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND,0);

            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class)
                    .putExtra("notify", true).putExtra("title", title).putExtra("notes", notes);
            AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            System.out.println("HELLO");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            cal.setTimeInMillis(System.currentTimeMillis());
        }

        setResult(MainActivity.RESULT_OK, new Intent());
        finish();
    }

    private void setUpToolbar() {
        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (_toolbar != null) {
            setSupportActionBar(_toolbar);
            getSupportActionBar().setTitle("New Reminder");
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

    private void setupCollapsingToolbarLayout(){

        _collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if(_collapsingToolbarLayout != null){
            _collapsingToolbarLayout.setTitle(_toolbar.getTitle());
            _collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER_VERTICAL);
            //collapsingToolbarLayout.setCollapsedTitleTextColor(0xED1C24);
            //collapsingToolbarLayout.setExpandedTitleColor(0xED1C24);
        }
    }

}
