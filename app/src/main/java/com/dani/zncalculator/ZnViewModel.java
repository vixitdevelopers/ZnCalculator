package com.dani.zncalculator;

import android.util.Log;

public class ZnViewModel {


    private String equation;
    private int n;
    private String solution;
    private Zn zn;
    private static final String TAG ="ZnViewModel";
    public void setEquation(String equation) {
        this.equation = equation.toLowerCase();
    }

    public void setN(int n) {
        this.n = n;
    }

    public ZnViewModel() {
    }

    public void apply(){
        this.zn = new Zn(n,equation);
        this. solution = zn.solve();
        Log.i(TAG,"solution: "+solution);
    }

    public String getSolution() {
        return solution;
    }


}
