package com.calculator;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

public class MainFragment extends Fragment {

    private String number1 = "0";
    private String number2 = "0";
    private int chosenButton;
    private final Map<Integer, BiFunction<Integer, Integer, Integer>> operations =
            new HashMap<Integer, BiFunction<Integer, Integer, Integer>>() {{
        put(R.id.buttonMinus, Operations::diff);
        put(R.id.buttonMultiply, Operations::prod);
        put(R.id.buttonDivide, Operations::div);
        put(R.id.buttonPlus, Operations::sum);
    }};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("Debug", "starting...");

        View.OnClickListener clickHandlerNumber = view1 -> {
            Button button = view1.findViewById(view1.getId());
            String digit = button.getText().toString();

            TextView textView = requireView().findViewById(R.id.textNumber);
            String number = textView.getText().toString();

            String fullNumber = number + digit;
            fullNumber = fullNumber.replaceFirst("^0+(?!$)", ""); //remove leading zeros
            textView.setText(fullNumber);
        };

        View.OnClickListener clickHandlerOperation = view2 -> {
            chosenButton = view2.getId();
            Button button = view2.findViewById(chosenButton);
            String operator = button.getText().toString();

            TextView textView = requireView().findViewById(R.id.textNumber);
            String number = textView.getText().toString();
            number1 = number;
            textView.setText("0");

            TextView textViewCalc = requireView().findViewById(R.id.textCalculation);
            textViewCalc.setText(number + " " + " " + operator);
        };

        View.OnClickListener clickHandlerCalculate = view3 -> {
            TextView textView = requireView().findViewById(R.id.textNumber);
            number2 = textView.getText().toString();

            int num1 = Integer.parseInt(number1);
            int num2 = Integer.parseInt(number2);

            int result = Objects.requireNonNull(operations.get(chosenButton)).apply(num1, num2);

            Log.d("Debug", String.format("%d", result));

            Locale currentLocale = getResources().getConfiguration().locale;

            number1 = String.format(currentLocale, "%d", result);
            number2 = "0";

            TextView textViewCalc = requireView().findViewById(R.id.textCalculation);
            String text = String.format(currentLocale, " = %d", result);
            textViewCalc.setText(text);

            String number = String.format(currentLocale, "%d", result);
            textView.setText(number);
        };

        View.OnClickListener clickHandlerReset = view4 -> {
            number1 = "0";
            number2 = "0";

            TextView textViewCalc = requireView().findViewById(R.id.textCalculation);
            textViewCalc.setText("");

            TextView textView = requireView().findViewById(R.id.textNumber);
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