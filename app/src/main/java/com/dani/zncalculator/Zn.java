package com.dani.zncalculator;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class Zn {

    private int n;
    private int[] set;
    private String left, right;
    private char x;
    private int type;

    public static final int TYPE_REGULAR_EQUATION = 0;
    public static final int TYPE_ONE_SIDE_NO_VAR = 1;
    public static final int TYPE_TWO_SIDE_NO_VAR = 2;
    public static final int TYPE_ONE_SIDE_WITH_VAR = 3;
    public static final String TAG = "Zn class";


    public Zn(int n, String expression) {
        this.n = n;
        this.set = new int[n-1];
        for (int i = 0; i < set.length; i++)
            set[i] = i + 1;
        this.x = varFinder(expression);
        splitByEquals(expression); //defines left , right
        defineType();
        Log.i(TAG,"Consructor\nn="+n+" expression: "+expression+" var: "+x+"\nleft: "+left
                +" right: "+right+ "type: "+type+ "set: "+ Arrays.toString(set));
    }

    private void defineType() {
        boolean oneSide = (right == null || right == "");
        if (x == '?' && oneSide)
            type = TYPE_ONE_SIDE_NO_VAR;
        else if (x == '?' && !oneSide)
            type = TYPE_TWO_SIDE_NO_VAR;
        else if (x != '?' && oneSide)
            type = TYPE_ONE_SIDE_WITH_VAR;
        else if (x != '?' && !oneSide)
            type = TYPE_REGULAR_EQUATION;
    }

    //find the var
    //check if var is connected , and take care of that.
    //return var
    private char varFinder(String expression) {

        for (int i = 0; i < expression.length(); i++) {
            for(  char alpha = 'a'; alpha<='z';alpha++){
                if (expression.charAt(i) == alpha)
                    return alpha;
            }
        }
        return '?';
    }

    //split expression to two by = sign
    private void splitByEquals(String expression) {
        String[] parts = expression.split("=");
        if(parts.length==2) {
            left = parts[0];
            right = parts[1];
        }else if (parts.length==1){
            left = parts[0];
            right = "";
        }else if(parts.length>2){
            left="";
            right="";
        }
    }


    //replace var with each int, and return
    private boolean checkIfEquals(String left, String right) {
        double solutionRight = Calculater.eval(right);
        double solutionLeft = Calculater.eval(left);
        int modRight = (int) (solutionRight % n);
        int modLeft = (int) (solutionLeft % n);
        Log.i(TAG,"\nChecking equality: "+"\nsolution left: "+solutionLeft+ "|mod: "+modLeft+

                "\nsolution right: "+solutionRight+"|mod: "+modRight);
        return (modLeft == modRight);
    }

    private double solveNoEquals(String left) {
        double solution = Calculater.eval(left);
        return (solution % n);
    }

    public String solve() {
        switch (type) {

            case TYPE_REGULAR_EQUATION:
                for (int a : set) {
                    Log.i(TAG,"CHECKING "+a+": left ="+replace(left,a)+" right="+replace(right,a));
                    if (checkIfEquals(replace(left, a), replace(right, a)))
                        return x + "=" + a;
                }
                return "No Solution";
            case TYPE_ONE_SIDE_NO_VAR:
                Double solve = solveNoEquals(left);
                if (solve<0)
                    solve = solveNoEquals(String.valueOf(solve + (-solve*n)));
                if (solve%1==0)//.0
                    return ""+solve.intValue();
                return "" + solveNoEquals(left);
            case TYPE_ONE_SIDE_WITH_VAR:
                return "Can't solve.";
            case TYPE_TWO_SIDE_NO_VAR:
                return "Equation is "+checkIfEquals(left,right);
        }
        return "error 205. can't solve";
    }

    private String replace(String equation, int a) {
        //args
        // int e.g : 4
        // equation e.g: "3x^2+8=X+1
        //returns : "3*4^2+8=4+1
        ArrayList<String>appearancesOfX = getListBySplit(equation);
        String result ="";
        for (String part : appearancesOfX) {

            for (int i = 0; i < part.length() - 1; i++) {
                char current = part.charAt(i);
                char next = part.charAt(i + 1);
                if (current == x)
                   part = part.replace(x, (char) a);
                else if(next==x) {
                    if (StringUtils.isAlphanumeric(String.valueOf(current))) // e.g is 3x
                        part = part.replace("" + x, "*" + a);
                    else
                        part = part.replace(x, (char) a);
                }
            }
            result+=part;
        }
        return result;
    }

    private ArrayList<String> getListBySplit(String equation){
        ArrayList<String>result = new ArrayList<>();
        while (true){
            if(equation==""||equation==null)
                return result;
            else{
                if(equation.contains(String.valueOf(x))){ //e.g 3x+2
                    result.add(StringUtils.substringBefore(equation, String.valueOf(x))+x);
                    equation = StringUtils.substringAfter(equation, String.valueOf(x));
                }
                else{ //e.g +2
                    result.add(equation);
                    equation ="";
                }

            }
        }
    }
}
