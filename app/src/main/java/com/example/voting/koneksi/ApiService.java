package com.example.voting.koneksi;

import com.example.voting.Model.ModelCalon;
import com.example.voting.Model.ModelJurusan;
import com.example.voting.Model.ModelSiswa;
import com.example.voting.Model.ModelVote;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("siswa")
    Call<List<ModelSiswa>> siswa(@Field("nrp") String nrp);

    @GET("calon")
    Call<List<ModelCalon>> calon();
    @GET("getcalon")
    Call<List<ModelJurusan>> get();

    @FormUrlEncoded
    @POST("vote")
    Call<ResponseBody> vote(
            @Field("siswa_id") String siswa_id,
            @Field("calon_id") String calon_id);

    
    @FormUrlEncoded
    @POST("getvote")
    Call<List<ModelVote>> vote(@Field("siswa_id") String siswa_id);

    @FormUrlEncoded
    @POST("detail")
    Call<List<ModelVote>> getdetail(@Field("idcalon") String idcalon);

    @FormUrlEncoded
    @POST("jurusanpresent")
    Call<List<ModelJurusan>> presentase_jurusan(@Field("no_paslon") String no_paslon);
}

