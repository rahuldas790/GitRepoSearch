package rahulkumardas.gitreposearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Rahul Kumar Das on 27-12-2017.
 */

class RepositoryAdapter extends RecyclerView.Adapter {
    private List<Repository> repositoryList;
    private Context context;

    public RepositoryAdapter(Context context, List<Repository> repositoryList) {
        this.context = context;
        this.repositoryList = repositoryList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return repositoryList.size();
    }
}
