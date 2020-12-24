package com.moadab.colornotesapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moadab.colornotesapp.Model.Note;
import com.moadab.colornotesapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private OnNoteListener mOnNoteListener;
    Context mContext;
     List<Note> mNotes;


    public NoteAdapter(Context mContext, List<Note> mNotes , OnNoteListener onNoteListener) {
        this.mContext = mContext;
        this.mNotes = mNotes;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view , mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.backColor.setBackgroundColor(holder.view.getResources().getColor(randomColor(),null));
        holder.txtTitle.setText(mNotes.get(position).getNoteTitle());
        holder.txtDesc.setText(mNotes.get(position).getNoteDesc());
        holder.txtDate.setText(mNotes.get(position).getNoteCraeted());
        holder.txtDateModified.setText(mNotes.get(position).getNoteModified());
        holder.id.setText(mNotes.get(position).getNoteId());

        switch (mNotes.get(position).getNoteFavorite()) {
            case "true":
                holder.star.setVisibility(View.VISIBLE);
                holder.star.setBackgroundResource(R.drawable.ic_star);
                break;

            case "false":
                holder.star.setVisibility(View.INVISIBLE);
                holder.star.setBackgroundResource(R.drawable.ic_outline_star);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        TextView id;
        public TextView txtTitle;
        public TextView txtDesc;
        public TextView txtDate;
        public TextView txtDateModified;
        public ImageView star;
        public RelativeLayout backColor;
        View view;
        OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView , OnNoteListener onNoteListener) {
            super(itemView);

            id = itemView.findViewById(R.id.id);
            backColor = itemView.findViewById(R.id.main);
            txtTitle = itemView.findViewById(R.id.n_title);
            txtDesc = itemView.findViewById(R.id.n_note);
            txtDate = itemView.findViewById(R.id.n_date);
            txtDateModified = itemView.findViewById(R.id.n_date_m);
            star = itemView.findViewById(R.id.n_star);
            view = itemView;
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }

    private int randomColor(){

        List<Integer> colorCode=new ArrayList<>();
        colorCode.add(R.color.light_blue);
        colorCode.add(R.color.lighte_purple);
        colorCode.add(R.color.light_green);
        colorCode.add(R.color.light_pick);
        colorCode.add(R.color.light_purple);
        colorCode.add(R.color.green);
        colorCode.add(R.color.pink);
        colorCode.add(R.color.dark_green);
        colorCode.add(R.color.v_light_blue);
        colorCode.add(R.color.v_light_green);
        colorCode.add(R.color.light_yellow);

        Random randomColor=new Random();
        int color = randomColor.nextInt(colorCode.size());
        return colorCode.get(color);
    }
    
}
