package com.erkkiperkele.master_android.utility;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.erkkiperkele.master_android.R;
import com.erkkiperkele.master_android.entity.JResult;

import java.text.NumberFormat;
import java.util.Locale;

public class ResultViewHolder extends RecyclerView.ViewHolder {
    private View _resultItemView;
    private Context _context;

    public ResultViewHolder(View itemView) {
        super(itemView);

        _resultItemView = itemView;
    }

    public void setResult(JResult result, Context context){
        _context = context;

        this
                .setDate(result.getExecutionDateTimePretty())
                .setExecutionTime(result.getExecutionTimeInS())
                .setNumberOfOperations(result.getTaskSize())
                .setNumberOfThreads(result.getThreadsCount())
        ;
    }

    private ResultViewHolder setDate(String resultText) {
        TextView text = (TextView) _resultItemView.findViewById(R.id.pi_result_date);
        text.setText(resultText);

        return this;
    }

    private ResultViewHolder setExecutionTime(double executionTime) {
        String formattedNumber = NumberFormat
                .getNumberInstance(Locale.getDefault())
                .format(executionTime);

        TextView text = (TextView) _resultItemView.findViewById(R.id.pi_result_execution_time);
        text.setText(new StringBuilder()
                .append(formattedNumber)
                .append(_context.getString(R.string.result_execution_time_unit))
                .toString());

        return this;
    }

    private ResultViewHolder setNumberOfOperations(int taskSize) {
        String formattedNumber = NumberFormat
                .getNumberInstance(Locale.getDefault())
                .format(taskSize);

        TextView text = (TextView) _resultItemView.findViewById(R.id.pi_result_number_of_operations);
        text.setText(new StringBuilder()
                .append(formattedNumber)
                .append(_context.getString(R.string.result_task_size_unit))
                .toString());

        return this;
    }

    private ResultViewHolder setNumberOfThreads(int threadsCount) {
        String formattedNumber = NumberFormat
                .getNumberInstance(Locale.getDefault())
                .format(threadsCount);

        TextView text = (TextView) _resultItemView.findViewById(R.id.pi_result_threads_count);
        text.setText(new StringBuilder()
                .append(formattedNumber)
                .append(_context.getString(R.string.result_threads_unit))
                .toString());

        return this;
    }
}
