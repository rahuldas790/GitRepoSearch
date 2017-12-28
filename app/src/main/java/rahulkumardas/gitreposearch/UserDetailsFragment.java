package rahulkumardas.gitreposearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

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
                    company.setText(user.getCompany());
                    location.setText(user.getLocation());
                    email.setText(user.getEmail());
                    blog.setText(user.getBlog());
                    createdOn.setText(user.getCreatedOn());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
