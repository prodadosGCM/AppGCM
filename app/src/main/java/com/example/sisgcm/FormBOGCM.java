package com.example.sisgcm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.*;

public class FormBOGCM extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_bogcm);
        getSupportActionBar().hide(); //esconder a barra da tela com o nome do programa

        Spinner spinner = findViewById(R.id.spinnercomo);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.itens_spinner,
                android.R.layout.simple_spinner_item

        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String selectedItem = parentView.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Selecionado: " + selectedItem, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Ação a ser executada quando nada é selecionado
            }
        });
    }


    }


