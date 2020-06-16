package com.himanshumauri.learning;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himanshumauri.learning.show.ContentsShow;

import java.util.Objects;

public class TopicViewAdapter extends FirebaseRecyclerAdapter<TopicModel, TopicViewAdapter.TopicViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TopicViewAdapter(@NonNull FirebaseRecyclerOptions<TopicModel> options) {
        super(options);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onBindViewHolder(@NonNull final TopicViewHolder holder, int position, @NonNull final TopicModel model) {
        String Approval = model.getApproved();
        if (Approval.equals("true")){
            holder.topicTitle.setText(model.getTitle());
            final String uID = model.getUid();

            final String pId = getRef(position).getKey();
            final String pType = Objects.requireNonNull(getRef(position).getParent()).getKey();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(uID);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String uploader = "Uploaded By :- "+ name;
                    holder.topicUploader.setText(uploader);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ContentsShow.class);
                    intent.putExtra("pId",pId);
                    intent.putExtra("pType",pType);
                    intent.putExtra("uID",uID);
                    v.getContext().startActivity(intent);
                }
            });
        }else {
            holder.itemView.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_topic,parent,false);
        return new TopicViewHolder(view);
    }

    class TopicViewHolder extends RecyclerView.ViewHolder{
        TextView topicTitle, topicUploader;
        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            topicTitle = itemView.findViewById(R.id.TopicTitleView);
            topicUploader = itemView.findViewById(R.id.UploadedBy);
        }
    }
}
