package com.emefilefrancis.journalapp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emefilefrancis.journalapp.database.JournalEntry;

import java.util.List;

/**
 * Created by SP on 6/26/2018.
 */

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {

    private final ItemClickListener itemClickListener;
    private Context mContext;
    private List<JournalEntry> journalEntries;

    public JournalAdapter(Context context, ItemClickListener listener){
        itemClickListener = listener;
        mContext = context;
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.journal_layout, parent, false);
        return new JournalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        JournalEntry journalEntry = journalEntries.get(position);

        holder.tvJournalTitle.setText(journalEntry.getJournalTitle());
        holder.tvJournalBodyPart.setText(journalEntry.getJournalBody());

        GradientDrawable colorCircle = (GradientDrawable) holder.tvColor.getBackground();
        int colorInt = getColorForColorCircle(journalEntry.getJournalColor());
        colorCircle.setColor(colorInt);
    }

    @Override
    public int getItemCount() {
        if(journalEntries == null){
            return 0;
        }
        return journalEntries.size();
    }

    public List<JournalEntry> getJournalEntries() { return journalEntries; }

    public void setJournalEntries(List<JournalEntry> journalEntries) {
        this.journalEntries = journalEntries;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    private int getColorForColorCircle(String color){
        int colorInt = 0;

        if(color.equals("red")){
            colorInt = ContextCompat.getColor(mContext, R.color.materialRed);
        }else if(color.equals("green")){
            colorInt = ContextCompat.getColor(mContext, R.color.materialGreen);
        }else if(color.equals("yellow")){
            colorInt = ContextCompat.getColor(mContext, R.color.materialYellow);
        }else if(color.equals("orange")){
            colorInt = ContextCompat.getColor(mContext, R.color.materialOrange);
        }

        return colorInt;
    }

    class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvJournalTitle;
        TextView tvJournalBodyPart;
        TextView tvColor;

        public JournalViewHolder(View itemView) {
            super(itemView);

            tvJournalTitle = itemView.findViewById(R.id.tv_journal_title);
            tvJournalBodyPart = itemView.findViewById(R.id.tv_journal_body_part);
            tvColor = itemView.findViewById(R.id.tv_color);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int journalId = journalEntries.get(getAdapterPosition()).getId();
            itemClickListener.onItemClickListener(journalId);
        }
    }
}
