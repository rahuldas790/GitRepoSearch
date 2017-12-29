package rahulkumardas.gitreposearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rahul Kumar Das on 28-12-2017.
 */

public class UserDetailsFragment extends Fragment {

    private TextView name, login, repoCount, bio, following, follower, company, location, email, blog, createdOn;
    private ImageView imageView;
    private String url;
    private View progress;

    public static UserDetailsFragment newInstance(String url) {
        UserDetailsFragment userDetailsFragment = new UserDetailsFragment();
        Bundle b = new Bundle();
        b.putString("url", url);
        userDetailsFragment.setArguments(b);
        return userDetailsFragment;
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
        View view = inflater.inflate(R.layout.fragment_user_details, null);
        name = view.findViewById(R.id.name);
        login = view.findViewById(R.id.login);
        repoCount = view.findViewById(R.id.repoCount);
        bio = view.findViewById(R.id.bio);
        following = view.findViewById(R.id.following);
        follower = view.findViewById(R.id.followers);
        company = view.findViewById(R.id.company);
        location = view.findViewById(R.id.location);
        email = view.findViewById(R.id.email);
        blog = view.findViewById(R.id.blog);
        createdOn = view.findViewById(R.id.createdOn);
        imageView = view.findViewById(R.id.image);
        progress = view.findViewById(R.id.progress);
        getUserDetails();
        return view;
    }

    private void getUserDetails() {
        RestAdapterAPI api = Config.getRestAdapter();
        Call<JsonObject> result = api.getUserByUrl(url);
        result.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    List<User> userList = JsonHandler.handleUsers(response.body());
                    User user = userList.get(0);
                    progress.setVisibility(View.GONE);
                    name.setText(user.getName());
                    login.setText(user.getLoginName());
                    repoCount.setText("Total repositories : " + user.getRepoCount());
                    bio.setText(user.getBio());
                    following.setText("Following (" + user.getFollowing() + ")");
                    follower.setText("Followers (" + user.getFollowers() + ")");

                    if (!TextUtils.isEmpty(user.getCompany())) {
                        company.setText(user.getCompany());
                    } else {
                        company.setHint("N/A");
                    }
                    if (!TextUtils.isEmpty(user.getLocation())) {
                        location.setText(user.getLocation());
                    } else {
                        location.setHint("N/A");
                    }
                    if (!TextUtils.isEmpty(user.getEmail())) {
                        email.setText(user.getEmail());
                    } else {
                        email.setHint("N/A");
                    }
                    if (!TextUtils.isEmpty(user.getBlog())) {
                        blog.setText(user.getBlog());
                    } else {
                        blog.setHint("N/A");
                    }
                    createdOn.setText("Created on: " + parseDate(user.getCreatedOn()));
                    Glide.with(getActivity())
                            .load(user.getAvtarUrl())
                            .into(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private String parseDate(String createdOn) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat showFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(createdOn);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return showFormat.format(date);
    }
}
