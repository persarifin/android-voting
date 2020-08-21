package com.example.voting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.voting.Model.ModelVote;
import com.example.voting.koneksi.ApiService;
import com.example.voting.koneksi.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main_end extends AppCompatActivity {
    private static final String TAG = "Main_end";
    ApiService apiService;
    Call<List<ModelVote>> callvote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_end);
        getSupportActionBar().setTitle("SELESAI");
        Button btn = (Button)findViewById(R.id.btnend);
        TextView idsiswa2 = (TextView) findViewById(R.id.idmhs);
        TextView textnama= (TextView) findViewById(R.id.namafb);
        ImageView imagepaslon = (ImageView)  findViewById(R.id.imgpaslonend);
        TextView textidcalon = (TextView) findViewById(R.id.logoend);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progres);
        idsiswa2.setVisibility(View.GONE);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        String nrpmhs = getIntent().getStringExtra("hasil");
        idsiswa2.setText(nrpmhs);
        callvote = apiService.vote(nrpmhs);
        callvote.enqueue(new Callback<List<ModelVote>>() {
            @Override
            public void onResponse(Call<List<ModelVote>> call, Response<List<ModelVote>> response) {
//                Log.w(TAG, "" + response.body());
                progressBar.setVisibility(View.GONE);
                textidcalon.setText("Pasangan No "+response.body().get(0).getPaslon_nomor());
                textnama.setText(response.body().get(0).getNama());
                if (response.body().get(0).getFoto_paslon()!=null && response.body().get(0).getFoto_paslon().length()>0){
                    Picasso.get().load("http://192.168.43.50/laravel/public/foto_paslon/"+ response.body().get(0).getFoto_paslon()).into(imagepaslon);
                }
                else
                    Picasso.get().load(R.drawable.ic_account_box_black_24dp).into(imagepaslon);

            }

            @Override
            public void onFailure(Call<List<ModelVote>> call, Throwable t) {

            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new  Intent(getApplicationContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
