package com.calculator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainFragment extends Fragment {

    private String methodName = "";
    private String number1 = "0";
    private String number2 = "0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("Debug", "starting...");

        View.OnClickListener clickHandlerNumber = view1 -> {
            Button button = view1.findViewById(view1.getId());
            String digit = button.getText().toString();

            TextView textView = getView().findViewById(R.id.textNumber);
            String number = textView.getText().toString();

            String fullNumber = number + digit;
            fullNumber = fullNumber.replaceFirst("^0+(?!$)", ""); //remove leading zeros
            textView.setText(fullNumber);
        };

        View.OnClickListener clickHandlerOperation = view2 -> {
            switch(view2.getId()){
                case R.id.buttonPlus:
                    methodName = "sum";
                    break;
                case R.id.buttonMinus:
                    methodName = "diff";
                    break;
                case R.id.buttonMultiply:
                    methodName = "prod";
                    break;
                case R.id.buttonDivide:
                    methodName = "div";
                    break;
            }

            Button button = view2.findViewById(view2.getId());
            String operator = button.getText().toString();

            TextView textView = getView().findViewById(R.id.textNumber);
            String number = textView.getText().toString();
            number1 = number;
            textView.setText("0");

            TextView textViewCalc = getView().findViewById(R.id.textCalculation);
            textViewCalc.setText(number + " " + " " + operator);
        };

        View.OnClickListener clickHandlerCalculate = view3 -> {
            TextView textView = getView().findViewById(R.id.textNumber);
            number2 = textView.getText().toString();
            int result;
            int num1 = Integer.parseInt(number1);
            int num2 = Integer.parseInt(number2);
            switch(methodName){
                case "diff":
                    result = Operations.diff(num1, num2);
                    break;
                case "prod":
                    result = Operations.prod(num1, num2);
                    break;
                case "div":
                    result = Operations.div(num1, num2);
                    break;
                case "sum":
                    result = Operations.sum(num1, num2);
                    break;
                default:
                    result = num2 != 0 ? num2 : num1;
            }
            Log.d("Debug", String.format("%d", result));

            Locale currentLocale = getResources().getConfiguration().locale;

            number1 = String.format(currentLocale, "%d", result);
            number2 = "0";
            methodName = "";

            TextView textViewCalc = getView().findViewById(R.id.textCalculation);
            String text = String.format(currentLocale, " = %d", result);
            textViewCalc.setText(text);

            String number = String.format(currentLocale, "%d", result);
            textView.setText(number);
        };

        View.OnClickListener clickHandlerReset = view4 -> {
            number1 = "0";
            number2 = "0";
            methodName = "";

            TextView textViewCalc = getView().findViewById(R.id.textCalculation);
            textViewCalc.setText("");

            TextView textView = getView().findViewById(R.id.textNumber);
            textView.setText(number1);
        };

        //set number button click handlers
        int[] ids = {R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
                        R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9};
        for (int id : ids) {
            view.findViewById(id).setOnClickListener(clickHandlerNumber);
        }

        //set operation button click handlers
        int[] opButtonIds = {R.id.buttonPlus, R.id.buttonMinus, R.id.buttonMultiply, R.id.buttonDivide};
        for (int id : opButtonIds) {
            view.findViewById(id).setOnClickListener(clickHandlerOperation);
        }

        //set calc button click handler
        view.findViewById(R.id.buttonCalculate).setOnClickListener(clickHandlerCalculate);

        //set reset button click handler
        view.findViewById(R.id.buttonReset).setOnClickListener(clickHandlerReset);
    }
}