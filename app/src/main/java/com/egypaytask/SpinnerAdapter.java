package com.egypaytask;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<Multiple> {

    ArrayList<Multiple> multiples ;
    String header ;
    public static  SpinnerAdapter createSpinnerAdapter(Context context,ArrayList<Multiple> list,String header){
        return  new SpinnerAdapter(context,android.R.layout.simple_list_item_1,list,header);
    }

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Multiple> objects,String header) {
        super(context, resource, objects);
        this.header =header;
        Multiple multipleHeader = new Multiple();
        multipleHeader.setName(header);
        multipleHeader.setValue(header);
        objects.set(0,multipleHeader);
        this.multiples =objects;
    }

    @Override
    public boolean isEnabled(int position) {
        return  position > 0;

    }

    private ColorStateList defaultTextColors = null;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);

        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            if (defaultTextColors == null) {
                defaultTextColors =textView.getTextColors();
            }
            Log.i("Adapte",""+position);


            if (position == 0) {
                textView.setText(header);
                textView.setTextColor(  textView.getHintTextColors());
            }else{
                textView.setTextColor(defaultTextColors);
                Multiple multiple =multiples.get(position);
                Log.i("Adapte",""+multiple.getName());

                textView.setText(multiple.getName());

            }

        }

        return super.getDropDownView(position, convertView, parent);
    }
}
