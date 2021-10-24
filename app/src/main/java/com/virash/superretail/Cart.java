package com.virash.superretail;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Cart extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner spinner1;
    String[] qty={"1","2","3","4","5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
      //  spinner1=findViewById(R.id.spinner1);
      //  spinner1.setOnItemSelectedListener(this);
      //  ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,qty);
        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner1.setAdapter(arrayAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
