package com.example.darrenlim.list;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.security.acl.Group;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {


    private android.support.v7.widget.Toolbar _toolbar;
    private DrawerLayout _drawerLayout;
    private CollapsingToolbarLayout _collapsingToolbarLayout;

    private RecyclerView _recyclerView;
    private ReminderAdapter _rAdapter;

    protected static ArrayList<Reminder> _data = new ArrayList<>();
    protected static Calendar _calendar = Calendar.getInstance();
    String _dayOfWeek, _dayOfMonth;

    private Boolean _isFabOpen = false;
    private FloatingActionButton _fab, _fab1, _fab2;
    private Animation _fabOpen, _fabClose, _rotateForward, _rotateBackward, _fabGrow;

    private AlertDialog _dialog;
    private View _dialogView;
    static public ParseUser _currentUser;
    static private boolean _reset = true;
    private String _currentCategory;
    private NavigationView _nv;
    private Menu _menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
        _dayOfWeek = dayFormat.format(_calendar.getTime());
        _dayOfMonth = String.valueOf(_calendar.get(Calendar.DAY_OF_MONTH));
        setContentView(R.layout.activity_main);
        setUpToolbar();
        setupCollapsingToolbarLayout();
        setUpNavDrawer();

        _currentCategory = "All";

        // Enable Local Datastore.
        if(_reset) {
            ParseObject.registerSubclass(Reminder.class);
            ParseObject.registerSubclass(Category.class);
            Parse.enableLocalDatastore(this);
            Parse.initialize(this, "0BC99FjSMdD9UhB5ipsBEey5iSx85hSgb1zRK7l5", "gkZPUEo70rXQCKyjscI0Q4FDJvRHERzY78Kr8fiS");
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
//            installation.put("userName", ParseUser.getCurrentUser().getUsername());
            installation.saveInBackground();

            _reset = false;
        }


//        System.out.println(ParseUser.getCurrentUser().getUsername());
//        System.out.println(installation.getString("ObjectId"));
//        System.out.println(installation.getString("deviceToken"));
//        System.out.println(installation.getString("localeIdentifier"));
//        System.out.println(installation.getString("parseVersion"));
//        System.out.println(installation.getString("appIdentifier"));
//        System.out.println(installation.getString("appName"));
//        System.out.println(installation.getDate("updatedAt"));
//        System.out.println(installation.getString("deviceType"));
//        System.out.println(installation.getString("pushType"));
//        System.out.println(installation.getString("installationId"));
//        System.out.println(installation.getString("appVersion"));
//        System.out.println(installation.getString("timeZone"));
//        System.out.println(installation.getDate("createdAt"));

//        ParseQuery pushQuery = ParseInstallation.getQuery();
//        pushQuery.whereEqualTo("userName", ParseUser.getCurrentUser().getUsername());

        // Send push notification to query
//        ParsePush push = new ParsePush();
//        push.setQuery(pushQuery); // Set our Installation query
//        push.setMessage("This is a test notification");
//        push.sendInBackground();

        _recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        _recyclerView.setHasFixedSize(false);

        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, 1);
        // Attach the layout manager to the recycler view
        _recyclerView.setLayoutManager(gridLayoutManager);
        _recyclerView.setItemAnimator(new DefaultItemAnimator());
        SpacesItemDecoration decoration = new SpacesItemDecoration(25);
        _recyclerView.addItemDecoration(decoration);
        final int fabMargin = getResources().getDimensionPixelSize(R.dimen.fab_margin);
        _recyclerView.addOnScrollListener(new MyRecyclerScroll() {
            @Override
            public void show() {
                _fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide() {
                _fab.animate().translationY(_fab.getHeight() + fabMargin).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });

        _rAdapter = new ReminderAdapter(MainActivity.this, _data);
//        _recyclerView.setAdapter(_rAdapter);

        _fab = (FloatingActionButton) findViewById(R.id.fab);
        _fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        _fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        _fabGrow = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_grow);
        _fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        _fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        _rotateForward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        _rotateBackward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
//        _fabOpenMore = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open_more);
        _fab.startAnimation(_fabGrow);
        _fab.setOnClickListener(this);
        _fab1.setOnClickListener(this);
        _fab2.setOnClickListener(this);

        _currentUser = ParseUser.getCurrentUser();
        if(_currentUser!= null) {
            getData();
        }
        else {
            LayoutInflater inflater = getLayoutInflater();
            View view;
            view = inflater.inflate(R.layout.log_layout, null);
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            _dialog = d.create();
            _dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                    finish();
                }
            });
            _dialogView = view;
            _dialog.setView(view);
            _dialog.show();
        }

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(_recyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    deleteItemFromCloud(position);
                                    _data.remove(position);
                                    _rAdapter.notifyItemRemoved(position);
                                }
//                                _rAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                System.out.println(_data.size());
                                for (int position : reverseSortedPositions) {
                                    deleteItemFromCloud(position);
                                    _data.remove(position);
                                    _rAdapter.notifyItemRemoved(position);
                                }
//                                _rAdapter.notifyDataSetChanged();
                            }
                        });

        _recyclerView.addOnItemTouchListener(swipeTouchListener);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab1:
//                animateFAB1();
                animateFAB();
                startActivityForResult(new Intent(getApplicationContext(), TaskMenu.class), 1);
                break;
            case R.id.fab2:
//                animateFAB2();
                animateFAB();
                break;
        }
    }

    public void animateFAB() {
        if (_isFabOpen) {
            _fab.startAnimation(_rotateBackward);
            _fab1.startAnimation(_fabClose);
            _fab2.startAnimation(_fabClose);
            _fab1.setClickable(false);
            _fab2.setClickable(false);
            _isFabOpen = false;
        }
        else {
            _fab.startAnimation(_rotateForward);
            _fab1.startAnimation(_fabOpen);
            _fab2.startAnimation(_fabOpen);
            _fab1.setClickable(true);
            _fab2.setClickable(true);
            _isFabOpen = true;
        }
    }

    public void getData() {
        _data.clear();
        ParseQuery<Reminder> query = ParseQuery.getQuery(Reminder.class);
        if(!_currentCategory.equals("All")){
            query.whereEqualTo("category",_currentCategory);
        }
        query.whereEqualTo("user", _currentUser.getUsername());
        query.orderByDescending("updatedAt");
        query.findInBackground(new FindCallback<Reminder>() {
            @Override
            public void done(List<Reminder> reminders, ParseException e) {
                if (e == null) {
                    for (Reminder r : reminders) {
                        _data.add(r);
                    }
                }
                _rAdapter.updateAdapter(_data);
                _recyclerView.setAdapter(_rAdapter);
            }

        });
    }

    public void deleteItemFromCloud(int position) {
        String id = _data.get(position).getId();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ReminderObj");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.deleteInBackground();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                _rAdapter.updateAdapter(_data);
                _rAdapter.notifyItemInserted(0);
                _recyclerView.smoothScrollToPosition(0);
//                _recyclerView.setAdapter(_rAdapter);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // NAV DRAWER
    private void setUpToolbar() {
        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (_toolbar != null) {
            setSupportActionBar(_toolbar);
            getSupportActionBar().setTitle(_dayOfWeek + " " + _dayOfMonth);
        }
    }

    private void setUpNavDrawer() {
        if (_toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            _toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
            _drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            _toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _nv = (NavigationView) findViewById(R.id.navigationView);
                    _drawerLayout.openDrawer(GravityCompat.START);

                    _nv.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            for(int i = 0; i < _nv.getMenu().size(); i++){
                                if(_nv.getMenu().getItem(i).getTitle().toString().equals(_currentCategory)){
                                    _nv.getMenu().getItem(i).setChecked(false);
                                }
                            }
                            item.setChecked(true);
                            _currentCategory = item.getTitle().toString();
                            getData();
                            return true;
                        }
                    });
                    if(_currentCategory.equals("All")){
                        _nv.getMenu().getItem(0).setChecked(true);
                    }

                    TextView text = (TextView) findViewById(R.id.user);
                    text.setText(_currentUser.getString("Name"));
                    ParseQuery<Category> query = ParseQuery.getQuery(Category.class);
                    query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
                    query.findInBackground(new FindCallback<Category>() {
                        @Override
                        public void done(List<Category> categories, ParseException e) {
                            if (e == null) {
                                _menu = _nv.getMenu();
                                for (Category c : categories) {
                                    boolean exists = false;
                                    for(int x = 0; x < _menu.size(); x++){
                                        if(_menu.getItem(x).getTitle().toString().equals(c.getCategory())){
                                            exists = true;
                                            break;
                                        }
                                    }
                                    if(exists){ continue;}
                                    MenuItem item = _menu.add(c.getCategory());
                                    if(_currentCategory.equals(item.getTitle())){ item.setChecked(true);}
                                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                        @Override
                                        public boolean onMenuItemClick(MenuItem item) {
                                            for(int i = 0; i < _menu.size(); i++){
                                                if(_menu.getItem(i).getTitle().toString().equals(_currentCategory)){
                                                    _menu.getItem(i).setChecked(false);
                                                }
                                            }
                                            item.setChecked(true);
                                            _currentCategory = item.getTitle().toString();
                                            getData();
                                            return true;
                                        }
                                    });
                                }
                            }
                        }
                    });


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
    public void drawerButton(View v) {
        Button b = (Button)v;
        if (b.getText().equals("LOG OUT")) {
            ParseUser.logOut();
            _currentUser = null;
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
    public void sign_up(View v){
        String name, username, password;
        EditText et;
        et = (EditText) _dialogView.findViewById(R.id.logName);
        name = et.getText().toString();
        et = (EditText) _dialogView.findViewById(R.id.logUsername);
        username = et.getText().toString();
        et = (EditText) _dialogView.findViewById(R.id.logPassword);
        password = et.getText().toString();
        if(name.equals("")) {
            Toast.makeText(getApplicationContext(), R.string.noNameError, Toast.LENGTH_LONG).show();
            return;
        }
        else if(username.equals("")) {
            Toast.makeText(getApplicationContext(), R.string.noUsernameError, Toast.LENGTH_LONG).show();
            return;
        }
        else if(password.equals("")){
            Toast.makeText(getApplicationContext(), R.string.noPasswordError, Toast.LENGTH_LONG).show();
            return;
        }
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.put("Name", name);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Sign Up Successful, Logging In", Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("ERROR");
                }
            }
        });

        SystemClock.sleep(1000);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                _currentUser = user;
                _dialog.dismiss();
                _dialog = null;
                _dialogView = null;
                getData();
            }
        });
    }

    public void log_in(View v){
        String username, password;
        EditText et;
        et = (EditText) _dialogView.findViewById(R.id.logUsername);
        username = et.getText().toString();
        et = (EditText) _dialogView.findViewById(R.id.logPassword);
        password = et.getText().toString();
        if(username.equals("")) {
            Toast.makeText(getApplicationContext(), R.string.noUsernameError, Toast.LENGTH_LONG).show();
            return;
        }
        else if(password.equals("")) {
            Toast.makeText(getApplicationContext(), R.string.noPasswordError, Toast.LENGTH_LONG).show();
            return;
        }
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    _currentUser = user;
                    _dialog.dismiss();
                    _dialog = null;
                    _dialogView = null;
                    getData();
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.logInError, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void update(View v){
        EditText et = (EditText) _dialogView.findViewById(R.id.logName);
        et.setVisibility(View.VISIBLE);
        Button b = (Button) _dialogView.findViewById(R.id.logButton);
        b.setVisibility(View.GONE);
        b = (Button) _dialogView.findViewById(R.id.signButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_up(v);
            }
        });
        et.requestFocus();
    }
}
