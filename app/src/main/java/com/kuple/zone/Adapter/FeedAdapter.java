package com.kuple.zone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.kuple.zone.R;
import com.kuple.zone.model.FeedModel;

import java.util.ArrayList;


public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>{

    Context context;
    ArrayList<FeedModel> FeedArrayList = new ArrayList<>();
    RequestManager glide;

    public FeedAdapter(Context context, ArrayList<FeedModel> modelFeedArrayList) {

        this.context = context;
        this.FeedArrayList = modelFeedArrayList;
        glide = Glide.with(context);

    }

    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_feed, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FeedModel modelFeed = FeedArrayList.get(position);

        holder.tv_name.setName(modelFeed.getName());
        holder.tv_time.setText(modelFeed.getTime());
        holder.tv_likes.setText(String.valueOf(modelFeed.getLikes()));
        holder.tv_comments.setText(modelFeed.getComments() + " comments");
        holder.tv_post.setText(modelFeed.getStatus());

        glide.load(modelFeed.getPropic()).into(holder.img_profile);

        if (modelFeed.getPostpic() == 0) {
            holder.img_post.setVisibility(View.GONE);
        } else {
            holder.img_post.setVisibility(View.VISIBLE);
            glide.load(modelFeed.getPostpic()).into(holder.img_post);
        }
    }

    @Override
    public int getItemCount() {
        return FeedArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_time, tv_likes, tv_comments, tv_post;
        ImageView img_profile, img_post;

        public ViewHolder(View itemView) {
            super(itemView);

            img_profile = (ImageView) itemView.findViewById(R.id.img_profile);
            img_post = (ImageView) itemView.findViewById(R.id.img_post);

            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_likes = (TextView) itemView.findViewById(R.id.tv_like);
            tv_comments = (TextView) itemView.findViewById(R.id.tv_comment);
            tv_post = (TextView) itemView.findViewById(R.id.tv_post);
        }
    }
}
