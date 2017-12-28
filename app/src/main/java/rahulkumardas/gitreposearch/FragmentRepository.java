package rahulkumardas.gitreposearch;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rahul Kumar Das on 29-12-2017.
 */

public class FragmentRepository extends Fragment {

    private RecyclerView recyclerView;
    private List<Repository> repositoryList = new ArrayList<>();
    private RepositoryAdapter adapter;
    private ProgressBar progressBar;
    private String url;

    public static FragmentRepository newInstance(String url) {
        FragmentRepository fragmentRepository = new FragmentRepository();
        Bundle b = new Bundle();
        b.putString("url", url);
        fragmentRepository.setArguments(b);
        return fragmentRepository;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        url = b.getString("url");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repository, null);

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progress);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new RepositoryAdapter(getActivity(), repositoryList);
        recyclerView.setAdapter(adapter);
        getRepositories();
        return view;

    }

    private void getRepositories() {
        RestAdapterAPI api = Config.getRestAdapter();
        Call<JsonArray> result = api.getReposByUrl(url);
        result.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                repositoryList.addAll(JsonHandler.handleRepositories(response.body()));
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }
}
