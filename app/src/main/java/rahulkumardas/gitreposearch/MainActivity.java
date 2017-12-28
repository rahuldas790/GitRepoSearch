package rahulkumardas.gitreposearch;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView searchView;
    private RecyclerView recyclerView;
    private List<Repository> repositoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView = toolbar.findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RepositoryAdapter(this, repositoryList));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchRepositories(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void searchRepositories(final String query) {
        if (!Config.hasInternetConnection(this)) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("No internet connection, app will close now.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            searchRepositories(query);
                        }
                    }).show();
            return;
        }
        RestAdapterAPI api = Config.getRestAdapter();
        Call<JsonObject> result = api.searchRepository(query, 10, 1, "updated", "desc");
        result.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}