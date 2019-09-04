package com.example.log_in;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class StayAdapter extends ArrayAdapter<Film> {
    private LayoutInflater inflater;
    private int layout;
    private List<Film> apInventorList;

    public StayAdapter(Context context, int resource, List<Film> apIconnectorList) {
        super(context, resource, apIconnectorList);
        this.apInventorList = apIconnectorList;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

       ImageView flagView = (ImageView) view.findViewById(R.id.flag);
        TextView nameView = (TextView) view.findViewById(R.id.name);

        Film apIconnector = apInventorList.get(position);
 //        APIconnector api = new APIconnector();


        //String[] info = api.FilmConnect().split(" ");
        //Picasso.with(getContext()).load(apIconnector.getPath()).into(flagView);
        ImageManager.fetchImage(apIconnector.getPath_img(),flagView);
        nameView.setText(apIconnector.getName());


        return view;
    }
}
