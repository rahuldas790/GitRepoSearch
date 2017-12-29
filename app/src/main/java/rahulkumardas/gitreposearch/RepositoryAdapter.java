package rahulkumardas.gitreposearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rahul Kumar Das on 27-12-2017.
 */

class RepositoryAdapter extends RecyclerView.Adapter {
    private List<Repository> repositoryList;
    private Context context;
    private LayoutInflater inflater;

    public RepositoryAdapter(Context context, List<Repository> repositoryList) {
        this.context = context;
        this.repositoryList = repositoryList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(inflater.inflate(R.layout.item_repository, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        Repository repository = repositoryList.get(position);
        myHolder.title.setText(repository.getFullName());
        myHolder.description.setText(repository.getDescription());
        myHolder.updated.setText("Last update: " + parseDate(repository.getUpdatedOn()));
        myHolder.language.setText(repository.getLanguage());
        myHolder.fork.setText(String.valueOf(repository.getForks()));
        myHolder.starts.setText(String.valueOf(repository.getStars()));
        myHolder.watcher.setText(String.valueOf(repository.getWatchers()));
        Glide.with(context)
                .load(repository.getAvtarUrl())
//                .placeholder(R.drawable.loading_spinner)
                .into(myHolder.imageView);

    }

    private String parseDate(String updatedOn) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat showFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(updatedOn);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return showFormat.format(date);
    }

    @Override
    public int getItemCount() {
        return repositoryList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View root;
        TextView title, description, updated, language, fork, starts, watcher;
        ImageView imageView;

        public MyHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.desc);
            updated = itemView.findViewById(R.id.updatedOn);
            language = itemView.findViewById(R.id.language);
            fork = itemView.findViewById(R.id.forks);
            starts = itemView.findViewById(R.id.stars);
            watcher = itemView.findViewById(R.id.watchers);
            imageView = itemView.findViewById(R.id.image);

            root.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent i = new Intent(context, RepoDetailsActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle b = new Bundle();
            b.putParcelable("repo", repositoryList.get(position));
            i.putExtras(b);
            context.startActivity(i);
        }
    }
}
