package com.example.cmpt496;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class StatsPdf {

    double ph;
    double temp;
    double ppm;
    double humidity;

    public StatsPdf(double ph, double temp, double ppm, double humidity) {
        this.ph = ph;
        this.temp = temp;
        this.ppm = ppm;
        this.humidity = humidity;
    }

    public void ParseApiData(){

    }

    //
    public void graphPlotValues(){


    }



    public  ArrayList<Integer> PhList(int interval){

        return null;
    }

    public ArrayList<Integer> PpmList(int interval){

        return null;
    }


    public ArrayList<Integer> TemperatureList(int interval){

        return null;
    }


    public ArrayList<Integer> HumidityList(int interval){

        return null;
    }




    public String descriptiveStats(ArrayList<Integer> metricList) {

        if (metricList.isEmpty() ){
            throw new IllegalArgumentException("Data not found");
        }


        /*Min starts at the first index since it is sorted  Max is the last index*/
        Collections.sort(metricList);
        int minVal= metricList.get(0);
        int maxVal=metricList.get(metricList.size() - 1);
        double range = maxVal - minVal;


        /*sums all assessed values */
        long combinedValues = 0;
        for (int i = 0; i < metricList.size(); i++) {
            combinedValues = metricList.get(i) + combinedValues;
        }

        /*finds the mean */
        double mean = (double) combinedValues / metricList.size();


        double median;
        /* median case for even */
        if (metricList.size() % 2 == 0) {
            int firstHalf = (metricList.size() / 2);
            int secondHalf = firstHalf - 1;
            median = (double) (metricList.get(firstHalf) + metricList.get(secondHalf)) / 2.0;


            /*median case for odd */
        } else {
            int half = (metricList.size() / 2);
            median =  metricList.get(half);

        }


        return "min = " + minVal + "\n" +
                "max = " + maxVal + "\n" +
                "range =" + range + "\n" +
                "mean = " + mean + "\n" +
                "median = " + median + "\n";


    }









}
