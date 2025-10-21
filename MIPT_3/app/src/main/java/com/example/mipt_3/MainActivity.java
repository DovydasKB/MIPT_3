package com.example.mipt_3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView textResult;
    private String currentNumber = "";
    private String lastOperator = "";
    private double result = 0;
    private double memory = 0;
    private boolean justCalculated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        textResult = findViewById(R.id.text_result);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setNumberButtonListeners();
        setOperatorButtonListeners();
    }

    private void setNumberButtonListeners() {
        int[] numberButtonIds = {
                R.id.button_zero, R.id.button_one, R.id.button_two, R.id.button_three, R.id.button_four,
                R.id.button_five, R.id.button_six, R.id.button_seven, R.id.button_eight, R.id.button_nine,
                R.id.button_dot
        };

        for (int id : numberButtonIds) {
            findViewById(id).setOnClickListener(this::onNumberClick);
        }
    }

    private void setOperatorButtonListeners() {
        int[] operatorButtonIds = {
                R.id.button_plus, R.id.button_minus, R.id.button_multiply, R.id.button_divide,
                R.id.button_percent, R.id.button_sqrt, R.id.button_inverse, R.id.button_plus_minus,
                R.id.button_c, R.id.button_ce, R.id.button_back, R.id.button_equals,
                R.id.button_mc, R.id.button_mr, R.id.button_ms, R.id.button_mplus, R.id.button_mminus
        };

        for (int id : operatorButtonIds) {
            findViewById(id).setOnClickListener(this::onOperatorClick);
        }
    }
    public void onNumberClick(View view) {
        Button button = (Button) view;
        if (justCalculated) {
            currentNumber = "";
            justCalculated = false;
        }

        String value = button.getText().toString();
        if (value.equals(".") && currentNumber.contains(".")) return;

        currentNumber += value;
        updateResultText();
    }
    public void onOperatorClick(View view) {
        Button button = (Button) view;
        String operator = button.getText().toString();
        int viewId = view.getId();
        if (viewId == R.id.button_c) {
            currentNumber = "";
            result = 0;
            lastOperator = "";
        } else if (viewId == R.id.button_ce) {
            currentNumber = "";
        } else if (viewId == R.id.button_back) {
            if (!currentNumber.isEmpty()) {
                currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
            }
        } else if (viewId == R.id.button_plus_minus) {
            if (!currentNumber.isEmpty()) {
                double number = Double.parseDouble(currentNumber);
                number = -number;
                currentNumber = removeTrailingZero(number);
            }
        } else if (viewId == R.id.button_sqrt) {
            if (!currentNumber.isEmpty()) {
                double number = Double.parseDouble(currentNumber);
                if (number >= 0) {
                    currentNumber = removeTrailingZero(Math.sqrt(number));
                } else {
                    currentNumber = "Error";
                }
            }
        } else if (viewId == R.id.button_inverse) {
            if (!currentNumber.isEmpty()) {
                double number = Double.parseDouble(currentNumber);
                if (number != 0) {
                    currentNumber = removeTrailingZero(1 / number);
                } else {
                    currentNumber = "Error";
                }
            }
        } else if (viewId == R.id.button_mc) {
            memory = 0;
        } else if (viewId == R.id.button_mr) {
            currentNumber = removeTrailingZero(memory);
        } else if (viewId == R.id.button_ms) {
            if (!currentNumber.isEmpty()) {
                memory = Double.parseDouble(currentNumber);
            }
        } else if (viewId == R.id.button_mplus) {
            if (!currentNumber.isEmpty()) {
                memory += Double.parseDouble(currentNumber);
            }
        } else if (viewId == R.id.button_mminus) {
            if (!currentNumber.isEmpty()) {
                memory -= Double.parseDouble(currentNumber);
            }
        } else if (viewId == R.id.button_equals) {
            calculate();
            lastOperator = "";
            justCalculated = true;
        } else {
            if (!currentNumber.isEmpty() || justCalculated) {
                calculate();
                lastOperator = operator;
                currentNumber = "";
                justCalculated = false;
            } else {
                lastOperator = operator;
            }
        }

        updateResultText();
    }

    private void calculate() {
        if (currentNumber.isEmpty()) return;

        double number = Double.parseDouble(currentNumber);

        if (lastOperator.isEmpty()) {
            result = number;
            return;
        }

        switch (lastOperator) {
            case "+":
                result += number;
                break;
            case "-":
                result -= number;
                break;
            case "*":
            case "×":
                result *= number;
                break;
            case "÷":
            case "/":
                if (number != 0) {
                    result /= number;
                } else {
                    currentNumber = "Error";
                    return;
                }
                break;
            case "%":
                result = result * number / 100.0;
                break;
        }

        currentNumber = removeTrailingZero(result);
    }
    private void updateResultText() {
        if (currentNumber.isEmpty()) {
            textResult.setText(removeTrailingZero(result));
        } else {
            textResult.setText(currentNumber);
        }
    }
    private String removeTrailingZero(double value) {
        if (value == (long) value)
            return String.format("%d", (long) value);
        else
            return String.format("%s", value);
    }
}