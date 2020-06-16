package com.himanshumauri.learning;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.Objects;

public class CategoryAdapter extends FirebaseRecyclerAdapter<ListModel, CategoryAdapter.CategoryViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CategoryAdapter(@NonNull FirebaseRecyclerOptions<ListModel> options) {
        super(options);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull ListModel model) {
        Glide.with(holder.homeImageView.getContext()).load(model.getTitleImage()).fitCenter().placeholder(R.drawable.ic_image_holder).into(holder.homeImageView);
        holder.homeTitle.setText(model.getTitleName());
        holder.homeDesc.setText(model.getTitleDesc());
        holder.homeTags.setText(model.getTitleTags());

        final String pId = getRef(position).getKey();
        final String ParentId = Objects.requireNonNull(Objects.requireNonNull(getRef(position).getParent()).getParent()).getKey();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SubCategory.class);
                intent.putExtra("pId", pId);
                intent.putExtra("ParentKey", ParentId);
                v.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category, parent, false);
        return new CategoryViewHolder(view);
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView homeImageView;
        TextView homeTitle, homeDesc, homeTags;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            homeImageView = itemView.findViewById(R.id.CatCategoryIv);
            homeTitle = itemView.findViewById(R.id.CatCategoryTitleTv);
            homeTitle.setSelected(true);
            homeDesc = itemView.findViewById(R.id.CatCategoryDescTv);
            homeDesc.setSelected(true);
            homeTags = itemView.findViewById(R.id.CatCategoryTagsTv);
            homeTags.setSelected(true);
        }
    }
    class MyAdViewHolder extends RecyclerView.ViewHolder{

        public MyAdViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
