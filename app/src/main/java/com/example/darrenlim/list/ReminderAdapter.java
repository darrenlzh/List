package com.example.darrenlim.list;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    private ArrayList<Reminder> _data;
    private static Context _context;
    private Boolean _isChecklist;

            // Provide a reference to the views for each data item
            // Complex data items may need more than one view per item, and
            // you provide access to all the views for a data item in a view holder

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public CardView cardView;
        public TextView detailTextView;
        public ViewHolder(View v) {
            super(v);
            _context = v.getContext();
            cardView = (CardView) v.findViewById(R.id.card_v);
            textView = (TextView) v.findViewById(R.id.info_title);
            detailTextView = (TextView) v.findViewById(R.id.text_details);
//            cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(_context, CardDetails.class);
//                    _context.startActivity(intent);
//                }
//            });

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ReminderAdapter(Context context, ArrayList<Reminder> data) {
        _context = context;
        _data = data;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ReminderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        _isChecklist =  _data.get(position).isChecklist();
        final int pos = position;
        holder.textView.setText(_data.get(position).getTitle());
        if(_isChecklist) {
            String itemsText = "";
            JSONArray jsonArray = _data.get(position).getItems();
            ArrayList<String> arrayList = new ArrayList<>();
            int length = jsonArray.optJSONArray(0).length();
            if(jsonArray!=null){
                for(int i=0;i<length;i++){
                    arrayList.add(jsonArray.optJSONArray(0).optString(i));
                }
            }
            for(int i=0; i<arrayList.size(); i++) {
                itemsText += "- " + arrayList.get(i) + "\n";
            }
            holder.detailTextView.setText(itemsText);
        }
        else holder.detailTextView.setVisibility(View.GONE);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(_context, CardDetails.class);
                    intent.putExtra("intPos", pos);
                    _context.startActivity(intent);
                }
            });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return _data.size();
    }

    public void updateAdapter(ArrayList<Reminder> data){
        _data = data;
    }
}