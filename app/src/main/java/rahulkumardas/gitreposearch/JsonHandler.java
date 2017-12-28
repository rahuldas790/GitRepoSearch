package rahulkumardas.gitreposearch;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul Kumar Das on 28-12-2017.
 * <p>
 * handle the REST responses
 */

public class JsonHandler {

    public static List<Repository> handleRepositories(JsonElement jsonElement) {
        List<Repository> repositoryList = new ArrayList<>();
        JsonArray items = null;
        if (jsonElement == null) {
            return repositoryList;
        }
        if (jsonElement.isJsonArray()) {
            Log.i("GitRepositiryJson", "Its a array");
            items = jsonElement.getAsJsonArray();
        }
        if (jsonElement.isJsonObject()) {
            Log.i("GitRepositiryJson", "Its a onject");
            JsonObject body = jsonElement.getAsJsonObject();
            if (body.has("items")) {
                items = body.get("items").getAsJsonArray();
            } else {
                Log.i("GitRepositiryJson", "Items not found");
                return repositoryList;
            }
        }

        Repository repository;
        for (int i = 0; i < items.size(); i++) {
            JsonObject item = items.get(i).getAsJsonObject();
            repository = new Repository();
            repository.setId(item.get("id").getAsInt());
            repository.setName(item.get("name").getAsString());
            repository.setFullName(item.get("full_name").getAsString());
            repository.setCreatedOn(item.get("created_at").getAsString());
            repository.setUpdatedOn(item.get("updated_at").getAsString());
            if (item.get("description").isJsonNull()) {
                repository.setDescription("No description");
            } else
                repository.setDescription(item.get("description").getAsString());
            repository.setHtmlLink(item.get("html_url").getAsString());
            repository.setContributorsUrl(item.get("contributors_url").getAsString());
            if (item.get("language").isJsonNull())
                repository.setLanguage("N/A");
            else
                repository.setLanguage(item.get("language").getAsString());
            repository.setForks(item.get("forks_count").getAsInt());
            repository.setStars(item.get("stargazers_count").getAsInt());
            repository.setWatchers(item.get("watchers_count").getAsInt());
            JsonObject owner = item.get("owner").getAsJsonObject();
            repository.setOwnerUrl(owner.get("url").getAsString());
            repository.setAvtarUrl(owner.get("avatar_url").getAsString());
            repository.setOwnerRepoUrl(owner.get("repos_url").getAsString());
            repositoryList.add(repository);
        }

        return repositoryList;
    }

    public static List<User> handleUsers(JsonElement jsonElement) {
        List<User> userList = new ArrayList<>();
        if (jsonElement == null || jsonElement.isJsonNull())
            return userList;
        JsonArray jsonArray = null;
        if (jsonElement.isJsonArray())
            jsonArray = jsonElement.getAsJsonArray();
        if (jsonElement.isJsonObject()) {
            jsonArray = new JsonArray();
            jsonArray.add(jsonElement.getAsJsonObject());
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            User user = new User();
            JsonObject item = jsonArray.get(i).getAsJsonObject();
            user.setLoginName(item.get("login").getAsString());
            user.setAvtarUrl(item.get("avatar_url").getAsString());
            user.setRepoUrl(item.get("repos_url").getAsString());
            user.setSelfUrl(item.get("url").getAsString());
            user.setId(item.get("id").getAsInt());
            if (item.has("contributions")) {
                user.setContributions(item.get("contributions").getAsInt());
            }
            if (item.has("public_repos")) {
                user.setRepoCount(item.get("public_repos").getAsInt());
            }
            if (item.has("followers"))
                user.setFollowers(item.get("followers").getAsInt());
            if (item.has("following"))
                user.setFollowing(item.get("following").getAsInt());
            if (item.has("name")) {
                user.setName(item.get("name").getAsString());
                if (!item.get("company").isJsonNull())
                    user.setCompany(item.get("company").getAsString());
                if (!item.get("email").isJsonNull())
                    user.setEmail(item.get("email").getAsString());
                if (!item.get("location").isJsonNull())
                    user.setLocation(item.get("location").getAsString());
                if (!item.get("bio").isJsonNull())
                    user.setBio(item.get("bio").getAsString());
                if (!item.get("blog").isJsonNull())
                    user.setBlog(item.get("blog").getAsString());
                if (!item.get("created_at").isJsonNull())
                    user.setCreatedOn(item.get("created_at").getAsString());
            }
            userList.add(user);
        }
        return userList;
    }
}
