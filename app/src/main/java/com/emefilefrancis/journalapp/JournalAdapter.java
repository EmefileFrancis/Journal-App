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
        String journalBody = journalEntry.getJournalBody();
        holder.tvJournalBodyPart.setText(getMaxDisplayJournalBody(journalBody));
        holder.tvLabel.setText(journalEntry.getJournalLabel());
        int colorInt = getColorForColorCircle(journalEntry.getJournalColor());
        holder.clViewholder.setBackgroundColor(colorInt);
    }

    public String getMaxDisplayJournalBody(String journalBody){
        String subJournalBody = "";
        if(journalBody.length() >= 500){
            subJournalBody = journalBody.substring(0, 450);
        }else{
            subJournalBody = journalBody;
        }
        return subJournalBody;
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

        if (color.equals("orange")) {
            colorInt = ContextCompat.getColor(mContext, R.color.materialOrange);
        } else if (color.equals("blue")) {
            colorInt = ContextCompat.getColor(mContext, R.color.materialBlue);
        } else if (color.equals("lemon")) {
            colorInt = ContextCompat.getColor(mContext, R.color.materialLemon);
        } else if (color.equals("yellow")) {
            colorInt = ContextCompat.getColor(mContext, R.color.materialYellow);
        } else if (color.equals("pink")) {
            colorInt = ContextCompat.getColor(mContext, R.color.materialPink);
        } else if (color.equals("purple")) {
            colorInt = ContextCompat.getColor(mContext, R.color.materialPurple);
        } else if (color.equals("dark_blue")) {
            colorInt = ContextCompat.getColor(mContext, R.color.materialDarkBlue);
        } else if (color.equals("light_blue")) {
            colorInt = ContextCompat.getColor(mContext, R.color.materialLightBlue);
        } else if (color.equals("white")) {
            colorInt = ContextCompat.getColor(mContext, R.color.white);
        }


        return colorInt;
    }

    class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ConstraintLayout clViewholder;
        TextView tvJournalTitle;
        TextView tvJournalBodyPart;
        TextView tvLabel;


        public JournalViewHolder(View itemView) {
            super(itemView);

            clViewholder = itemView.findViewById(R.id.vh_constraint_layout);
            tvJournalTitle = itemView.findViewById(R.id.tv_journal_title);
            tvJournalBodyPart = itemView.findViewById(R.id.tv_journal_body_part);
            tvLabel = itemView.findViewById(R.id.label_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int journalId = journalEntries.get(getAdapterPosition()).getId();
            itemClickListener.onItemClickListener(journalId);
        }
    }
}
