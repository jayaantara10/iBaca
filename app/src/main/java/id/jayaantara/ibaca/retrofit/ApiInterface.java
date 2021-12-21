package id.jayaantara.ibaca.retrofit;

import id.jayaantara.ibaca.model.ResponseModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiInterface {
    @GET("paper")
    Call<ResponseModel>getDataPaper();
    @FormUrlEncoded
    @POST("paper")
    Call<ResponseModel>postpaper(@Field("judul") String judul, @Field("jenis") String jenis, @Field("penulis") String penulis, @Field("link") String link, @Field("lisensi") String lisensi, @Field("batasan_umur") String batasan_umur, @Field("deskripsi") String deskripsi, @Field("id_user") String id_user );
    @FormUrlEncoded
    @PUT("paper")
    Call<ResponseModel>puPaper(@Field("id") String id, @Field("judul") String judul, @Field("jenis") String jenis, @Field("penulis") String penulis, @Field("link") String link, @Field("lisensi") String lisensi, @Field("batasan_umur") String batasan_umur, @Field("deskripsi") String deskripsi, @Field("id_user") String id_user );
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "paper", hasBody = true)
    Call<ResponseModel>deletePaper(@Field("id") long id);
}
