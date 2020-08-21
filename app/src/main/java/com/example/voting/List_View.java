package com.example.voting;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.voting.Model.ModelCalon;
import com.example.voting.Model.ModelVote;

import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;

public class List_View  extends BaseAdapter {

    Context context;
    List<ModelCalon> list;
    private  ArrayList<String> arrayList;

    public  List_View(Context context, List<ModelCalon> list){
        this.context=context;
        this.list=list;
        arrayList= new ArrayList<>();

    }
    public void addItem(String item) {
        this.arrayList.add(item);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view== null){
            LayoutInflater inflater = LayoutInflater.from(this.context);
            view= inflater.inflate(R.layout.list_view,null);
        }

        ModelCalon maincalon= list.get(i);
        TextView nomorauto= (TextView) view.findViewById(R.id.autonum);
        TextView textjudul= view.findViewById(R.id.logo);
        TextView txtketua = view.findViewById(R.id.text1);
        TextView txtwakil = view.findViewById(R.id.text2);
        ImageView foto_paslon = view.findViewById((R.id.imgpaslon));;
        textjudul.setVisibility(View.GONE);
        nomorauto.setText(maincalon.getPaslon_nomor());
        textjudul.setText(maincalon.getId());
        txtketua.setText(maincalon.getNama_ketua());
        txtwakil.setText(maincalon.getNama_wakil());
        if (maincalon.getFoto_paslon()!=null && maincalon.getFoto_paslon().length() > 0){
            Picasso.get().load("http://192.168.43.50/laravel/public/foto_paslon/"+ maincalon.getFoto_paslon()).into(foto_paslon);
        }
        else
            Picasso.get().load(R.drawable.ic_account_box_black_24dp).into(foto_paslon);

        return view;

    }
}
