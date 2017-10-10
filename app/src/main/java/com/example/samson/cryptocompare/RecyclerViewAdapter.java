package com.example.samson.cryptocompare;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by SAMSON on 10/7/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{

    Context context;
    List<CustomObject> customObjects;

    public RecyclerViewAdapter(Context context, List<CustomObject> customObjects) {
        this.context = context;
        this.customObjects = customObjects;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_view_item, parent, false);


        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        CustomObject object = customObjects.get(position);

        holder.textView.setText("Hello  " + object.currency + " BTC value: " + object.getBTC() + "  ETC value: " + object.getETC());
    }

    @Override
    public int getItemCount() {
        return customObjects.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.itemView);
        }
    }
}
