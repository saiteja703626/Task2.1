package com.example.appunit;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText inputValue;
    private Spinner sourceUnit;
    private Spinner destUnit;
    private Button convertButton;
    private TextView resultText;

    // Arrays for spinner options
    private final String[] units = {
            "Inch", "Foot", "Yard", "Mile", "Centimeter", "Kilometer",
            "Pound", "Ounce", "Ton", "Kilogram", "Gram",
            "Celsius", "Fahrenheit", "Kelvin"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputValue = findViewById(R.id.inputValue);
        sourceUnit = findViewById(R.id.sourceUnit);
        destUnit = findViewById(R.id.destUnit);
        convertButton = findViewById(R.id.convertButton);
        resultText = findViewById(R.id.resultText);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceUnit.setAdapter(adapter);
        destUnit.setAdapter(adapter);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertUnits();
            }
        });
    }

    private void convertUnits() {
        String inputStr = inputValue.getText().toString();
        if (inputStr.isEmpty()) {
            resultText.setText("Please enter a value");
            return;
        }

        double input = Double.parseDouble(inputStr);
        String source = sourceUnit.getSelectedItem().toString();
        String dest = destUnit.getSelectedItem().toString();
        double result;

        try {
            result = performConversion(input, source, dest);
            resultText.setText(String.format("%.2f %s = %.2f %s", input, source, result, dest));
        } catch (IllegalArgumentException e) {
            resultText.setText("Cannot convert between these units");
        }
    }

    private double performConversion(double value, String source, String dest) {
        double baseValue = toBaseUnit(value, source);

        return fromBaseUnit(baseValue, dest, source);
    }

    private double toBaseUnit(double value, String unit) {
        switch (unit) {

            case "Inch": return value * 2.54;
            case "Foot": return value * 30.48;
            case "Yard": return value * 91.44;
            case "Mile": return value * 160934;
            case "Centimeter": return value;
            case "Kilometer": return value * 100000;


            case "Pound": return value * 0.453592;
            case "Ounce": return value * 0.0283495;
            case "Ton": return value * 907.185;
            case "Kilogram": return value;
            case "Gram": return value * 0.001;

            case "Celsius": return value;
            case "Fahrenheit": return (value - 32) / 1.8;
            case "Kelvin": return value - 273.15;

            default: throw new IllegalArgumentException("Unknown unit");
        }
    }

    private double fromBaseUnit(double value, String dest, String source) {
        boolean sourceIsLength = isLengthUnit(source);
        boolean sourceIsWeight = isWeightUnit(source);
        boolean sourceIsTemp = isTempUnit(source);

        if ((sourceIsLength && !isLengthUnit(dest)) ||
                (sourceIsWeight && !isWeightUnit(dest)) ||
                (sourceIsTemp && !isTempUnit(dest))) {
            throw new IllegalArgumentException("Incompatible units");
        }

        switch (dest) {
            case "Inch": return value / 2.54;
            case "Foot": return value / 30.48;
            case "Yard": return value / 91.44;
            case "Mile": return value / 160934;
            case "Centimeter": return value;
            case "Kilometer": return value / 100000;

            case "Pound": return value / 0.453592;
            case "Ounce": return value / 0.0283495;
            case "Ton": return value / 907.185;
            case "Kilogram": return value;
            case "Gram": return value * 1000;

            case "Celsius": return value;
            case "Fahrenheit": return (value * 1.8) + 32;
            case "Kelvin": return value + 273.15;

            default: throw new IllegalArgumentException("Unknown unit");
        }
    }

    private boolean isLengthUnit(String unit) {
        return unit.equals("Inch") || unit.equals("Foot") || unit.equals("Yard") ||
                unit.equals("Mile") || unit.equals("Centimeter") || unit.equals("Kilometer");
    }

    private boolean isWeightUnit(String unit) {
        return unit.equals("Pound") || unit.equals("Ounce") || unit.equals("Ton") ||
                unit.equals("Kilogram") || unit.equals("Gram");
    }

    private boolean isTempUnit(String unit) {
        return unit.equals("Celsius") || unit.equals("Fahrenheit") || unit.equals("Kelvin");
    }
}