package com.example.voting;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voting.Model.ModelJurusan;
import com.example.voting.Model.ModelVote;
import com.example.voting.koneksi.ApiService;
import com.example.voting.koneksi.RetrofitClient;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class StatistikFragment extends Fragment {
    ApiService apiService;
    Call<List<ModelJurusan>> call;
    Call<List<ModelVote>> call1;
    Call<ResponseBody> callvote;
    PieChart pieChart;
    private Dialog customDialog;


    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistik, container, false);
        TextView paslon = (TextView) view.findViewById(R.id.paslon);
        TextView person = (TextView)view.findViewById(R.id.persent);
        ProgressBar present = (ProgressBar) view.findViewById(R.id.statistik);
        TextView orang = (TextView)view.findViewById(R.id.orang);
        FloatingActionButton floatingActionButton= (FloatingActionButton) view.findViewById(R.id.pilihf );
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog = new Dialog(getContext());
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
                id_calon.setText(getActivity().getIntent().getStringExtra("idcalon"));
                idmhs.setText(getActivity().getIntent().getStringExtra("idmhs"));
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callvote = apiService.vote(idmhs.getText().toString(),id_calon.getText().toString() );
                        callvote.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
//                                                Log.w(TAG, "" + response.body());
                                    Intent intent = new Intent(getContext(), Main_end.class);
                                    String nrpmhs = idmhs.getText().toString();
                                    intent.putExtra("hasil",nrpmhs);
                                    startActivity(intent);
                                    customDialog.dismiss();
                                }
                                else {
                                    customDialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("Error")
                                            .setIcon(R.drawable.ic_info_black_24dp)
                                            .setCancelable(false)
                                            .setMessage("Anda sudah memilih di sesi sebelumnya!")
                                            .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(getContext(),MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                }
                                            });
                                    builder.show();
                                }
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
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

        apiService = RetrofitClient.getClient().create(ApiService.class);
        call1=apiService.getdetail(getActivity().getIntent().getStringExtra("idcalon"));
        call1.enqueue(new Callback<List<ModelVote>>() {
            @Override
            public void onResponse(Call<List<ModelVote>> call, Response<List<ModelVote>> response) {
                if (response.isSuccessful()){
                    person.setText(response.body().get(0).getPersentase().substring(0,4)+ " %");
                    orang.setText("Pemilih : " + response.body().get(0).getPerson());
                    paslon.setText("Pasangan No "+response.body().get(0).getPaslon_nomor());
                    present.setMax(100);
                    present.setProgress((int) Double.parseDouble(response.body().get(0).getPersentase()));
                }
            }

            @Override
            public void onFailure(Call<List<ModelVote>> call, Throwable t) {

            }
        });

        pieChart= (PieChart) view.findViewById(R.id.chart);

        call= apiService.presentase_jurusan(getActivity().getIntent().getStringExtra("idcalon"));
        call.enqueue(new Callback<List<ModelJurusan>>() {
            @Override
            public void onResponse(Call<List<ModelJurusan>> call, Response<List<ModelJurusan>> response) {
                if (response.isSuccessful()){
                    Log.w(TAG, "onResponse: "+response.body());
                    try {
                        pieChart = view.findViewById(R.id.chart);
                        List<PieEntry> yentry = new ArrayList<>();
                        for (int x =0; x<response.body().size(); x++){
                            Log.w(TAG, response.body() + "");
                            yentry.add(new PieEntry(Float.parseFloat(response.body().get(x).getPersentase() == null ? "0.0" : response.body().get(x).getPersentase()),response.body().get(x).getJurusan()));
                        }


                        PieDataSet pieDataSet = new PieDataSet(yentry,"");
                        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                        pieChart.setRotationEnabled(true);
                        pieChart.setHoleRadius(25f);
                        pieChart.setTransparentCircleAlpha(0);
                        pieChart.setCenterText("Jurusan");
                        pieChart.setCenterTextSize(10);
                        pieDataSet.setSliceSpace(1);
                        pieDataSet.setValueTextSize(12);
                        PieData pieData = new PieData(pieDataSet);
                        pieChart.setData(pieData);
                        pieChart.animateY(1000);
                        pieChart.invalidate();
                    }catch (Exception ex){

                        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<List<ModelJurusan>> call, Throwable t) {
                Log.w(TAG, t.getMessage());
            }
        });
        return view;
    }
}