package reader.android.com.newsx;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by venkatesh on 24-09-2016.
 */
public class TopNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    ImageLoader imageLoader;
    Context context;
    List<Topnews> topnewsList;

    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public TopNewsAdapter(Context context, List<Topnews> topnewsList, RecyclerView recyclerView) {
       // this.imageLoader = imageLoader;
        super();

        this.context = context;
        this.topnewsList = topnewsList;


        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        return topnewsList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vHolder;

        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_news_list, parent, false);
            vHolder = new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_progress, parent, false);
            vHolder = new ProgressViewHolder(view);
        }
        return vHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            Topnews user = topnewsList.get(position);
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            imageLoader = CustomApiRequest.getInstance(context).getImageLoader();
            imageLoader.get(user.getTopnews_url(), ImageLoader.getImageListener(myViewHolder.userAvatarImg, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
            myViewHolder.userAvatarImg.setImageUrl(user.getTopnews_url(), imageLoader);
            myViewHolder.userNameTV.setText(user.getTopnews_headline());
        } else {
            ProgressViewHolder progressViewHolder = (ProgressViewHolder) holder;
            progressViewHolder.progressBar.setIndeterminate(true);
        }


    }
    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return topnewsList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public NetworkImageView userAvatarImg;
        public TextView userNameTV;

        public MyViewHolder(View itemView) {
            super(itemView);

            userAvatarImg = (NetworkImageView) itemView.findViewById(R.id.imgUser);
            userNameTV = (TextView) itemView.findViewById(R.id.userNameTV);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String navigationLink = "https://github.com/" + userNameTV.getText();
            //Toast.makeText(context,userNameTV.getText()+" click at ",Toast.LENGTH_SHORT).show();
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(navigationLink));
                context.startActivity(intent);
            } catch (Exception e) {
                Log.d("EXC", e.toString());
            }
        }
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBarLoad);
        }
    }

}
