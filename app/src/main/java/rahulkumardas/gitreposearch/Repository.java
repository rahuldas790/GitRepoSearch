package rahulkumardas.gitreposearch;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rahul Kumar Das on 27-12-2017.
 */

public class Repository implements Parcelable {

    public Repository() {
        //required empty constructor for instantiation
    }

    protected Repository(Parcel in) {
        name = in.readString();
        fullName = in.readString();
        description = in.readString();
        language = in.readString();
        contributorsUrl = in.readString();
        avtarUrl = in.readString();
        htmlLink = in.readString();
        id = in.readInt();
        forks = in.readInt();
        watchers = in.readInt();
        stars = in.readInt();
    }

    public static final Creator<Repository> CREATOR = new Creator<Repository>() {
        @Override
        public Repository createFromParcel(Parcel in) {
            return new Repository(in);
        }

        @Override
        public Repository[] newArray(int size) {
            return new Repository[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getContributorsUrl() {
        return contributorsUrl;
    }

    public void setContributorsUrl(String contributorsUrl) {
        this.contributorsUrl = contributorsUrl;
    }

    public String getAvtarUrl() {
        return avtarUrl;
    }

    public void setAvtarUrl(String avtarUrl) {
        this.avtarUrl = avtarUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public int getWatchers() {
        return watchers;
    }

    public void setWatchers(int watchers) {
        this.watchers = watchers;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    private String name;
    private String fullName;
    private String description;
    private String language;
    private String contributorsUrl;
    private String avtarUrl;

    public String getHtmlLink() {
        return htmlLink;
    }

    public void setHtmlLink(String htmlLink) {
        this.htmlLink = htmlLink;
    }

    private String htmlLink;
    private int id, forks, watchers, stars;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(fullName);
        dest.writeString(description);
        dest.writeString(language);
        dest.writeString(contributorsUrl);
        dest.writeString(avtarUrl);
        dest.writeString(htmlLink);
        dest.writeInt(id);
        dest.writeInt(forks);
        dest.writeInt(watchers);
        dest.writeInt(stars);
    }
}
