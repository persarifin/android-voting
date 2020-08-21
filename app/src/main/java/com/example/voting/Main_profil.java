package com.example.voting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.example.voting.Model.ModelSiswa;
import com.example.voting.koneksi.ApiService;
import com.example.voting.koneksi.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main_profil extends AppCompatActivity {

    private static final String TAG = "Main_profil";
    ApiService apiService;
    Call<List<ModelSiswa>> call;
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profil);
        getSupportActionBar().hide();
        TextView nissiswa= (TextView)findViewById(R.id.txtnrp);
        TextView namasiswa = (TextView) findViewById(R.id.txtmahasiswa);
        TextView jurusan = (TextView) findViewById(R.id.txtjurusan);
        TextView jk = (TextView) findViewById(R.id.txtjk);
        TextView tgl_lahir = (TextView) findViewById(R.id.txtttl);
        ImageView img = (ImageView) findViewById(R.id.imgsiswa);
        ImageView imgback = (ImageView) findViewById(R.id.imgbesar);
        Button btn = (Button) findViewById(R.id.btnlanjut);
        ProgressBar progressBar= findViewById(R.id.progres);



        String nrp =getIntent().getStringExtra("hasil");
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);


        apiService = RetrofitClient.getClient().create(ApiService.class);
        call = apiService.siswa(nrp);
        call.enqueue(new Callback<List<ModelSiswa>>() {
            @Override
            public void onResponse(Call<List<ModelSiswa>> call, Response<List<ModelSiswa>> response) {
//                Log.w(TAG,""+ response.body());
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    //
                    nissiswa.setText(response.body().get(0).getNrp());
                    namasiswa.setText(response.body().get(0).getNama());
                    jurusan.setText(response.body().get(0).getKelas() + " / " + response.body().get(0).getCabang() + " " + response.body().get(0).getJurusan());
                    jk.setText(response.body().get(0).getJk());
                    tgl_lahir.setText(response.body().get(0).getTgl_lahir().toString());

                    if (response.body().get(0).getFoto() != null && response.body().get(0).getFoto().length() > 0) {
                        Picasso.get().load("http://192.168.43.50/laravel/public/foto/" + response.body().get(0).getFoto()).into(img);
                    } else
                        Picasso.get().load(R.drawable.ic_account_box_black_24dp).into(img);

                    if (response.body().get(0).getFoto() != null && response.body().get(0).getFoto().length() > 0) {
                        Picasso.get().load("http://192.168.43.50/laravel/public/foto/" + response.body().get(0).getFoto()).into(imgback);
                    } else
                        Picasso.get().load(R.drawable.ic_account_box_black_24dp).into(imgback);
                    }
//                Log.w(TAG,""+ response.body());
                else{
                    if(response.code()==401){
                        progressBar.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Main_profil.this);
                        builder.setTitle("Error 404")
                                .setIcon(R.drawable.ic_info_black_24dp)
                                .setCancelable(false)
                                .setMessage("Anda tidak Terdaftar Silahkan Hubungi Admin !!")
                                .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });
                        builder.show();
                    }
                    else{
                        progressBar.setVisibility(View.GONE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main_profil.this);
                    builder.setTitle("Error")
                            .setIcon(R.drawable.ic_info_black_24dp)
                            .setCancelable(false)
                            .setMessage("Voting belum di buka, tunggu beberapa saat!")
                            .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });
                    builder.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ModelSiswa>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Main_profil.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Main_profil.this, Main_calon.class);
                i.putExtra("hasil",nrp);
                startActivity(i);
                finish();
            }
        });

    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
