package com.himanshumauri.learning.show;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.himanshumauri.learning.R;

public class NotesAdapter extends FirebaseRecyclerAdapter<NotesModel, NotesAdapter.NotesViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NotesAdapter(@NonNull FirebaseRecyclerOptions<NotesModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotesViewHolder holder, int position, @NonNull NotesModel model) {
        holder.TitleView.setText(model.getTitle());
        final String body = model.getBody();
        final String title = model.getTitle();
        final String uploader = model.getUploader();
        final String verified = model.getVerified();
        final String video = model.getVideo();

        if (verified != null){
            if (verified.equals("false")){
                holder.itemView.setVisibility(View.GONE);
            }else {
                holder.itemView.setVisibility(View.VISIBLE);
            }
        }else {
            holder.itemView.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ShowNotes.class);
                intent.putExtra("title",title);
                intent.putExtra("body",body);
                intent.putExtra("uploader",uploader);
                intent.putExtra("video",video);
                v.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_question,parent,false);
        return new NotesViewHolder(view);
    }

    static class NotesViewHolder extends RecyclerView.ViewHolder{
        private TextView TitleView;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            TitleView = itemView.findViewById(R.id.QuestionView);
            TitleView.setSelected(true);
        }
    }
}
