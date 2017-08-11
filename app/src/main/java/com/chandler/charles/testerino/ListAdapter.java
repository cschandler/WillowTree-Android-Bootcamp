package com.chandler.charles.testerino;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Collection;
import java.util.List;

/**
 * Created by charleschandler on 8/8/17.
 */

public class ListAdapter extends RecyclerView.Adapter {

    private List<ForecastViewModel> data;
    private Context context;

    public ListAdapter(List<ForecastViewModel> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ListRow(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (data == null) {
            return;
        }

        ((ListRow) holder).bind(context, data.get(position));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void updateData(List<ForecastViewModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
