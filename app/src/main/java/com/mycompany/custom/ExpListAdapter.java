package com.mycompany.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mycompany.mynotes.R;
import com.mycompany.pojo.Crypt;
import com.mycompany.pojo.Not;
import com.mycompany.pojo.Sabitler;
import com.mycompany.util.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> basliklar;
    private HashMap<String, ArrayList<Not>> icerik;
    //LayoutInflater inflater;

    public ExpListAdapter(Context context, ArrayList<String> basliklar, HashMap<String, ArrayList<Not>> icerik) {
        super();
        this.context = context;
        this.basliklar = basliklar;
        this.icerik = icerik;

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return icerik.get(basliklar.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        // TODO Auto-generated method stub
        final Not not = (Not) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.notlar_layout, null);
        }

        TextView baslik = (TextView) convertView.findViewById(R.id.txtBaslik);
        Crypt crypt = new Crypt();
        try {
            baslik.setText(crypt.decrypt(not.getKonu(), Sabitler.loginPassword));
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView tarih = (TextView) convertView.findViewById(R.id.txtTarih);
        tarih.setText(not.getKayittarihi());

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.chkSecim);
        checkBox.setChecked(false);
        checkBox.setVisibility(View.INVISIBLE);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return icerik.get(basliklar.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return basliklar.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return basliklar.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        String baslik = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.liste_grup, null);
        }
        Crypt crypt = new Crypt();
        TextView grup = (TextView) convertView.findViewById(R.id.txtGrup);
        try {
            grup.setText(crypt.decrypt(baslik,Sabitler.loginPassword));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return true;
    }

}
