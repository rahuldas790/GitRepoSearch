package rahulkumardas.gitreposearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoDetailsActivity extends AppCompatActivity {

    private ImageView image;
    private TextView title, description, watcher, fork, stars, link, language, updated, contributors;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Repository repository;
    private List<User> userList = new ArrayList<>();
    private LayoutInflater inflater;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        image = findViewById(R.id.image);
        title = findViewById(R.id.title);
        description = findViewById(R.id.desc);
        watcher = findViewById(R.id.watchers);
        fork = findViewById(R.id.forks);
        stars = findViewById(R.id.stars);
        link = findViewById(R.id.link);
        language = findViewById(R.id.language);
        updated = findViewById(R.id.updatedOn);
        progressBar = findViewById(R.id.progress);
        contributors = findViewById(R.id.contributors);
        recyclerView = findViewById(R.id.recyclerView);

        Bundle b = getIntent().getExtras();
        repository = b.getParcelable("repo");
        title.setText(repository.getName());
        description.setText(repository.getDescription());
        watcher.setText(repository.getWatchers() + "");
        fork.setText(repository.getForks() + "");
        stars.setText(repository.getStars() + "");
        link.setText("Project link :- " + repository.getHtmlLink());
        link.setMovementMethod(LinkMovementMethod.getInstance());
        setTextWithClick(link);
        language.setText("Language: " + repository.getLanguage());
        String date = parseDate(repository.getUpdatedOn());
        updated.setText("Last updated on " + date);
        Glide.with(this)
                .load(repository.getAvtarUrl())
                .into(image);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter();
        recyclerView.setAdapter(adapter);
        getSupportActionBar().setTitle(repository.getFullName());
        inflater = LayoutInflater.from(this);
        getContributors();
    }

    private class MySpan extends ClickableSpan {

        private String mUrl;

        public MySpan(String url) {

            super();
            mUrl = url;
        }

        @Override
        public void onClick(View widget) {

            isClickingLink = true;
            Intent i = new Intent(RepoDetailsActivity.this, WebViewActivity.class);
            i.putExtra("url", "" + mUrl);
            startActivity(i);
        }

    }

    private boolean isClickingLink = false;

    protected void setTextWithClick(TextView textView) {
        CharSequence charSequence = textView.getText();
        SpannableStringBuilder sp = new SpannableStringBuilder(charSequence);

        URLSpan[] spans = sp.getSpans(0, charSequence.length(), URLSpan.class);

        for (URLSpan urlSpan : spans) {
            MySpan mySpan = new MySpan(urlSpan.getURL());
            sp.setSpan(mySpan, sp.getSpanStart(urlSpan),
                    sp.getSpanEnd(urlSpan), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        textView.setText(sp);

        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 2.if clicking a link
                if (!isClickingLink) {
                    Log.i("log", "not clicking link");
                }
                isClickingLink = false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private void getContributors() {
        RestAdapterAPI api = Config.getRestAdapter();
        Call<JsonArray> result = api.getContributors(repository.getContributorsUrl());
        result.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                userList.clear();
                progressBar.setVisibility(View.GONE);
                userList.addAll(JsonHandler.handleUsers(response.body()));
                adapter.notifyDataSetChanged();
                contributors.setText(userList.size() + " Contributors");
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    private String parseDate(String updatedOn) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat showFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(updatedOn);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return showFormat.format(date);
    }

    class UserAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(inflater.inflate(R.layout.item_user, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyHolder myHolder = (MyHolder) holder;
            myHolder.title.setText(userList.get(position).getLoginName());
            myHolder.contribution.setText("Contributions : " + userList.get(position).getContributions());
            Glide.with(RepoDetailsActivity.this)
                    .load(userList.get(position).getAvtarUrl())
                    .into(myHolder.imageView);
        }

        @Override
        public int getItemCount() {
            return userList.size();
        }

        class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView imageView;
            TextView title, contribution;

            public MyHolder(View itemView) {
                super(itemView);

                imageView = itemView.findViewById(R.id.image);
                title = itemView.findViewById(R.id.name);
                contribution = itemView.findViewById(R.id.contributions);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Intent i = new Intent(RepoDetailsActivity.this, UserDetailsActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("user", userList.get(getAdapterPosition()));
                i.putExtras(b);
                startActivity(i);
            }
        }
    }
}
