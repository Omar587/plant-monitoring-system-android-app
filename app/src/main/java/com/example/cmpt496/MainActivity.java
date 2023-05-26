package com.example.cmpt496;

import static java.util.logging.Logger.global;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.ekn.gruzer.gaugelibrary.ArcGauge;
import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
     static boolean loadingScreenCalled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_activity);
        //THis finds views for the custom action bar
        Spinner tower_selector = findViewById(R.id.towerSpinner);
        ImageView menu_btn = findViewById(R.id.menu_btn);
        ImageView more_btn = findViewById(R.id.more_btn);

        //This makes an adapter for the spinner (the drop down list of towers)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.towers_Array,  R.layout.spinner_color);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tower_selector.setAdapter(adapter);
        //==================================================================

        com.ekn.gruzer.gaugelibrary.Range ppm_Range1,ppm_Range2,ppm_Range3,ph_Range1,ph_Range2,ph_Range3,temp_Range1, temp_Range2,temp_Range3, humid_Range1, humid_Range2, humid_Range3;
        HalfGauge ppm = findViewById(R.id.ppm);
        HalfGauge ph = findViewById(R.id.ph);
        HalfGauge humidity = findViewById(R.id.humidity);
        HalfGauge temperature = findViewById(R.id.temperature);
        ApiStuff apiStuff = new ApiStuff();
        apiStuff.getAccessToken();
        ApiStuff.getHistoricData();


       // List<Integer> lr_colors = ["A", "B", "C"];

        ppm.setMinValue(1);ppm.setMaxValue(1200);
        ppm_Range1 = new com.ekn.gruzer.gaugelibrary.Range();
        ppm_Range2 = new com.ekn.gruzer.gaugelibrary.Range();
        ppm_Range3 = new com.ekn.gruzer.gaugelibrary.Range();
        ppm_Range1.setFrom(1);ppm_Range1.setTo(650);ppm_Range1.setColor(Color.rgb(173, 23, 28)); //173, 23, 28
        ppm_Range2.setFrom(650);ppm_Range2.setTo(900);ppm_Range2.setColor(Color.rgb(21, 158, 34));
        ppm_Range3.setFrom(900);ppm_Range3.setTo(1200);ppm_Range3.setColor(Color.rgb(173, 23, 28));
        ppm.addRange(ppm_Range1);
        ppm.addRange(ppm_Range2);
        ppm.addRange(ppm_Range3);


        ph_Range1 = new com.ekn.gruzer.gaugelibrary.Range();
        ph_Range2 = new com.ekn.gruzer.gaugelibrary.Range();
        ph_Range3 = new com.ekn.gruzer.gaugelibrary.Range();
        ph.setMinValue(1);ph.setMinValueTextColor(Color.BLACK);
        ph.setMaxValue(14);ph.setMaxValueTextColor(Color.BLACK);
        ph_Range1.setFrom(1);ph_Range1.setTo(6);ph_Range1.setColor(Color.rgb(173, 23, 28));
        ph_Range2.setFrom(6);ph_Range2.setTo(9);ph_Range2.setColor(Color.rgb(21, 158, 34));
        ph_Range3.setFrom(9);ph_Range3.setTo(14);ph_Range3.setColor(Color.rgb(173, 23, 28));
        ph.addRange(ph_Range1);
        ph.addRange(ph_Range2);
        ph.addRange(ph_Range3);



        temperature.setMinValue(-20);
        temperature.setMaxValue(60); temperature.setValueColor(Color.BLACK);
        temp_Range1 = new com.ekn.gruzer.gaugelibrary.Range();
        temp_Range2 = new com.ekn.gruzer.gaugelibrary.Range();
        temp_Range3 = new com.ekn.gruzer.gaugelibrary.Range();
        temp_Range1.setFrom(-20);temp_Range1.setTo(18);temp_Range1.setColor(Color.rgb(173, 23, 28));
        temp_Range2.setFrom(18);temp_Range2.setTo(30);temp_Range2.setColor(Color.rgb(21, 158, 34));
        temp_Range3.setFrom(30);temp_Range3.setTo(60);temp_Range3.setColor(Color.rgb(173, 23, 28));
        temperature.addRange(temp_Range1);
        temperature.addRange(temp_Range2);
        temperature.addRange(temp_Range3);

        humidity.setMinValue(-0);
        humidity.setMaxValue(90); humidity.setValueColor(Color.BLACK);
        humid_Range1 = new com.ekn.gruzer.gaugelibrary.Range();
        humid_Range2 = new com.ekn.gruzer.gaugelibrary.Range();
        humid_Range3 = new com.ekn.gruzer.gaugelibrary.Range();
        humid_Range1.setFrom(0);humid_Range1.setTo(30);humid_Range1.setColor(Color.rgb(173, 23, 28));
        humid_Range2.setFrom(30);humid_Range2.setTo(60);humid_Range2.setColor(Color.rgb(21, 158, 34));
        humid_Range3.setFrom(60);humid_Range3.setTo(90);humid_Range3.setColor(Color.rgb(173, 23, 28));//21, 158, 34
        humidity.addRange(humid_Range1);
        humidity.addRange(humid_Range2);
        humidity.addRange(humid_Range3);
        humidity.addRange(humid_Range3);

        DecimalFormat df = new DecimalFormat("0.00");
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                apiStuff.getAccessToken();
                ApiStuff.getHistoricData();
                ppm.setValue(Double.parseDouble(df.format(ApiStuff.getPh())));
                ph.setValue(Double.parseDouble(df.format(ApiStuff.getPpm())));
                humidity.setValue(Double.parseDouble(df.format(ApiStuff.getHumidity())));
                temperature.setValue(Double.parseDouble(df.format(ApiStuff.getTemperature())));

            }
        }, 0, 5*1000);
        //if (!loadingScreenCalled){
        // loadingScreenCalled = true;

            displayLoadingScreen(); };
  //  }

    private void displayLoadingScreen() {
            // Create a new ProgressDialog
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Talking to your plants.........");
            dialog.setCancelable(false);

            // Show the ProgressDialog
            dialog.show();
            switchGraphs();
            // Use a Handler and Runnable to hide the ProgressDialog after 10 seconds
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.hide();
                    switchGraphs();
                }
            }, 12000);
        }




    /******************* GRAPHS *************************************************/

    private void switchGraphs(){
        GraphView graph =  (GraphView) findViewById(R.id.graph);
        graph.removeAllSeries();
        humidityGraph();
        graph.setOnClickListener(new View.OnClickListener() {

             boolean temp = true;
             boolean ph = false;
             boolean humidity =false;
             boolean ppm = false;



            @Override
            public void onClick(View view) {

                if (temp){
                    temperatureGraph();

                     ph = true;
                     humidity =false;
                     ppm = false;
                     temp = false;
                }

                else if(ph){
                    phGraph();
                     temp = false;
                     humidity =true;
                     ppm = false;
                     ph = false;
                }

                else if(humidity){
                    humidityGraph();
                    temp = false;
                    humidity =false;
                    ppm = true;
                    ph = false;

                }

                else if (ppm){
                    ppmGraph();
                    temp = true;
                    humidity =false;
                    ppm = false;
                    ph = false;

                }




            }
        });

    }


    private GraphView phGraph(){

        GraphView graph =  (GraphView) findViewById(R.id.graph);
        graph.removeAllSeries();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();


        for (int x=1; x<=14; x++){
            Double i = ApiStuff.getpHData()[x-1];
            series.appendData(new DataPoint(x,i), true,14);
        }



        //graph title
        graph.addSeries(series);

        //axis titles
        GridLabelRenderer gridLabel= graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Last 14 days");
        gridLabel.setVerticalAxisTitle("PH");

        //graph styling
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        glr.setPadding(36);
        glr.getVerticalLabelsAlign();
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(15);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(14);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        return graph;



    }


    private GraphView ppmGraph(){

        GraphView graph =  (GraphView) findViewById(R.id.graph);
        graph.removeAllSeries();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        double y=0;

        for (int x=1; x<=14; x++){

            Double i = ApiStuff.getPpmData()[x-1];
            series.appendData(new DataPoint(x,i), true,14);
        }

        //graph title
        graph.addSeries(series);

        //axis titles
        GridLabelRenderer gridLabel= graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Last 14 days");
        gridLabel.setVerticalAxisTitle("ppm");

        //graph styling
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        glr.setPadding(36);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(15);
        graph.getViewport().setMinY(Collections.min(Arrays.asList(ApiStuff.getPpmData()))-10);
        graph.getViewport().setMaxY(Collections.max(Arrays.asList(ApiStuff.getPpmData()))+10);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        return graph;
    }



    private GraphView humidityGraph(){

        GraphView graph =  (GraphView) findViewById(R.id.graph);
        graph.removeAllSeries();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        double y=0;

        for (int x=1; x<=14; x++){

            Double i = ApiStuff.getHumidData()[x-1];
            series.appendData(new DataPoint(x,i), true,14);
        }

        //graph title
        graph.addSeries(series);

        //axis titles
        GridLabelRenderer gridLabel= graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Last 14 days");
        gridLabel.setVerticalAxisTitle("Humidity");

        //graph styling
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        glr.setPadding(36);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(15);
        graph.getViewport().setMinY(Collections.min(Arrays.asList(ApiStuff.getHumidData()))-5);
        graph.getViewport().setMaxY(Collections.max(Arrays.asList(ApiStuff.getHumidData()))+5);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        return graph;


    }


    private GraphView temperatureGraph() {

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.removeAllSeries();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        double y = 0;

        for (int x=1; x<=14; x++){

            Double i = ApiStuff.getTempData()[x-1];
            series.appendData(new DataPoint(x,i), true,30);
        }

        //graph title
        graph.addSeries(series);

        //axis titles
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Last 14 days");
        gridLabel.setVerticalAxisTitle("Temperature");

        //graph styling
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        glr.setPadding(48);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(15);
        graph.getViewport().setMinY(Collections.min(Arrays.asList(ApiStuff.getTempData()))-5);
        graph.getViewport().setMaxY(Collections.max(Arrays.asList(ApiStuff.getTempData()))+5);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        return graph;

    }


    private void displayTowerListActivity(){

        Intent intent = new Intent(MainActivity.this, PlantListActivity.class);
        startActivity(intent);
    }



    public void showConfigMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.export_btn){
                    Toast.makeText(MainActivity.this, "Downloading to /Downloads", Toast.LENGTH_SHORT).show();
                    mergePDFS();
                }
                if (item.getItemId() == R.id.config_btn){
                    displayTowerListActivity();

                }
                return false;
            }
        });
        popupMenu.show();
    }

    private static double findMean(String parameter){
        Double[] data = new Double[0];
        if (parameter.equals("temp")){
            data = ApiStuff.getTempData();
        }
        if (parameter.equals("ppm")){
            data = ApiStuff.getPpmData();
        }
        if (parameter.equals("ph")){
            data = ApiStuff.getpHData();
        }
        if (parameter.equals("humid")){
            data = ApiStuff.getHumidData();
        }
        double total = 0;
        
        for(int i=0; i<data.length; i++){
            total = total + data[i];
        }
        double average = total/data.length;
        return round(average,2);
    }

    private static double findMedian(String parameter) {
        Double[] metricList = new Double[0];
        if (parameter.equals("temp")){
            metricList = ApiStuff.getTempData();
        }
        if (parameter.equals("ppm")){
            metricList = ApiStuff.getPpmData();
        }
        if (parameter.equals("ph")){
            metricList = ApiStuff.getpHData();
        }
        if (parameter.equals("humid")){
            metricList = ApiStuff.getHumidData();
        }
        int n = metricList.length;
        double median=0;

        if (n % 2 == 0) {
            int firstHalf = (n / 2);
            int secondHalf = firstHalf - 1;
            median = (double) (metricList[firstHalf] + metricList[secondHalf]) / 2.0;


            /*median case for odd */
        } else {
            int half = (n / 2);
            median =  metricList[half];

        }
        return median;
    }

    private static double findMode(String parameter){
        Double[] arrayEx1 = new Double[0];

        if (parameter.equals("temp")){
            arrayEx1 = ApiStuff.getTempData();
        }
        if (parameter.equals("ppm")){
            arrayEx1 = ApiStuff.getPpmData();
        }
        if (parameter.equals("ph")){
            arrayEx1 = ApiStuff.getpHData();
        }
        if (parameter.equals("humid")){
            arrayEx1 = ApiStuff.getHumidData();
        }
       // Arrays.sort(arrayEx1);
        int n=arrayEx1.length;

        int countofmax = 1;
        double temp = arrayEx1[0];
        int count = 1;

        for (int i = 1; i < arrayEx1.length; i++)
        {
            if (arrayEx1[i] == arrayEx1[i - 1])
                count++;
            else
            {
                if (count > countofmax)
                {
                    countofmax = count;
                    temp = arrayEx1[i - 1];
                }
                count = 1;
            }
        }



        if (count > countofmax)
        {
            countofmax = count;
            temp = arrayEx1[n - 1];
        }

        return round(temp,2);
    }
    private static double findMax(String parameter){
        Double[] data = new Double[0];

        if (parameter.equals("temp")){
            data = ApiStuff.getTempData();
        }
        if (parameter.equals("ppm")){
            data = ApiStuff.getPpmData();
        }
        if (parameter.equals("ph")){
            data = ApiStuff.getpHData();
        }
        if (parameter.equals("humid")){
            data = ApiStuff.getHumidData();
        }
        double max = data[0];
        for (int i = 0 ; i <data.length;i++){

            if(data[i]>max){max = data[i];}
        }

        return round(max,2);
    }

    private static double findMin(String parameter){
        Double[] data = new Double[0];

        if (parameter.equals("temp")){
            data = ApiStuff.getTempData();
        }
        if (parameter.equals("ppm")){
            data = ApiStuff.getPpmData();
        }
        if (parameter.equals("ph")){
            data = ApiStuff.getpHData();
        }
        if (parameter.equals("humid")){
            data = ApiStuff.getHumidData();
        }
        double min = data[0];
        for (int i = 0 ; i <data.length;i++){

            if(data[i]<min){min = data[i];}
        }

        return round(min,2);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    private Bitmap getppmBitmap(){
        GraphView view = ppmGraph();

        view.setDrawingCacheEnabled(true);

        view.buildDrawingCache();

        return view.getDrawingCache();
    }
    private Bitmap getphBitmap(){
        GraphView pHView = phGraph();
        pHView.setDrawingCacheEnabled(true);

        pHView.buildDrawingCache();

        return pHView.getDrawingCache();
    }
    private Bitmap gettempBitmap(){
        GraphView tempView = temperatureGraph();

        tempView.setDrawingCacheEnabled(true);

        tempView.buildDrawingCache();

        return tempView.getDrawingCache();
    }
    private Bitmap gethumidBitmap(){
        GraphView humidView = humidityGraph();

        humidView.setDrawingCacheEnabled(true);

        humidView.buildDrawingCache();

        return humidView.getDrawingCache();
    }
    private void createPdf(){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"/PlantGroupData" + ApiStuff.getFromDate()+ ".pdf");
        PdfDocument plantPdf = new PdfDocument();
        Paint myPaint = new Paint();
        Paint boldPaint = new Paint();
        boldPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        boldPaint.setTextAlign(Paint.Align.CENTER);

        PdfDocument.PageInfo pageinfo1 = new PdfDocument.PageInfo.Builder(400,600,1).create();
        PdfDocument.Page page1 = plantPdf.startPage(pageinfo1);
        Canvas canvas  = page1.getCanvas();
        int xPos = (canvas.getWidth() / 2);

        canvas.drawText("Data for Plant Group 1",xPos,50,boldPaint);
        canvas.drawText("PPM DATA",xPos,70,boldPaint);
        canvas.drawText("Statistics collected from " + ApiStuff.getToDate() + " to " + ApiStuff.getFromDate(),80,90,myPaint);

        float left = 60;
        float top = 400;
        canvas.drawBitmap(getppmBitmap(),null,new RectF(left, top,  left+250, top+200),myPaint);
        plantPdf.finishPage(page1);

        try {
            plantPdf.writeTo(new FileOutputStream(file));
        }catch (IOException e ){
            e.printStackTrace();
        }

       PdfDocument.PageInfo pageinfo2 = new PdfDocument.PageInfo.Builder(400,600,2).create();
        PdfDocument.Page page2 = plantPdf.startPage(pageinfo2);
        Canvas canvas2  = page2.getCanvas();

        canvas2.drawText("PH Data",xPos,70,boldPaint);
        canvas2.drawText("Statistics collected from " + ApiStuff.getToDate() + " to " + ApiStuff.getFromDate(),80,90,myPaint);

        canvas2.drawBitmap(getphBitmap(),null,new RectF(left, top,  left+250, top+200),myPaint);
        plantPdf.finishPage(page2);
        try {
            plantPdf.writeTo(new FileOutputStream(file));
        }catch (IOException e ){
            e.printStackTrace();
        }

        PdfDocument.PageInfo pageinfo3 = new PdfDocument.PageInfo.Builder(400,600,3).create();
        PdfDocument.Page page3 = plantPdf.startPage(pageinfo3);
        Canvas canvas3  = page3.getCanvas();

        canvas3.drawText("TEMPERATURE DATA",xPos,70,boldPaint);
        canvas3.drawText("Statistics collected from " + ApiStuff.getToDate() + " to " + ApiStuff.getFromDate(),80,90,myPaint);


        canvas3.drawBitmap(gettempBitmap(),null,new RectF(left, top,  left+250, top+200),myPaint);
        plantPdf.finishPage(page3);
        try {
            plantPdf.writeTo(new FileOutputStream(file));
        }catch (IOException e ){
            e.printStackTrace();
        }

        PdfDocument.PageInfo pageinfo4 = new PdfDocument.PageInfo.Builder(400,600,4).create();
        PdfDocument.Page page4 = plantPdf.startPage(pageinfo4);
        Canvas canvas4 = page4.getCanvas();

        canvas4.drawText("HUMIDITY DATA",xPos,70,boldPaint);
        canvas4.drawText("Statistics collected from " + ApiStuff.getToDate() + " to " + ApiStuff.getFromDate(),80,90,myPaint);
       canvas4.drawText("Calculated Mean = " + findMean("humid"),40,110,myPaint);
       canvas4.drawText("Calculated Median = " + findMedian("humid"),40,130,myPaint);
       canvas4.drawText("Calculated Mode = " + findMode("humid"),40,150,myPaint);
        canvas4.drawText("Maximum Value = " + findMax("humid"),40,170,myPaint);
        canvas4.drawText("Minimum Value = " + findMin("humid"),40,190,myPaint);


        canvas4.drawBitmap(gethumidBitmap(),null,new RectF(left, top,  left+250, top+200),myPaint);
        plantPdf.finishPage(page4);
        try {
            plantPdf.writeTo(new FileOutputStream(file));
        }catch (IOException e ){
            e.printStackTrace();
        }



        plantPdf.close();
    }

    private File createPdftest(){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"/PPM " + ApiStuff.getFromDate()+ ".pdf");
        PdfDocument plantPdf = new PdfDocument();
        Paint myPaint = new Paint();
        Paint boldPaint = new Paint();
        boldPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        boldPaint.setTextAlign(Paint.Align.CENTER);

        PdfDocument.PageInfo pageinfo1 = new PdfDocument.PageInfo.Builder(400,600,1).create();
        PdfDocument.Page page1 = plantPdf.startPage(pageinfo1);
        Canvas canvas  = page1.getCanvas();
        int xPos = (canvas.getWidth() / 2);

        canvas.drawText("Data for Plant Group 1",xPos,50,boldPaint);
        canvas.drawText("PPM DATA",xPos,70,boldPaint);
        canvas.drawText("Statistics collected from " + ApiStuff.getToDate() + " to " + ApiStuff.getFromDate(),80,90,myPaint);
        canvas.drawText("Calculated Mean = " + findMean("ppm"),40,110,myPaint);
        canvas.drawText("Calculated Median = " + findMedian("ppm"),40,130,myPaint);
        canvas.drawText("Calculated Mode = " + findMode("ppm"),40,150,myPaint);
        canvas.drawText("Maximum Value = " + findMax("ppm"),40,170,myPaint);
        canvas.drawText("Minimum Value = " + findMin("ppm"),40,190,myPaint);
        float left = 60;
        float top = 400;
        Bitmap ppmBitmap = getppmBitmap();
        if (ppmBitmap != null) {
            canvas.drawBitmap(ppmBitmap,null,new RectF(left, top,  left+250, top+200),myPaint);
        } else {
            Log.d("createPdf", "getppmBitmap returned null");
        }
        plantPdf.finishPage(page1);

        try {
            plantPdf.writeTo(new FileOutputStream(file));
        }catch (IOException e ){
            e.printStackTrace();
        }
        plantPdf.close();
        return file;
    }

    private File createPHFile(){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"/PH " + ApiStuff.getFromDate()+ ".pdf");
        PdfDocument plantPdf = new PdfDocument();
        Paint myPaint = new Paint();
        Paint boldPaint = new Paint();
        boldPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        boldPaint.setTextAlign(Paint.Align.CENTER);
        PdfDocument.PageInfo pageinfo2 = new PdfDocument.PageInfo.Builder(400,600,1).create();
        PdfDocument.Page page2 = plantPdf.startPage(pageinfo2);
        Canvas canvas2  = page2.getCanvas();
        int xPos = (canvas2.getWidth() / 2);
        canvas2.drawText("PH Data",xPos,70,boldPaint);
        canvas2.drawText("Statistics collected from " + ApiStuff.getToDate() + " to " + ApiStuff.getFromDate(),80,90,myPaint);
        canvas2.drawText("Calculated Mean = " + findMean("ph"),40,110,myPaint);
        canvas2.drawText("Calculated Median = " + findMedian("ph"),40,130,myPaint);
        canvas2.drawText("Calculated Mode = " + findMode("ph"),40,150,myPaint);
        canvas2.drawText("Maximum Value = " + findMax("ph"),40,170,myPaint);
        canvas2.drawText("Minimum Value = " + findMin("ph"),40,190,myPaint);
        float left = 60;
        float top = 400;
        Bitmap phBitmap = getphBitmap();
        if (phBitmap != null) {
            canvas2.drawBitmap(phBitmap,null,new RectF(left, top,  left+250, top+200),myPaint);
        } else {
            Log.d("createPdf", "getphBitmap returned null");
        }
        plantPdf.finishPage(page2);

        try {
            plantPdf.writeTo(new FileOutputStream(file));
        }catch (IOException e ){
            e.printStackTrace();
        }
        plantPdf.close();
        return file;

    }

    private File createTempFile(){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"/Temperature " + ApiStuff.getFromDate()+ ".pdf");
        PdfDocument plantPdf = new PdfDocument();
        Paint myPaint = new Paint();
        Paint boldPaint = new Paint();
        boldPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        boldPaint.setTextAlign(Paint.Align.CENTER);

        PdfDocument.PageInfo pageinfo3 = new PdfDocument.PageInfo.Builder(400,600,1).create();
        PdfDocument.Page page3 = plantPdf.startPage(pageinfo3);
        Canvas canvas3  = page3.getCanvas();
        int xPos = (canvas3.getWidth() / 2);
        canvas3.drawText("TEMPERATURE DATA",xPos,70,boldPaint);
        canvas3.drawText("Statistics collected from " + ApiStuff.getToDate() + " to " + ApiStuff.getFromDate(),80,90,myPaint);
        canvas3.drawText("Calculated Mean = " + findMean("temp"),40,110,myPaint);
        canvas3.drawText("Calculated Median = " + findMedian("temp"),40,130,myPaint);
        canvas3.drawText("Calculated Mode = " + findMode("temp"),40,150,myPaint);
        canvas3.drawText("Maximum Value = " + findMax("temp"),40,170,myPaint);
        canvas3.drawText("Minimum Value = " + findMin("temp"),40,190,myPaint);
        float left = 60;
        float top = 400;

        canvas3.drawBitmap(gettempBitmap(),null,new RectF(left, top,  left+250, top+200),myPaint);
        plantPdf.finishPage(page3);
        try {
            plantPdf.writeTo(new FileOutputStream(file));
        }catch (IOException e ){
            e.printStackTrace();
        }
        plantPdf.close();
        return file;
    }
    private File createHumidFile(){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"/Humidity " + ApiStuff.getFromDate()+ ".pdf");
        PdfDocument plantPdf = new PdfDocument();
        Paint myPaint = new Paint();
        Paint boldPaint = new Paint();
        boldPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        boldPaint.setTextAlign(Paint.Align.CENTER);
        PdfDocument.PageInfo pageinfo4 = new PdfDocument.PageInfo.Builder(400,600,4).create();
        PdfDocument.Page page4 = plantPdf.startPage(pageinfo4);
        Canvas canvas4 = page4.getCanvas();
        int xPos = (canvas4.getWidth() / 2);
        canvas4.drawText("HUMIDITY DATA",xPos,70,boldPaint);
        canvas4.drawText("Statistics collected from " + ApiStuff.getToDate() + " to " + ApiStuff.getFromDate(),80,90,myPaint);
        canvas4.drawText("Calculated Mean = " + findMean("humid"),40,110,myPaint);
        canvas4.drawText("Calculated Median = " + findMedian("humid"),40,130,myPaint);
        canvas4.drawText("Calculated Mode = " + findMode("humid"),40,150,myPaint);
        canvas4.drawText("Maximum Value = " + findMax("humid"),40,170,myPaint);
        canvas4.drawText("Minimum Value = " + findMin("humid"),40,190,myPaint);

        float left = 60;
        float top = 400;
        canvas4.drawBitmap(gethumidBitmap(),null,new RectF(left, top,  left+250, top+200),myPaint);
        plantPdf.finishPage(page4);
        try {
            plantPdf.writeTo(new FileOutputStream(file));
        }catch (IOException e ){
            e.printStackTrace();
        }

        plantPdf.close();
        return file;
    }
    private void mergePDFS() {
        createPdftest();
        createPHFile();
        createHumidFile();
        createTempFile();
}
}




