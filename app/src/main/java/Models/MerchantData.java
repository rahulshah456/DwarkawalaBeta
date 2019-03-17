package Models;

public class MerchantData {

    public String merchantName;
    public String email;
    public String phoneNumber;
    public String shopPic;
    public String latitude;
    public String longitude;
    public String shopAddress;
    public String shopName;


    public MerchantData() {
    }

    public MerchantData(String merchantName, String email, String phoneNumber, String shopPic, String latitude, String longitude, String shopAddress, String shopName) {
        this.merchantName = merchantName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.shopPic = shopPic;
        this.latitude = latitude;
        this.longitude = longitude;
        this.shopAddress = shopAddress;
        this.shopName = shopName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
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

    public String getShopPic() {
        return shopPic;
    }

    public void setShopPic(String shopPic) {
        this.shopPic = shopPic;
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

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
