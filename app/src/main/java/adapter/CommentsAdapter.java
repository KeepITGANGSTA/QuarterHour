package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import bwie.com.quarterhour.R;
import entity.VideoDetails;

/**
 * Created by 李英杰 on 2017/12/18.
 * Description：
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>{

    private Context context;
    private List<VideoDetails.CommentsBean> comments;
    private LayoutInflater inflater;

    public CommentsAdapter(Context context, List<VideoDetails.CommentsBean> comments) {
        this.context = context;
        this.comments = comments;
        inflater=LayoutInflater.from(context);
    }

    public void refreshComments(List<VideoDetails.CommentsBean> comments){
        this.comments.clear();
        this.comments=comments;
        notifyDataSetChanged();
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.comments_layout,null);
        CommentsViewHolder holder=new CommentsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, int position) {
        VideoDetails.CommentsBean commentsBean = comments.get(position);
        holder.tv_author.setText(commentsBean.nickname+"：");
        holder.tv_content.setText(commentsBean.content);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentsViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_author,tv_content;
        public CommentsViewHolder(View itemView) {
            super(itemView);
            tv_author=itemView.findViewById(R.id.comment_author_tv);
            tv_content=itemView.findViewById(R.id.comment_content_tv);
        }
    }

    public void destroy(){
        context=null;
        inflater=null;
    }

}
