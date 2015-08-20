package com.kimery.tipcalculator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.view.View.OnClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import java.text.NumberFormat;


public class TipCalculatorActivity extends Activity{

    //define instance variables for the widgets
    private TextView percentTextView;
    private TextView billAmountEditText;
    private TextView tipTextView;
    private TextView totalTextView;
    private Button applyButton;
    private SeekBar seekBar;

    //global variable that need to be saved
    private float tipPercent =0.15f;
    private String billAmountEditString = "";

    //define the shared preferences object
    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);

        //get references to the widgets
        billAmountEditText = (EditText)findViewById(R.id.billAmountEditText);
        percentTextView = (TextView)findViewById(R.id.percentTextView);
        tipTextView = (TextView)findViewById(R.id.tipTextView);
        totalTextView = (TextView)findViewById(R.id.totalTextView);
        applyButton = (Button)findViewById(R.id.applyButton);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        //set the Listeners
        applyButton.setOnClickListener(clickListener);
        billAmountEditText.setOnEditorActionListener(editListener);
        tipTextView.setOnEditorActionListener(editListener);
        totalTextView.setOnEditorActionListener(editListener);
        percentTextView.setOnEditorActionListener(editListener);
        seekBar.setOnSeekBarChangeListener(seekListener);
        //get shared preferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    protected void onPause() {
       //Save the instance variables
        Editor editor = savedValues.edit();
        editor.putString("billAmountString", billAmountEditString);
        editor.putFloat("tipPercent", tipPercent);
        editor.commit();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Get saved variables
        billAmountEditString = savedValues.getString("billAmountString", "");
        tipPercent = savedValues.getFloat("tipPercent", 0.15f);
        //set the bill amount on the widget
        billAmountEditText.setText(billAmountEditString);
        //call calculate and display
        calculateAndDisplay();
    }

    //Calculate tip and total then display it
    public void calculateAndDisplay() {
        billAmountEditString = billAmountEditText.getText().toString();
        float billAmount;
        if (billAmountEditString.equals(""))
            billAmount = 0;
        else
            billAmount = Float.parseFloat(billAmountEditString);

        //calculate tip and total
        int progress = seekBar.getProgress();
        tipPercent = (float) progress / 100;
        float tipAmount = billAmount * tipPercent;
        float totalAmount = billAmount + tipAmount;

        //display the other result with formatting
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        tipTextView.setText(currency.format(tipAmount));
        totalTextView.setText(currency.format(totalAmount));

    }
    //anonymous listener for OnEditorActionListener
    private OnEditorActionListener editListener = new OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                calculateAndDisplay();
            }
            return false;
        }
    };
    //anonymous listener for OnclickListener
    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.applyButton:
                    calculateAndDisplay();
                    break;
            }

        }
    };
    //anonymous listener for seek bar
    private OnSeekBarChangeListener seekListener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            percentTextView.setText(progress + "%");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


}
