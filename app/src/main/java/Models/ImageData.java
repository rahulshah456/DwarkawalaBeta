package Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageData {


    @SerializedName("source_url")
    private String imageLink;

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public String toString() {
        return
                "Links{" +
                        "link = '" + imageLink + '\'' +
                        "}";
    }
}
