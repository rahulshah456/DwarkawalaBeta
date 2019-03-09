package Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeatureMedia {

    @SerializedName("href")
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }



}
