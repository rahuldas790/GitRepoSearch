package rahulkumardas.gitreposearch;

import android.animation.Animator;
import android.app.DatePickerDialog;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
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
    private View filterLayout, card, nothingLayout;
    private int radiusEnd;
    private Animator animator;
    private boolean iShowingFilter = false;

    private Spinner sortBy, orderBy, date, forkCount, starCount, sizeKb;
    private EditText fork, star, size, language, keyword;
    private Switch forked;
    private TextView dateSelector;
    private String dateComparator[] = new String[]{"Before", "After"};
    private String comparator[] = new String[]{">", ">=", "=", "<", "<="};
    private String order[] = new String[]{"Ascending", "Descending"};
    private String sort[] = new String[]{"forks", "stars", "watchers", "updated"};

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
        floatingActionButton = findViewById(R.id.filter);
        filterLayout = findViewById(R.id.filterLayout);
        root = findViewById(R.id.root);
        card = findViewById(R.id.card);
        nothingLayout = findViewById(R.id.nothingLayout);
        setUpRevealAnimation();
        setUpFilters();
        iShowingFilter = false;
//        searchRepositories("");
    }

    private void setUpFilters() {
        keyword = findViewById(R.id.keyword);
        fork = findViewById(R.id.forkCount);
        star = findViewById(R.id.starsCount);
        size = findViewById(R.id.size);
        language = findViewById(R.id.language);
        forked = findViewById(R.id.forkedSwitch);
        dateSelector = findViewById(R.id.date);
        sortBy = findViewById(R.id.sortSpinner);
        orderBy = findViewById(R.id.orderSpinner);
        date = findViewById(R.id.createdSpinner);
        forkCount = findViewById(R.id.forkCountSpinner);
        starCount = findViewById(R.id.starsCountSpinner);
        sizeKb = findViewById(R.id.sizSpinner);

        sortBy.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sort));
        date.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dateComparator));
        orderBy.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, order));
        forkCount.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, comparator));
        starCount.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, comparator));
        sizeKb.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, comparator));

        final Calendar calendar = Calendar.getInstance();
        dateSelector.setText(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
        dateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateSelector.setText(year + "-" + month + "-" + dayOfMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
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
        hideNothing();
        nothingLayout.animate()
                .translationY(dpsToPxs(50))
                .alpha(0)
                .setDuration(300)
                .start();
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
                if (repositoryList.size() == 0) {
                    showNothing();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private int dpsToPxs(int dps) {
        return (int) (dps * getResources().getDisplayMetrics().density);
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
                iShowingFilter = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    animator = ViewAnimationUtils.createCircularReveal(filterLayout, (int) floatingActionButton.getX() + floatingActionButton.getWidth() / 2,
                            (int) floatingActionButton.getY() + floatingActionButton.getHeight() / 2,
                            0, radiusEnd);
                    animator.start();
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            card.animate()
                                    .alpha(1)
                                    .translationY(-15)
                                    .setDuration(600)
                                    .start();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                } else {
                    card.animate()
                            .alpha(1)
                            .translationY(-15)
                            .setDuration(600)
                            .start();
                }
                filterLayout.setVisibility(VISIBLE);
                floatingActionButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void submit(View view) {
        String query = verifyFields();
        Log.i("query formed", query + "");
        if (query == null && view != null)
            return;
        if (view != null) {
            searchAdvance(query);
        }
        iShowingFilter = false;

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
                    card.animate()
                            .translationY(15)
                            .alpha(0)
                            .setDuration(0)
                            .start();
                    filterLayout.setVisibility(GONE);
                    animator.removeAllListeners();
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
            card.animate()
                    .translationY(15)
                    .alpha(0)
                    .setDuration(0)
                    .start();
            filterLayout.setVisibility(GONE);
        }
        floatingActionButton.setVisibility(VISIBLE);


    }

    private void searchAdvance(String path) {
        hideNothing();
        progressBar.setVisibility(VISIBLE);
        repositoryList.clear();
        adapter.notifyDataSetChanged();
        RestAdapterAPI api = Config.getRestAdapter();
        Call<JsonElement> result = api.searchRepositoryByUrl(path);
        result.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                repositoryList.addAll(JsonHandler.handleRepositories(response.body()));
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(GONE);
                if (repositoryList.size() == 0)
                    showNothing();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private String verifyFields() {
        String query = "https://api.github.com/search/repositories?q=";
        if (TextUtils.isEmpty(keyword.getText())) {
            keyword.setError("enter a keyword");
            return null;
        } else {
            query = query + keyword.getText().toString();
        }

        //date selection
        if (date.getSelectedItemPosition() == 0)
            query = query + "+created:<" + dateSelector.getText().toString();
        else
            query = query + "+created:>" + dateSelector.getText().toString();
        //fork inclusion
        if (forked.isActivated())
            query = query + "+fork:true";
        //forks count
        query = query + "+forks:" + comparator[forkCount.getSelectedItemPosition()] + "" + fork.getText().toString();
        //stars count
        query = query + "+stars:" + comparator[starCount.getSelectedItemPosition()] + "" + star.getText().toString();
        query = query + "+size:" + comparator[sizeKb.getSelectedItemPosition()] + "" + size.getText().toString();
        //include language
        if (!TextUtils.isEmpty(language.getText())) {
            query = query + "+language:" + language.getText().toString();
        }

        //sorting
        query = query + "&sort=" + sort[sortBy.getSelectedItemPosition()];
        //ordering
        if (orderBy.getSelectedItemPosition() == 0)
            query = query + "&order=asc";
        else
            query = query + "&order=desc";
        return query + "&per_page=10&page=1";
    }

    @Override
    public void onBackPressed() {
        if (iShowingFilter) {
            submit(null);
        } else {
            super.onBackPressed();
        }
    }

    private void hideNothing() {
        nothingLayout.animate()
                .translationY(dpsToPxs(50))
                .alpha(0)
                .setDuration(300)
                .start();
    }

    private void showNothing() {
        nothingLayout.animate()
                .translationY(dpsToPxs(-50))
                .alpha(1)
                .setDuration(600)
                .start();
    }
}
