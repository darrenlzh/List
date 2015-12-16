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
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by darrenlim on 12/15/15.
 */
public class CheckListMenu extends AppCompatActivity {

    private Toolbar _toolbar;
    private RecyclerView _recyclerView;
    private ChecklistItemAdapter _chAdapter;
    private ArrayList<String> _items = new ArrayList<>();
    private ArrayList<Boolean> _itemsTruth = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list_menu);
        setUpToolbar();

        _items.add("");
        _itemsTruth.add(false);
        _recyclerView = (RecyclerView) this.findViewById(R.id.checklist_recycler_view);
        _recyclerView.setHasFixedSize(false);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
        _recyclerView.setItemAnimator(new DefaultItemAnimator());
        _chAdapter = new ChecklistItemAdapter(this, _items, _itemsTruth);
        _recyclerView.setAdapter(_chAdapter);


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
//                delete(_recyclerView.getChildAdapterPosition());
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
    }
}
