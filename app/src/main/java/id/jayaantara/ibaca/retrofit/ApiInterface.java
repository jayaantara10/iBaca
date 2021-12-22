package id.jayaantara.ibaca.retrofit;

import id.jayaantara.ibaca.model.GetResponseModel;
import id.jayaantara.ibaca.model.PostResponseModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiInterface {
    @GET("paper")
    Call<GetResponseModel>getDataPaper();
    @FormUrlEncoded
    @POST("paper/store")
    Call<PostResponseModel>createDataPaper(@Field("judul") String judul, @Field("jenis") String jenis, @Field("penulis") String penulis, @Field("link") String link, @Field("lisensi") String lisensi, @Field("batasan_umur") String batasan_umur, @Field("deskripsi") String deskripsi, @Field("id_user") long id_user );
    @FormUrlEncoded
    @PUT("paper")
    Call<PostResponseModel>putPaper(@Field("id") long id, @Field("judul") String judul, @Field("jenis") String jenis, @Field("penulis") String penulis, @Field("link") String link, @Field("lisensi") String lisensi, @Field("batasan_umur") String batasan_umur, @Field("deskripsi") String deskripsi, @Field("id_user") long id_user );
    @FormUrlEncoded
    @POST("paper/delete")
    Call<PostResponseModel>deleteDataPaper(@Field("id") long id);
}
