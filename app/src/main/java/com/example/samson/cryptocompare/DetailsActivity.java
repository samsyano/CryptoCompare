package com.example.samson.cryptocompare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * Created by SAMSON on 10/10/2017.
 */

public class DetailsActivity extends AppCompatActivity {

    Spinner toSpinner;
    EditText fromEditText;
    Button make_conversion;
    TextView resultView, fromTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toSpinner = (Spinner) findViewById(R.id.to_currency_spinner);
        fromEditText = (EditText) findViewById(R.id.editText);
        fromTextView = (TextView) findViewById(R.id.from_currency);
        resultView = (TextView) findViewById(R.id.result);

        make_conversion = (Button) findViewById(R.id.button);

        Intent intent = getIntent();
        String btc = intent.getStringExtra("btc");
        String eth = intent.getStringExtra("eth");
        final String currency = intent.getStringExtra("currency");



        fromTextView.setText(currency);

        final Double btc_value = Double.parseDouble(btc);
        final Double eth_value = Double.parseDouble(eth);


        make_conversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected = (String) toSpinner.getSelectedItem();

                Double answer = null;

                String editTValue = fromEditText.getText().toString();
                if (TextUtils.isEmpty(editTValue)) {
                    Toast.makeText(DetailsActivity.this, "Input the value to be converted", Toast.LENGTH_SHORT).show();
                    return;
                }

                Double value = Double.parseDouble(editTValue);

                if (selected.equals("BTC")) {
                    answer = (1 * value) / btc_value;
                } else if (selected.equals("ETH")) {
                    answer = (1 * value / eth_value);
                }

                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String answerString = decimalFormat.format(answer);

                String editDoubleValue = decimalFormat.format(value);

                resultView.setText( currency + " " + editDoubleValue + " = " + answerString + " " + selected);
            }
        });


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, new String[]{"BTC", "ETH"});

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        toSpinner.setAdapter(arrayAdapter);


        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.home:
                Intent upIntent = getParentActivityIntent();
                //This activity is NOT part of this app's task, so create a new task
                //when navigating up, with a synthesized back stack.
                if(NavUtils.shouldUpRecreateTask(this, upIntent)){
                    TaskStackBuilder.create(this)

                            //Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            //Navigate up to the closest parent
                            .startActivities();
                }else {
                    //This activity is part of this app's task, so simply
                    //  navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
