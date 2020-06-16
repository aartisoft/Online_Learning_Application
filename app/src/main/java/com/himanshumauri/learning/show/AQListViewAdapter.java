package com.himanshumauri.learning.show;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.himanshumauri.learning.R;

public class AQListViewAdapter extends FirebaseRecyclerAdapter<AQModel,AQListViewAdapter.AQListViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AQListViewAdapter(@NonNull FirebaseRecyclerOptions<AQModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AQListViewHolder holder, int position, @NonNull AQModel model) {
        holder.QuestionView.setText(model.getQuestion());
        final String uID = model.getUploader();
        final String question = model.getQuestion();
        final String answer = model.getAnswer();
        final String verified = model.getVerified();

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
                Intent intent = new Intent(v.getContext(), ShowQuestionAnswer.class);
                intent.putExtra("question",question);
                intent.putExtra("answer",answer);
                intent.putExtra("ID",uID);
                v.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public AQListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_question,parent,false);
        return new AQListViewHolder(view);
    }

    static class AQListViewHolder extends RecyclerView.ViewHolder{
        private TextView QuestionView;
        public AQListViewHolder(@NonNull View itemView) {
            super(itemView);
            QuestionView = itemView.findViewById(R.id.QuestionView);
            QuestionView.setSelected(true);
        }
    }
}
