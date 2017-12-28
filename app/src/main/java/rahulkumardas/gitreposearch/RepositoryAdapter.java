package rahulkumardas.gitreposearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

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
        myHolder.updated.setText(repository.getUpdatedOn());
        myHolder.language.setText(repository.getLanguage());
        myHolder.fork.setText(String.valueOf(repository.getForks()));
        myHolder.starts.setText(String.valueOf(repository.getStars()));
        myHolder.watcher.setText(String.valueOf(repository.getWatchers()));
    }

    @Override
    public int getItemCount() {
        return repositoryList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        View root;
        TextView title, description, updated, language, fork, starts, watcher;

        public MyHolder(View itemView) {
            super(itemView);
            root = itemView;

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.desc);
            updated = itemView.findViewById(R.id.updatedOn);
            language = itemView.findViewById(R.id.language);
            fork = itemView.findViewById(R.id.forks);
            starts = itemView.findViewById(R.id.stars);
            watcher = itemView.findViewById(R.id.watchers);
        }
    }
}
