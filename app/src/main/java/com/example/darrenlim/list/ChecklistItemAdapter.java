package com.example.darrenlim.list;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChecklistItemAdapter extends RecyclerView.Adapter<ChecklistItemAdapter.ViewHolder> {
    private ArrayList<String> _items;
    private ArrayList<Boolean> _itemsTruth;
    private static Context _context;
    private int _pos;
    private static ImageButton _imageButton;
    private static EditText _editText;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CheckBox checkBox;
        public EditText editText;
        public ImageButton imageButton;
        public ViewHolder(View v) {
            super(v);
            _context = v.getContext();
            checkBox = (CheckBox) v.findViewById(R.id.check_box);
            editText = (EditText) v.findViewById(R.id.check_text);
            imageButton = (ImageButton) v.findViewById(R.id.add_item_button);
            _imageButton = imageButton;
            _editText = editText;
//            cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(_context, CardDetails.class);
//                    _context.startActivity(intent);
//                }
//            });

        }

        public int myGetPosition() {
            return getAdapterPosition();
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChecklistItemAdapter(Context context, ArrayList<String> items, ArrayList<Boolean> itemsTruth) {
        _context = context;
        _items = items;
        _itemsTruth = itemsTruth;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChecklistItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.check_box, null);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        _pos = position;
//        holder.textView.setText(_data.get(position).getTitle());
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(_context, CardDetails.class);
//                intent.putExtra("intPos", pos);
//                _context.startActivity(intent);
//            }
//        });

        holder.checkBox.setChecked(_itemsTruth.get(position));
        holder.editText.setText(_items.get(position));
        if(position == getItemCount()-1) holder.editText.requestFocus();
        if(position != 0) holder.editText.setHint(" ");
//        _imageButton.setOnClickListener();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return _items.size();
    }

    public void updateChecklistAdapter(){

//        _imageButton.setVisibility(View.GONE);
//        _editText.requestFocus();
    }

}