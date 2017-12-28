package rahulkumardas.gitreposearch;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rahul Kumar Das on 27-12-2017.
 */

public class User implements Parcelable {

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

    private String name, loginName, avtarUrl, company, location, email;
    int id;

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
    }
}
