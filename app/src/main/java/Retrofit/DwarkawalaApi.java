package Retrofit;

import java.util.List;

import Models.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DwarkawalaApi {

    String BASE_URL = "https://dwarkawala.com/wp-json/wp/v2/";


    @GET("posts/")
    Call<List<Response>> getPosts(@Query("type") String type,
                                       @Query("page") int page);




    class Factory {
        private static DwarkawalaApi service;
        public static DwarkawalaApi getInstance() {
            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build();
                service = retrofit.create(DwarkawalaApi.class);
                return service;
            } else {
                return service;
            }
        }
    }

}
