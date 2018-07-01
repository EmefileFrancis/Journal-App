package com.emefilefrancis.journalapp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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
    private List<JournalEntry> mJournalEntries;


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
        JournalEntry journalEntry = mJournalEntries.get(position);

        holder.mJournalTitle.setText(journalEntry.getJournalTitle());
        String journalBody = journalEntry.getJournalBody();
        holder.mJournalBodyPart.setText(getMaxDisplayJournalBody(journalBody));
        holder.mLabel.setText(journalEntry.getJournalLabel());
        int colorInt = getColorForColorCircle(journalEntry.getJournalColor());
        holder.mViewholder.setBackgroundColor(colorInt);
    }

    private String getMaxDisplayJournalBody(String journalBody){
        String subJournalBody;
        if(journalBody.length() >= 500){
            subJournalBody = journalBody.substring(0, 450);
        }else{
            subJournalBody = journalBody;
        }
        return subJournalBody;
    }

    @Override
    public int getItemCount() {
        if(mJournalEntries == null){ return 0; }
        return mJournalEntries.size();
    }

    public List<JournalEntry> getJournalEntries() { return mJournalEntries; }

    public void setJournalEntries(List<JournalEntry> journalEntries) {
        this.mJournalEntries = journalEntries;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    private int getColorForColorCircle(String color){
        int colorInt = 0;

        switch (color) {
            case "orange":
                colorInt = ContextCompat.getColor(mContext, R.color.materialOrange);
                break;
            case "blue":
                colorInt = ContextCompat.getColor(mContext, R.color.materialBlue);
                break;
            case "lemon":
                colorInt = ContextCompat.getColor(mContext, R.color.materialLemon);
                break;
            case "yellow":
                colorInt = ContextCompat.getColor(mContext, R.color.materialYellow);
                break;
            case "pink":
                colorInt = ContextCompat.getColor(mContext, R.color.materialPink);
                break;
            case "purple":
                colorInt = ContextCompat.getColor(mContext, R.color.materialPurple);
                break;
            case "dark_blue":
                colorInt = ContextCompat.getColor(mContext, R.color.materialDarkBlue);
                break;
            case "light_blue":
                colorInt = ContextCompat.getColor(mContext, R.color.materialLightBlue);
                break;
            case "white":
                colorInt = ContextCompat.getColor(mContext, R.color.white);
                break;
        }


        return colorInt;
    }

    class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ConstraintLayout mViewholder;
        TextView mJournalTitle;
        TextView mJournalBodyPart;
        TextView mLabel;


        public JournalViewHolder(View itemView) {
            super(itemView);

            mViewholder = itemView.findViewById(R.id.vh_constraint_layout);
            mJournalTitle = itemView.findViewById(R.id.tv_journal_title);
            mJournalBodyPart = itemView.findViewById(R.id.tv_journal_body_part);
            mLabel = itemView.findViewById(R.id.label_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int journalId = mJournalEntries.get(getAdapterPosition()).getId();
            itemClickListener.onItemClickListener(journalId);
        }
    }
}
