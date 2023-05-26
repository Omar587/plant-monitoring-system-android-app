package com.example.cmpt496;

//import org.apache.commons.io.IOUtils;

import android.app.Application;
import android.os.StrictMode;

import java.io.IOException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.ElementType;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class ApiStuff extends Application {
    public static String token;
    public static float temperature;
    public static float humidity;
    public static float ppm;
    public static float ph;
    public static Double[] tempData = {0.0, 0.0,0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    public static Double[] ppmData = {0.0, 0.0,0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    public static Double[] humidData =  {0.0, 0.0,0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    public static Double[] pHData =  {0.0, 0.0,0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    public static String fromDate;
    public static String toDate;

    public static String getToken() {
        return token;
    }

    public static float getTemperature() {
        return temperature;
    }

    public static float getHumidity() {
        return humidity;
    }

    public static float getPpm() {
        return ppm;
    }

    public static float getPh() {
        return ph;
    }

    public static Double[] getTempData() {
        return tempData;
    }

    public static Double[] getPpmData() {
        return ppmData;
    }

    public static Double[] getHumidData() {
        return humidData;
    }

    public static Double[] getpHData() {
        return pHData;
    }

    public static String getFromDate() {
        return fromDate;
    }

    public static String getToDate() {
        return toDate;
    }

    public void getAccessToken(){
        Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {

            try{
                URL url = new URL("https://api2.arduino.cc/iot/v1/clients/token");
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("POST");

                httpConn.setRequestProperty("content-type", "application/x-www-form-urlencoded");

                httpConn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
                writer.write("grant_type=client_credentials&client_id=RdwLxijh6clYPQQLCR4ZsgiCCxiMSQSj&client_secret=d4IdBzQPJDrGogNrkleaCbiWBkWWz2Xg2PlHq3RHJsUUHnaS6Vclvoa8JcqBoyAq&audience=https://api2.arduino.cc/iot");
                writer.flush();
                writer.close();
                httpConn.getOutputStream().close();

                InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                        ? httpConn.getInputStream()
                        : httpConn.getErrorStream();
                Scanner s = new Scanner(responseStream).useDelimiter("\\A");
                String response = s.hasNext() ? s.next() : "";
                //System.out.println(response);
                response = response.substring(response.indexOf(":")+1);
                response = response.substring(0,response.indexOf(":"));
                response = response.substring(response.indexOf("\"")+1);
                response = response.substring(0,response.indexOf("\""));
                //System.out.println(response);
                ApiStuff.token = response;

                url = new URL("https://api2.arduino.cc/iot/v2/dashboards/27a66be0-673e-4487-a007-e0a54af565a3");
                httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("GET");

                httpConn.setRequestProperty("Authorization", "Bearer "+ApiStuff.token);

                responseStream = httpConn.getResponseCode() / 100 == 2
                        ? httpConn.getInputStream()
                        : httpConn.getErrorStream();
                s = new Scanner(responseStream).useDelimiter("\\A");
                response = s.hasNext() ? s.next() : "";
                //System.out.println(response);
                String Values[] = response.split("last_value");

                String tmpString = Values[1].substring(Values[1].indexOf(":")+1);
                tmpString = tmpString.replaceAll(",","");
                tmpString = tmpString.replaceAll("\"","");
                ApiStuff.temperature= Float.parseFloat(tmpString);


                tmpString = Values[3].substring(Values[3].indexOf(":")+1);
                tmpString = tmpString.replaceAll(",","");
                tmpString = tmpString.replaceAll("\"","");
                ApiStuff.humidity= Float.parseFloat(tmpString);


                tmpString = Values[5].substring(Values[5].indexOf(":")+1);
                tmpString = tmpString.replaceAll(",","");
                tmpString = tmpString.replaceAll("\"","");
                ApiStuff.ph= Float.parseFloat(tmpString);


                tmpString = Values[7].substring(Values[7].indexOf(":")+1);
                tmpString = tmpString.replaceAll(",","");
                tmpString = tmpString.replaceAll("\"","");
                ApiStuff.ppm= Float.parseFloat(tmpString);

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    });
    thread.start();

    }

    public static void getHistoricData(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat df = new DecimalFormat("#.##");
        Date date = new Date();

        Calendar cal = Calendar.getInstance();
        String fromDate = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, -15);
        String toDate = dateFormat.format(cal.getTime());

        ApiStuff.fromDate = fromDate;
        ApiStuff.toDate = toDate;
        if (ApiStuff.pHData[ApiStuff.pHData.length - 1] != 0.0){
            System.out.println("OKKK");
            return;}
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    URL url = new URL("https://api2.arduino.cc/iot/v2/things/0f9b4f8f-95c8-472d-97b7-3a78cd001d03/properties/af16f42d-5a66-46a5-a0e6-94ae38b1419a/timeseries?desc=false&from=" + toDate+"T00:00:00.02492Z" +"&interval=16000&to="+fromDate+"T00:00:00.02492Z");
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setRequestMethod("GET");

                    httpConn.setRequestProperty("Authorization", "Bearer "+ApiStuff.token);

                    InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                            ? httpConn.getInputStream()
                            : httpConn.getErrorStream();
                    Scanner s = new Scanner(responseStream).useDelimiter("\\A");
                    String response = s.hasNext() ? s.next() : "";


                    String[] ResponseArray = response.split("\"");
                    Double[] data = {0.0};
                    int counter = 0;
                    for (int i = 8; i < ResponseArray.length; i+=6) {
                        String strvalue  = ResponseArray[i].replaceAll("[^\\d.]", "");
                        double value = Double.parseDouble(strvalue);
                        ApiStuff.ppmData[counter] = Double.valueOf(df.format(value));
                        counter += 1;
                    }
                    while (ApiStuff.ppmData.length < 14){
                        ppmData[ppmData.length - 1 ] = 0.0;
                    }
                    for (int i = 0; i < ppmData.length; i++){
                        if (ppmData[i] == null){
                            ppmData[i] = 0.0;
                        }
                    }
                   // pushZerosToStart(ppmData);
                    System.out.println("PPM DATA --> " + Arrays.toString(ppmData));
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        getHistoricTemp();
        getHistoricHumid();
        getHistoricPH();
    }

    public static void getHistoricTemp(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat df = new DecimalFormat("#.##");
        Date date = new Date();

        Calendar cal = Calendar.getInstance();
        String fromDate = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, -15);
        String toDate = dateFormat.format(cal.getTime());

        ApiStuff.fromDate = fromDate;
        ApiStuff.toDate = toDate;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    URL url = new URL("https://api2.arduino.cc/iot/v2/things/0f9b4f8f-95c8-472d-97b7-3a78cd001d03/properties/40d7ee87-63b5-4057-b2ae-dae111cf41c0/timeseries?desc=false&from=" + toDate+"T00:00:00.02492Z" +"&interval=16000&to="+fromDate+"T00:00:00.02492Z");
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setRequestMethod("GET");

                    httpConn.setRequestProperty("Authorization", "Bearer "+ApiStuff.token);

                    InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                            ? httpConn.getInputStream()
                            : httpConn.getErrorStream();
                    Scanner s = new Scanner(responseStream).useDelimiter("\\A");
                    String response = s.hasNext() ? s.next() : "";


                    String[] ResponseArray = response.split("\"");
                    Double[] data = {0.0};
                    int counter = 0;
                    for (int i = 8; i < ResponseArray.length; i+=6) {
                        String strvalue  = ResponseArray[i].replaceAll("[^\\d.]", "");
                        double value = Double.parseDouble(strvalue);
                        ApiStuff.tempData[counter] = Double.valueOf(df.format(value));
                        counter += 1;
                    }
                    while (ApiStuff.tempData.length < 14){
                        tempData[tempData.length - 1 ] = 0.0;
                    }
                    for (int i = 0; i < tempData.length; i++){
                        if (tempData[i] == null){
                            tempData[i] = 0.0;
                        }
                    }
                   // pushZerosToStart(tempData);
                    System.out.println("Temperature Data --> " + Arrays.toString(tempData));
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        thread.start();

    }

    public static void getHistoricHumid(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat df = new DecimalFormat("#.##");
        Date date = new Date();

        Calendar cal = Calendar.getInstance();
        String fromDate = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, -15);
        String toDate = dateFormat.format(cal.getTime());

        ApiStuff.fromDate = fromDate;
        ApiStuff.toDate = toDate;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    URL url = new URL("https://api2.arduino.cc/iot/v2/things/0f9b4f8f-95c8-472d-97b7-3a78cd001d03/properties/9368a655-d7f1-4f6a-904c-93a95808416d/timeseries?desc=false&from=" + toDate+"T00:00:00.02492Z" +"&interval=16000&to="+fromDate+"T00:00:00.02492Z");
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setRequestMethod("GET");

                    httpConn.setRequestProperty("Authorization", "Bearer "+ApiStuff.token);

                    InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                            ? httpConn.getInputStream()
                            : httpConn.getErrorStream();
                    Scanner s = new Scanner(responseStream).useDelimiter("\\A");
                    String response = s.hasNext() ? s.next() : "";


                    String[] ResponseArray = response.split("\"");
                    Double[] data = {0.0};
                    int counter = 0;
                    for (int i = 8; i < ResponseArray.length; i+=6) {
                        String strvalue  = ResponseArray[i].replaceAll("[^\\d.]", "");
                        double value = Double.parseDouble(strvalue);
                        ApiStuff.humidData[counter] = Double.valueOf(df.format(value));
                        counter += 1;
                    }
                    while (ApiStuff.humidData.length < 14){
                        humidData[humidData.length - 1 ] = 0.0;
                    }
                    for (int i = 0; i < humidData.length; i++){
                        if (humidData[i] == null){
                            humidData[i] = 0.0;
                        }
                    }
                   // pushZerosToStart(humidData);
                    System.out.println("Humidity data --> " + Arrays.toString(humidData));
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        thread.start();

    }

    public static void getHistoricPH(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat df = new DecimalFormat("#.##");
        Date date = new Date();

        Calendar cal = Calendar.getInstance();
        String fromDate = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, -15);
        String toDate = dateFormat.format(cal.getTime());

        ApiStuff.fromDate = fromDate;
        ApiStuff.toDate = toDate;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    URL url = new URL("https://api2.arduino.cc/iot/v2/things/0f9b4f8f-95c8-472d-97b7-3a78cd001d03/properties/af16f42d-5a66-46a5-a0e6-94ae38b1419a/timeseries?desc=false&from=" + toDate+"T00:00:00.02492Z" +"&interval=16000&to="+fromDate+"T00:00:00.02492Z");
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setRequestMethod("GET");

                    httpConn.setRequestProperty("Authorization", "Bearer "+ApiStuff.token);

                    InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                            ? httpConn.getInputStream()
                            : httpConn.getErrorStream();
                    Scanner s = new Scanner(responseStream).useDelimiter("\\A");
                    String response = s.hasNext() ? s.next() : "";


                    String[] ResponseArray = response.split("\"");
                    int counter = 0;
                    for (int i = 8; i < ResponseArray.length; i+=6) {
                        String strvalue  = ResponseArray[i].replaceAll("[^\\d.]", "");
                        double value = Double.parseDouble(strvalue);
                        ApiStuff.pHData[counter] = Double.valueOf(df.format(value));
                        counter += 1;
                    }
                    while (ApiStuff.pHData.length < 14){
                        pHData[pHData.length - 1 ] = 0.0;
                    }
                    for (int i = 0; i < pHData.length; i++){
                        if (pHData[i] == null){
                            pHData[i] = 0.0;
                        }
                    }
                   // pushZerosToStart(pHData);
                    System.out.println("PH DATA --> "+ Arrays.toString(pHData));
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        thread.start();

    }

    public static void pushZerosToStart(Double[] arr) {
        // Count the number of zeros in the array
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0.0) {
                count++;
            }
        }

        // Create a new array to store the non-zero elements
        double[] nonZeroElements = new double[arr.length - count];

        // Copy the non-zero elements to the new array
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0.0) {
                nonZeroElements[index++] = arr[i];
            }
        }

        // Copy the non-zero elements back to the original array, starting at the end
        index = arr.length - 1;
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] != 0.0) {
                arr[index--] = nonZeroElements[i];
            }
        }

        // Fill the remaining positions with zeros
        for (int i = 0; i < count; i++) {
            arr[i] = 0.0;
        }
    }
    }




