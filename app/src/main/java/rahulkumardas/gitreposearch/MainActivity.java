package rahulkumardas.gitreposearch;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView searchView;
    private RecyclerView recyclerView;
    private List<Repository> repositoryList = new ArrayList<>();
    private RepositoryAdapter adapter;
    private ProgressBar progressBar;
    private View root;
    private FloatingActionButton floatingActionButton;
    private View filterLayout;
    private int radiusEnd;
    private Animator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_github_white);
        searchView = toolbar.findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progress);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RepositoryAdapter(this, repositoryList);
        recyclerView.setAdapter(adapter);
        searchRepositories("");
        floatingActionButton = findViewById(R.id.filter);
        filterLayout = findViewById(R.id.filterLayout);
        root = findViewById(R.id.root);
        setUpRevealAnimation();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        progressBar.setVisibility(VISIBLE);
        repositoryList.clear();
        adapter.notifyDataSetChanged();
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
        Call<JsonElement> result = api.searchRepository(query, 10, 1, "watchers", "desc");
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                repositoryList.addAll(JsonHandler.handleRepositories(response.body()));
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(GONE);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setUpRevealAnimation() {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    root.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                radiusEnd = (int) Math.hypot(root.getWidth(), root.getHeight());
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    animator = ViewAnimationUtils.createCircularReveal(filterLayout, (int) floatingActionButton.getX() + floatingActionButton.getWidth() / 2,
                            (int) floatingActionButton.getY() + floatingActionButton.getHeight() / 2,
                            0, radiusEnd);
                    animator.start();
                }
                filterLayout.setVisibility(VISIBLE);
                floatingActionButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void submit(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animator = ViewAnimationUtils.createCircularReveal(filterLayout, (int) floatingActionButton.getX() + floatingActionButton.getWidth() / 2,
                    (int) floatingActionButton.getY() + floatingActionButton.getHeight() / 2,
                    radiusEnd, 0);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    filterLayout.setVisibility(GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        } else {
            filterLayout.setVisibility(GONE);
        }
        floatingActionButton.setVisibility(VISIBLE);
    }
}
