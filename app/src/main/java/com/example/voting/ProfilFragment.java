package com.example.voting;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voting.Model.ModelJurusan;
import com.example.voting.Model.ModelVote;
import com.example.voting.koneksi.ApiService;
import com.example.voting.koneksi.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilFragment extends Fragment {
    ApiService apiService;
    Call<List<ModelVote>> call;
    Call<ResponseBody> callvote;
    private  Dialog customDialog;

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        ImageView fotoketua = (ImageView) view.findViewById(R.id.img3);
        TextView namaketua = (TextView) view.findViewById(R.id.textnamaketua);
        TextView kelasketua = (TextView) view.findViewById(R.id.textkelasketua);
        TextView jkketua = (TextView) view.findViewById(R.id.textjkketua);
        TextView jurusanketua = (TextView) view.findViewById(R.id.textjurusanketua);

        // wakil

        ImageView fotowakil = (ImageView) view.findViewById(R.id.img4);
        TextView namawakil = (TextView) view.findViewById(R.id.textnamawakil);
        TextView kelaswakil = (TextView) view.findViewById(R.id.textkelaswakil);
        TextView jkwakil = (TextView) view.findViewById(R.id.textjkwakil);
        TextView jurusanwakil = (TextView) view.findViewById(R.id.textjurusanwakil);
        TextView paslon= (TextView) view.findViewById(R.id.paslon_nomor);

        TextView slogan =(TextView) view.findViewById(R.id.textslogan);
        TextView visi =(TextView) view.findViewById(R.id.textvisi);
        TextView misi =(TextView) view.findViewById(R.id.textmisi);
        FloatingActionButton floatingActionButton= (FloatingActionButton) view.findViewById(R.id.pilihf);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog = new Dialog(getContext());
                customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customDialog.setCancelable(false);
                customDialog.setContentView(R.layout.alert_dialog);
                TextView idmhs = customDialog.findViewById(R.id.txtidmhs);
                TextView id_calon = customDialog.findViewById(R.id.txtidcalon);
                TextView txtisi = customDialog.findViewById(R.id.textisi);
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
                        callvote = apiService.vote(idmhs.getText().toString(), id_calon.getText().toString());
                        callvote.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
//                                                Log.w(TAG, "" + response.body());
                                    Intent intent = new Intent(getContext(), Main_end.class);
                                    String nrpmhs = idmhs.getText().toString();
                                    intent.putExtra("hasil", nrpmhs);
                                    startActivity(intent);
                                    customDialog.dismiss();
                                } else {
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
        call = apiService.getdetail(getActivity().getIntent().getStringExtra("idcalon"));
        call.enqueue(new Callback<List<ModelVote>>() {
            @Override
            public void onResponse(Call<List<ModelVote>> call, Response<List<ModelVote>> response) {
                if (response.isSuccessful()){
                    namaketua.setText(" "+response.body().get(0).getNama_ketua());
                    kelasketua.setText(" "+response.body().get(0).getKelas_ketua());
                    jurusanketua.setText(" "+response.body().get(0).getJurusan_ketua());
                    jkketua.setText(" "+response.body().get(0).getJk_ketua());
                    paslon.setText("Pasangan No 0"+response.body().get(0).getPaslon_nomor());

                    namawakil.setText(" "+response.body().get(0).getNama_wakil());
                    kelaswakil.setText(" "+response.body().get(0).getKelas_wakil());
                    jurusanwakil.setText(" "+response.body().get(0).getJurusan_wakil());
                    jkwakil.setText(" "+response.body().get(0).getJk_wakil());

                    visi.setText(" "+response.body().get(0).getVisi());
                    misi.setText(" "+response.body().get(0).getMisi());
                    slogan.setText(" "+response.body().get(0).getSlogan());

                    if (response.body().get(0).getFoto_ketua() != null && response.body().get(0).getFoto_ketua().length() > 0) {
                        Picasso.get().load("http://192.168.43.50/laravel/public/foto/" + response.body().get(0).getFoto_ketua()).into(fotoketua);
                    } else
                        Picasso.get().load(R.drawable.ic_account_box_black_24dp).into(fotoketua);

                    if (response.body().get(0).getFoto_wakil() != null && response.body().get(0).getFoto_wakil().length() > 0) {
                        Picasso.get().load("http://192.168.43.50/laravel/public/foto/" + response.body().get(0).getFoto_wakil()).into(fotowakil);
                    } else
                        Picasso.get().load(R.drawable.ic_account_box_black_24dp).into(fotowakil);

                }
            }

            @Override
            public void onFailure(Call<List<ModelVote>> call, Throwable t) {

            }
        });
        return  view;

    }
}