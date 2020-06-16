package com.himanshumauri.learning;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class HomeAdapter extends FirebaseRecyclerAdapter<ListModel, HomeAdapter.HomeViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public HomeAdapter(@NonNull FirebaseRecyclerOptions<ListModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull HomeViewHolder holder, int position, @NonNull ListModel model) {
        Glide.with(holder.homeImageView.getContext()).load(model.getTitleImage()).fitCenter().placeholder(R.drawable.ic_image_holder).into(holder.homeImageView);
        holder.homeTitle.setText(model.getTitleName());
        holder.homeDesc.setText(model.getTitleDesc());
        holder.homeTags.setText(model.getTitleTags());

        final String pId = getRef(position).getKey();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Category.class);
                intent.putExtra("pId",pId);
                v.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_home, parent,false);
        return new HomeViewHolder(view);
    }

    static class HomeViewHolder extends RecyclerView.ViewHolder{
        ImageView homeImageView;
        TextView homeTitle,homeDesc,homeTags;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            homeImageView = itemView.findViewById(R.id.HomeCategoryIv);
            homeTitle = itemView.findViewById(R.id.HomeCategoryTitleTv);
            homeTitle.setSelected(true);
            homeDesc = itemView.findViewById(R.id.HomeCategoryDescTv);
            homeDesc.setSelected(true);
            homeTags = itemView.findViewById(R.id.HomeCategoryTagsTv);
            homeTags.setSelected(true);

        }
    }
}
