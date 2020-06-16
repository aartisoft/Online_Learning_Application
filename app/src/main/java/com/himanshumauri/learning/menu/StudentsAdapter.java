package com.himanshumauri.learning.menu;

import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himanshumauri.learning.R;

import java.util.Calendar;
import java.util.Locale;

public class StudentsAdapter extends FirebaseRecyclerAdapter<StudentsModel,StudentsAdapter.StudentViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public StudentsAdapter(@NonNull FirebaseRecyclerOptions<StudentsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final StudentViewHolder holder, int position, @NonNull StudentsModel model) {
        holder.Marks.setText(String.valueOf(model.getScore()));

        final String User = model.getStudent();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(User);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                holder.Name.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference time = getRef(position);
        String timeSnap = time.getKey();

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        assert timeSnap != null;
        calendar.setTimeInMillis(Long.parseLong(timeSnap));
        String TestTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        holder.Date.setText(TestTime);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(v.getContext(),Profile.class);
                profileIntent.putExtra("uploader",User);
                v.getContext().startActivity(profileIntent);
            }
        });
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_students_list,parent,false);
        return new StudentViewHolder(view);
    }

    class StudentViewHolder extends RecyclerView.ViewHolder{
        private TextView Marks,Name,Date;
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            Marks = itemView.findViewById(R.id.MarksObtained);
            Name = itemView.findViewById(R.id.StudentName);
            Date = itemView.findViewById(R.id.StudentDate);
        }
    }
}
