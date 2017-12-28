package rahulkumardas.gitreposearch;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul Kumar Das on 28-12-2017.
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

    public static List<User> handleUsers(JsonArray jsonArray) {
        List<User> userList = new ArrayList<>();
        if (jsonArray == null || jsonArray.size() == 0)
            return userList;
        for (int i = 0; i < jsonArray.size(); i++) {
            User user = new User();
            JsonObject item = jsonArray.get(i).getAsJsonObject();
            user.setLoginName(item.get("login").getAsString());
            user.setAvtarUrl(item.get("avatar_url").getAsString());
            user.setId(item.get("id").getAsInt());
            userList.add(user);
        }
        return userList;
    }
}
