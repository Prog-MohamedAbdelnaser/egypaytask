package com.egypaytask;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jakewharton.rxbinding.widget.RxAdapterView;
import com.jakewharton.rxbinding.widget.RxTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.*;
import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity {

    HashMap<String,ViewItem> viewValuesHashMap =new HashMap<>();
    boolean isStillOpen=true;
    boolean isValidParams =false;
    LinearLayout linearLayout;
    ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Locale locale =new Locale("ar");
        Locale.setDefault(locale);
         linearLayout = new LinearLayout(this);

        // Defining the RelativeLayout layout parameters.
        // In this case I want to fill its parent
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(VERTICAL);
        super.setContentView(linearLayout);

        progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        InputStream inputStream = getResources().openRawResource(R.raw.data);
        String jsonString =Util.getStringFromInputStream(inputStream);
        Log.i("jsonString","jsonString "+ jsonString);


        Gson gson =new Gson();
        ViewItems viewItems = gson.fromJson(jsonString, ViewItems.class);
        Collections.sort(viewItems);

        handleViewItemsLayout(viewItems);

    }


    JSONObject getParams(){
        JSONObject obj=new JSONObject();
        JsonObject jsonObject =new JsonObject();
        for (String item : viewValuesHashMap.keySet()){
            if (viewValuesHashMap.get(item).getValue() !=null) {
                jsonObject.addProperty(item, viewValuesHashMap.get(item).getValue());
            }else {
                if (viewValuesHashMap.get(item).getIsRequired()){
                    showAlertDialog(MainActivity.this,"قم بملء جميع البيانات اولا","تنبيه");
                    isValidParams=false;
                    break;
                }
            }
        }


        if (isValidParams){
            send(obj);
        }
        Log.i("getParams","obj "+jsonObject.toString());

        return obj;

    }
/*Gson gson =new Gson();
		MultipleItems multiples  = gson.fromJson(multiple,MultipleItems.class);*/
    private void handleViewItemsLayout(ViewItems viewItems) {
        for (ViewItem viewItem:viewItems) {
            Log.i("handleViewItemsLayout","view "+viewItem.toString());
            switch (viewItem.getType()){
                case "date":{

                    addDateEditText(linearLayout,viewItem.getId(),InputType.TYPE_CLASS_DATETIME,viewItem);

                break;}
                case "select":{
                   addSpinner(linearLayout,viewItem.getId(),viewItem.getMultiple(),viewItem,viewItem.getIsRequired()) ;
                break;}
                case "string":{
                    addEditText(linearLayout,viewItem.getId(), InputType.TYPE_CLASS_TEXT,viewItem,viewItem.getIsRequired());
                break;}
                case "number":{
                    addEditText(linearLayout,viewItem.getId(), TYPE_CLASS_NUMBER,viewItem,viewItem.getIsRequired());
                break;}
                case "textarea":{
                    addEditText(linearLayout,viewItem.getId(),InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE,viewItem,viewItem.getIsRequired());
                break;
                }
            }
        }
        addButton();
    }

    private void addButton() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin=30;
        layoutParams.gravity= Gravity.BOTTOM;

        Button view  = new Button(this);
        view.setText("Submit");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidParams) {
                    getParams();
                }else {
                    showAlertDialog(MainActivity.this,"قم بملء جميع البيانات اولا","تنبيه");
                }
            }
        });
        linearLayout.addView(view);
    }

    private  EditText  addEditText(LinearLayout linearLayout,int id ,int inputType ,ViewItem viewItem,boolean isRequired ){
        viewValuesHashMap.put(String.valueOf(id),viewItem);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin=20;
        EditText view  = new EditText(this);
        view.setInputType(inputType);
        if (inputType == InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE){
            view.setSingleLine(false);
            view.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        }
        view.setId(id);
        if (viewItem.getDefaultValue()!=null)
        view.setHint(viewItem.getName());

        RxTextView.textChanges(view).subscribe(text->{

            if (isRequired && text.toString().isEmpty()){
                view.setError("Please entry "+viewItem.getName());
                isValidParams=false;

            }else {
                view.setError(null);
                isValidParams=true;

            }
            viewItem.setValue(text.toString()+"");
            if (!text.equals("")) viewValuesHashMap.put(String.valueOf(id),viewItem);

        },throwable -> {

        });
        view.setLayoutParams(layoutParams);
        linearLayout.addView(view);
        return  view;
    }

    boolean isShown =false;

    private  void  addDateEditText(LinearLayout linearLayout,Integer id ,int inputType ,ViewItem viewItem ){
        viewValuesHashMap.put(id.toString(),viewItem);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin=20;
        layoutParams.setMarginStart(80);
        TextView view  = new TextView(this);
        view.setId(id);

        view.setPadding(20,20,20,0);
        view.setTextSize(16);
        if (viewItem.getName()!=null) view.setText(viewItem.getName());
        view.setLayoutParams(layoutParams);

        RxTextView.textChanges(view).subscribe(t->{
            if (viewItem.getIsRequired()&&viewItem.getValue()==null){
                view.setError("من فضلك ادخل البيانات");
            }else view.setError(null);
        },throwable -> {});
        DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
            view.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
            viewItem.setValue(year+"-"+monthOfYear+"-"+dayOfMonth+"");

            viewValuesHashMap.put(id.toString(),viewItem);
        };
        final Calendar myCalendar = Calendar.getInstance();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isShown){
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                isShown =true;
            }else isShown =false;



            }
        });

        linearLayout.addView(view);
        addLine();
    }

    void addLine(){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1);
        layoutParams.topMargin=20;
        layoutParams.rightMargin=20;
        layoutParams.leftMargin=20;
        View view  = new View(this);
        view.setLayoutParams(layoutParams);
        view.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        linearLayout.addView(view);
    }

    private void addSpinner(LinearLayout linearLayout, int id, ArrayList<Multiple> list,ViewItem viewItem,boolean isRequired){
        viewValuesHashMap.put(String.valueOf(id),viewItem);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams containerlayoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams textviewLabelLayoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);


        LinearLayout container =new LinearLayout(this);
        Spinner spinner =new Spinner(this);
        TextView textviewLabel = new TextView(this);


        spinner.setLayoutParams(layoutParams);
        spinner.setId(id);
        spinner.setAdapter(SpinnerAdapter.createSpinnerAdapter(this,list,viewItem.getName()));
        layoutParams.weight=4.8f;
        //layoutParams.setMarginStart(20);

        textviewLabel.setLayoutParams(textviewLabelLayoutParams);
        textviewLabelLayoutParams.weight=0.2f;

        containerlayoutParams.setLayoutDirection(HORIZONTAL);
        container.setLayoutParams(containerlayoutParams);
        container.setWeightSum(5);
        containerlayoutParams.topMargin=10;
        container.addView(spinner);
        container.addView(textviewLabel);



        RxAdapterView.itemSelections(spinner).subscribe(item->{

            if (isRequired && item == 0) {
                isValidParams=false;
                textviewLabel.setError(viewItem.getName()+"من فضلك قم بتحديد ");
            }else {
                isValidParams=true;
                textviewLabel.setError(null);
            }
            viewItem.setValue(list.get(item).getName());

            if (item > 0) viewValuesHashMap.put(String.valueOf(id), viewItem);

        },throwable -> {});

        linearLayout.addView(container);
        addLine();

    }

    void showAlertDialog(Context context,String message,String title ) {
      AlertDialog.Builder    alertDialogBuilder =new  AlertDialog.Builder(context);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton("موافق", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog alertDialog =alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(wrap(newBase));
    }

    Context wrap(Context context)  {

        Configuration config = context.getResources().getConfiguration();


        Resources resourc = context.getResources();

        DisplayMetrics dm = resourc.getDisplayMetrics();

            Locale locale =new  Locale("ar");
            Locale.setDefault(locale);
            config.setLocale(locale);
            resourc.updateConfiguration(config, dm);


        return context.createConfigurationContext(config);
    }

    void send(Object o){
        CompositeDisposable compositeDisposable =new CompositeDisposable();
                compositeDisposable.add(
                        new ApiClient().getInstance().sendData(o)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .doOnSubscribe(it-> {
                                    progressDialog.show();
                                }).doFinally(()->{progressDialog.dismiss();}).subscribe(t->{
                            showAlertDialog(MainActivity.this,"Success","");
                        },throwable -> {
                           showAlertDialog(MainActivity.this,throwable.getMessage(),"Server Side Notice");
                            throwable.printStackTrace();}));
    }




}

