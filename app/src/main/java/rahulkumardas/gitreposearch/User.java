package rahulkumardas.gitreposearch;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rahul Kumar Das on 27-12-2017.
 * User model
 */

public class User implements Parcelable {

    private String name;
    private String loginName;
    private String avtarUrl;
    private String company;
    private String location;
    private String email;
    private String bio;
    private String blog;
    private String selfUrl;
    private String createdOn;
    private String repoUrl;
    private int following, followers;
    int id;

    public int getRepoCount() {
        return repoCount;
    }

    public void setRepoCount(int repoCount) {
        this.repoCount = repoCount;
    }

    public int getContributions() {
        return contributions;
    }

    public void setContributions(int contributions) {
        this.contributions = contributions;
    }

    int repoCount;
    int contributions;

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public User() {
        //required for instantiation
    }

    protected User(Parcel in) {
        name = in.readString();
        loginName = in.readString();
        avtarUrl = in.readString();
        company = in.readString();
        location = in.readString();
        email = in.readString();
        id = in.readInt();
        followers = in.readInt();
        following = in.readInt();
        contributions = in.readInt();
        repoCount = in.readInt();
        bio = in.readString();
        blog = in.readString();
        createdOn = in.readString();
        repoUrl = in.readString();
        selfUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getAvtarUrl() {
        return avtarUrl;
    }

    public void setAvtarUrl(String avtarUrl) {
        this.avtarUrl = avtarUrl;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(loginName);
        dest.writeString(avtarUrl);
        dest.writeString(company);
        dest.writeString(location);
        dest.writeString(email);
        dest.writeInt(id);
        dest.writeInt(repoCount);
        dest.writeInt(contributions);
        dest.writeInt(followers);
        dest.writeInt(following);
        dest.writeString(bio);
        dest.writeString(blog);
        dest.writeString(createdOn);
        dest.writeString(repoUrl);
        dest.writeString(selfUrl);
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getSelfUrl() {
        return selfUrl;
    }

    public void setSelfUrl(String selfUrl) {
        this.selfUrl = selfUrl;
    }
}
