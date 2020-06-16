package com.himanshumauri.learning.menu;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.himanshumauri.learning.R;

public class PerformanceAdapter extends FirebaseRecyclerAdapter<PerfModel, PerformanceAdapter.PerformanceViewHolder> {
    private int count = 0;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PerformanceAdapter(@NonNull FirebaseRecyclerOptions<PerfModel> options) {
        super(options);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onBindViewHolder(@NonNull final PerformanceViewHolder holder, int position, @NonNull final PerfModel model) {
        holder.TopicName.setText(model.getTitle());
        DatabaseReference reference = getRef(position);
        final String parent = reference.getKey();
        DatabaseReference grandref = getRef(position).getParent();
        assert grandref != null;
        final String grandParent = grandref.getKey();

        reference.child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    count =(int) dataSnapshot.getChildrenCount();
                    holder.NumStud.setText(String.valueOf(count));
                }else {
                    count = 0;
                    holder.NumStud.setText(String.valueOf(count));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent StudentIntent = new Intent(v.getContext(),StudentsList.class);
                StudentIntent.putExtra("parent",parent);
                StudentIntent.putExtra("grand",grandParent);
                v.getContext().startActivity(StudentIntent);
            }
        });
    }

    @NonNull
    @Override
    public PerformanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_performance_mcq_topic,parent,false);
        return new PerformanceViewHolder(view);
    }

    class PerformanceViewHolder extends RecyclerView.ViewHolder{
        private TextView NumStud,TopicName;
        public PerformanceViewHolder(@NonNull View itemView) {
            super(itemView);
            NumStud = itemView.findViewById(R.id.StudentsNo);
            TopicName = itemView.findViewById(R.id.MCQTopicName);
        }
    }
}