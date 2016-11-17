package com.erkkiperkele.master_android.utility;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.erkkiperkele.master_android.R;

public class ResultViewHolder extends RecyclerView.ViewHolder {
    private View _resultItemView;

    public ResultViewHolder(View itemView) {
        super(itemView);

        _resultItemView = itemView;
    }

    public void setText(String resultText) {
        TextView resultTextView = (TextView) _resultItemView.findViewById(R.id.recycler_result_text);
        resultTextView.setText(resultText);
    }
}
