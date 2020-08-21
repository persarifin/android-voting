package com.example.voting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voting.Model.ModelCalon;
import com.example.voting.Model.ModelJurusan;
import com.example.voting.koneksi.ApiService;
import com.example.voting.koneksi.RetrofitClient;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class Main_calon extends AppCompatActivity {
    private static final String TAG = "Main_calon";
    ApiService apiService;
    Call<List<ModelCalon>> call;
    Call<List<ModelJurusan>> call1;
    Call<ResponseBody> callvote;
    ProgressBar progressBar;
    ProgressBar progresspilih;
    private ListView listview;
    private List_View adapter;
    private Dialog customDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calon);
        getSupportActionBar().hide();
        try {
            listview = (ListView) findViewById(R.id.listView);

            progressBar = (ProgressBar) findViewById(R.id.progresbar);
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.GONE);
            apiService = RetrofitClient.getClient().create(ApiService.class);
            tampildata();


            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        TextView detail = (TextView) view.findViewById(R.id.btndetail);
                        TextView textidcalon=  (TextView) view.findViewById(R.id.logo);
                        TextView pilih = (TextView)view.findViewById(R.id.btnpilih);
                        detail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String idcalon = textidcalon.getText().toString();
                                String idmhs = getIntent().getStringExtra("hasil");
                                textidcalon.setVisibility(View.GONE);
                                Intent intent = new Intent(Main_calon.this,Detail.class);
                                intent.putExtra("idcalon", idcalon);
                                intent.putExtra("idmhs", idmhs);
                                startActivity(intent);
                            }
                        });
                        pilih.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                customDialog = new Dialog(Main_calon.this);
                                customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                customDialog.setCancelable(false);
                                customDialog.setContentView(R.layout.alert_dialog);
                                TextView idmhs= customDialog.findViewById(R.id.txtidmhs);
                                TextView id_calon= customDialog.findViewById(R.id.txtidcalon);
                                TextView txtisi= customDialog.findViewById(R.id.textisi);
                                Button submit = customDialog.findViewById(R.id.submit);
                                Button cancel = customDialog.findViewById(R.id.cancel);
                                id_calon.setVisibility(View.GONE);
                                //mulai deklarasi
                                idmhs.setVisibility(View.GONE);
                                txtisi.setText("Yakin untuk memilih ?");
                                id_calon.setText(textidcalon.getText().toString());
                                idmhs.setText(getIntent().getStringExtra("hasil"));
                                submit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        callvote = apiService.vote(idmhs.getText().toString(),id_calon.getText().toString() );
                                        callvote.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.isSuccessful()) {
                                                    progressBar.setIndeterminate(true);
                                                    progressBar.setVisibility(View.VISIBLE);
//                                                Log.w(TAG, "" + response.body());
                                                    Intent intent = new Intent(Main_calon.this, Main_end.class);
                                                    String nrpmhs = idmhs.getText().toString();
                                                    intent.putExtra("hasil",nrpmhs);
                                                    startActivity(intent);
                                                    customDialog.dismiss();
                                                    finish();
                                                }
                                                else {
                                                    customDialog.dismiss();
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(Main_calon.this);
                                                    builder.setTitle("Error")
                                                            .setIcon(R.drawable.ic_info_black_24dp)
                                                            .setCancelable(false)
                                                            .setMessage("Anda sudah memilih di sesi sebelumnya!")
                                                            .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    finish();
                                                                }
                                                            });
                                                    builder.show();
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                Toast.makeText(Main_calon.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });

                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        customDialog.dismiss();
                                    }
                                });

                                customDialog.show();

                            }
                        });
                    }catch (Exception ex){
                        Toast.makeText(Main_calon.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }catch (Exception ex){
            Toast.makeText(Main_calon.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void isidata (List < ModelCalon > modelCalon) {
        adapter = new List_View(Main_calon.this, modelCalon);
        listview.setAdapter(adapter);
    }
    private void tampildata() {
        ProgressBar statistik = (ProgressBar) findViewById(R.id.statistik);
        PieChart persen_jurusan = (PieChart) findViewById(R.id.persentase);
        TextView pemilih =  (TextView) findViewById(R.id.pemilih);
        TextView persen =  (TextView) findViewById(R.id.persent);
        call1 = apiService.get();
        call1.enqueue(new Callback<List<ModelJurusan>>() {
            @Override
            public void onResponse(Call<List<ModelJurusan>> call, Response<List<ModelJurusan>> response) {
                if (response.isSuccessful()) {

                    List<PieEntry> yentry = new ArrayList<>();
                    for (int x = 0; x < response.body().size(); x++) {
//                        Log.w(TAG, response.body() + "");
                        yentry.add(new PieEntry(Float.parseFloat(response.body().get(x).getPersentase() == null ? "0.0" : response.body().get(x).getPersentase()), response.body().get(x).getJurusan()));
                    }
                    PieDataSet pieDataSet = new PieDataSet(yentry,"");
                    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                    persen_jurusan.setRotationEnabled(true);
                    persen_jurusan.setHoleRadius(25f);
                    persen_jurusan.setTransparentCircleAlpha(0);
                    persen_jurusan.setCenterText("Jurusan");
                    persen_jurusan.setCenterTextSize(10);
                    pieDataSet.setSliceSpace(1);
                    pieDataSet.setValueTextSize(12);
                    PieData pieData = new PieData(pieDataSet);
                    persen_jurusan.setData(pieData);
                    persen_jurusan.animateY(1000);
                    persen_jurusan.invalidate();
                }
            }
            @Override
            public void onFailure(Call<List<ModelJurusan>> call, Throwable t) {

            }
        });
        call = apiService.calon();
        call.enqueue(new Callback<List<ModelCalon>>() {
            @Override
            public void onResponse(Call<List<ModelCalon>> call, Response<List<ModelCalon>> response) {
                if (response.isSuccessful()){
                    pemilih.setText("pemilih : "+response.body().get(0).getPemilih());
                    statistik.setMax(100);
                    persen.setText(response.body().get(0).getPersentase().substring(0,4)+ "%");
                    statistik.setProgress((int) Double.parseDouble(response.body().get(0).getPersentase()));
                    progressBar.setVisibility(View.GONE);
                    isidata(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<ModelCalon>> call, Throwable t) {

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
