package com.dani.zncalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etZn, etEquation;
    TextView tvSolution;
    private ZnViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewModel = new ZnViewModel();
        etEquation = (EditText) findViewById(R.id.et_equation);
        etZn = (EditText) findViewById(R.id.et_zn);
        tvSolution = (TextView) findViewById(R.id.tv_solution);
        etEquation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void Solve(View v) {
        etEquation.clearFocus();;
        String equation = etEquation.getText().toString();
        if (equation==null||equation==""){
            Toast.makeText(this, "Please enter an equation", Toast.LENGTH_SHORT).show();
            return;
        }
        String zn = etZn.getText().toString();
        int n;
        Log.i("main activity","zn: "+zn);
        if (zn == "" || zn == null||zn.isEmpty()) {
            n = 3;
        } else
            n = Integer.parseInt(etZn.getText().toString());
        if (!Calculater.isPrime(n)){
            Toast.makeText(this, "n can be prime number only", Toast.LENGTH_SHORT).show();
            return;
        }
        mViewModel.setEquation(equation);
        mViewModel.setN(n);
        try {
            mViewModel.apply();
            tvSolution.setText(mViewModel.getSolution());
        } catch (Exception e) {
            Toast.makeText(this, "Equation contains illegal characters", Toast.LENGTH_SHORT).show();
        }
    }
}