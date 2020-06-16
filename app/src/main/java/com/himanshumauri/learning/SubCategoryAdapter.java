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

public class SubCategoryAdapter extends FirebaseRecyclerAdapter<ListModel, SubCategoryAdapter.SubCategoryViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public SubCategoryAdapter(@NonNull FirebaseRecyclerOptions<ListModel> options) {
        super(options);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position, @NonNull ListModel model) {
        Glide.with(holder.homeImageView.getContext()).load(model.getTitleImage()).fitCenter().placeholder(R.drawable.ic_image_holder).into(holder.homeImageView);
        holder.homeTitle.setText(model.getTitleName());

        //---------Passing Referance --------------------------------

        final String pId = getRef(position).getKey();
        final String ParentId = Objects.requireNonNull(Objects.requireNonNull(getRef(position).getParent()).getParent()).getKey();
        final String GrandId = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getRef(position)
                .getParent()).getParent()).getParent()).getParent()).getKey();

        //---------------onClick Item Implementation-------------------------
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LastCategory.class);
                intent.putExtra("pId",pId);
                intent.putExtra("ParentKey",ParentId);
                intent.putExtra("GrandKey",GrandId);
                v.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_subcategory, parent,false);
        return new SubCategoryViewHolder(view);
    }

    static class SubCategoryViewHolder extends RecyclerView.ViewHolder{
        ImageView homeImageView;
        TextView homeTitle,homeDesc,homeTags;
        public SubCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            homeImageView = itemView.findViewById(R.id.SubCateIv);
            homeTitle = itemView.findViewById(R.id.SubCateTitleTv);
            homeTitle.setSelected(true);
        }
    }
}
