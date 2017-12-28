package rahulkumardas.gitreposearch;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Rahul Kumar Das on 27-12-2017.
 */

public interface RestAdapterAPI {
    public static final String END_POINT = "https://api.github.com";

    /**
     * search repository with query
     *
     * @param query   to find repository by query
     *                q=query
     *                <p>
     *                to find repositories of a particular user
     *                q=user:login_name
     * @param sort
     * @param order   for sorting filters
     *                sort=[stars, forks, updated]
     *                order=[asc, desc]
     * @param perPage
     * @param pageNo  pagination
     *                per_page=100
     *                page=50
     */
    @GET("/search/repositories")
    Call<JsonElement> searchRepository(@Query("q") String query, @Query("per_page") int perPage, @Query("page")
            int pageNo, @Query("sort") String sort, @Query("order") String order);

    //get user by login name
    @GET("/users/{login}")
    Call<JsonObject> getUser(@Path("login") String username);

    //find all repository of a user by login name
    @GET("/users/{login}/gists")
    Call<JsonObject> getUserRepos(@Path("login") String username);

    //get repository by repo name and login
    @GET("/repos/{username}/{reponame}")
    Call<JsonObject> getRepoDetails(@Path("username") String username, @Path("reponame") String repoName);

    //get contributors by dynamic url
    @GET
    Call<JsonArray> getContributors(@Url String url);

    //get user by dynamic url
    @GET
    Call<JsonObject> getUserByUrl(@Url String url);
}
