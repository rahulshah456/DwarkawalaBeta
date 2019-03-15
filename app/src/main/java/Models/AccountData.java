package Models;

public class AccountData {

    public String name;
    public String email;
    public String phoneNumber;
    public String profilePic;
    public String latitude;
    public String longitude;
    public String password;


    public AccountData() {
    }


    public AccountData(String name, String email, String phoneNumber, String profilePic, String latitude, String longitude) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePic = profilePic;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public AccountData(String name, String email, String phoneNumber, String profilePic, String latitude, String longitude, String password) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePic = profilePic;
        this.latitude = latitude;
        this.longitude = longitude;
        this.password = password;
    }

    public AccountData(String name, String email, String phoneNumber, String profilePic) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePic = profilePic;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
