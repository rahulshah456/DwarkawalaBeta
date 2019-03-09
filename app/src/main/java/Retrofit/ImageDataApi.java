package Retrofit;

import Models.ImageData;
import Models.Thumbnail;
import Models.WpAttachmentItem;
import Models.WpFeaturedmediaItem;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ImageDataApi {

    String BASE_URL = "https://dwarkawala.com/index.php/wp-json//wp//v2//";

    @GET("media/{Id}")
    Call<Thumbnail> getImageData(
            @Path("Id") int postId
    );



    class ImageFactory {
        private static ImageDataApi service;
        public static ImageDataApi getInstance() {

            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build();
                service = retrofit.create(ImageDataApi.class);
                return service;
            } else {
                return service;
            }
        }
    }

}
