package com.chandler.charles.testerino;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by charleschandler on 8/8/17.
 */

public final class ListRow extends RecyclerView.ViewHolder {

    private TextView dateTextView;
    private TextView conditionTextView;
    private TextView highTempTextView;
    private TextView lowTempTextView;
    private ImageView icon;

    public ListRow(View itemView) {
        super(itemView);

        dateTextView = (TextView) itemView.findViewById(R.id.date);
        conditionTextView = (TextView) itemView.findViewById(R.id.condition);
        highTempTextView = (TextView) itemView.findViewById(R.id.highTemp);
        lowTempTextView = (TextView) itemView.findViewById(R.id.lowTemp);
        icon = (ImageView) itemView.findViewById(R.id.iconImageView);
    }

    public void bind(Context context, ForecastViewModel viewModel) {
        dateTextView.setText(viewModel.getDate());
        conditionTextView.setText(viewModel.getCondition());
        highTempTextView.setText(viewModel.getHighTemp());
        lowTempTextView.setText(viewModel.getLowTemp());
        Picasso.with(context)
                .load(viewModel.getIconURLString())
                .into(icon);
    }

}
