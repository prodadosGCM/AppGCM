package com.example.sisgcm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.*;
import android.text.InputType;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Locale;

import android.widget.TimePicker;


public class FormBOGCM extends AppCompatActivity {

    DatePickerDialog picker; // datepicker
    EditText eText; // datepicker

    TimePickerDialog pickertime;//relogio hora inicial
    EditText editTextTime; //relogio hora inicial
    TimePickerDialog pickertimeHoraFinal;//relogio hora final
    EditText editTextTimehorafinal; //relogio hora final




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_bogcm);
        getSupportActionBar().hide(); //esconder a barra da tela com o nome do programa



// inicio do calendario
        eText=(EditText) findViewById(R.id.edit_data);
        eText.setInputType(InputType.TYPE_NULL);

        eText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                picker = new DatePickerDialog(FormBOGCM.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, (monthOfYear + 1), year);
                                eText.setText(selectedDate);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
//fim do calendario


// inicio do relogio hora inicial
        editTextTime=(EditText) findViewById(R.id.edit_hora_inicio);
        editTextTime.setInputType(InputType.TYPE_NULL);
        editTextTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minute = cldr.get(Calendar.MINUTE);
                // time picker dialog
                pickertime = new TimePickerDialog(FormBOGCM.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                                editTextTime.setText(selectedTime);
                            }
                        },
                        hour,
                        minute,
                        true
                );
                pickertime.show();
            }
        });
//fim do relogio hora inicial

// inicio do relogio hora final
        editTextTimehorafinal=(EditText) findViewById(R.id.edit_hora_final);
        editTextTimehorafinal.setInputType(InputType.TYPE_NULL);
        editTextTimehorafinal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minute = cldr.get(Calendar.MINUTE);
                // time picker dialog
                pickertimeHoraFinal = new TimePickerDialog(FormBOGCM.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                                editTextTimehorafinal.setText(selectedTime);
                            }
                        },
                        hour,
                        minute,
                        true
                );
                pickertimeHoraFinal.show();
            }
        });
//fim do relogio hora final

//inicio da spinner como foi solicitado
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
                // Toast.makeText(getApplicationContext(), "Selecionado: " + selectedItem, Toast.LENGTH_SHORT).show(); //código para exibir item selecionado
// Defina a cor do item selecionado para preto
                if (selectedItemView != null && selectedItemView instanceof TextView) {
                    ((TextView) selectedItemView).setTextColor(Color.BLACK);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Ação a ser executada quando nada é selecionado
            }
        }); //final da spinner como foi solicitado


        //inicio da spinner BAIRRO
        Spinner spinnerBairro = findViewById(R.id.spinnerBairro);
        ArrayAdapter<CharSequence> adapterBairro = ArrayAdapter.createFromResource(
                this,
                R.array.itens_spinnerBairro,
                android.R.layout.simple_spinner_item
        );
        adapterBairro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBairro.setAdapter(adapterBairro);
        spinnerBairro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String selectedItem = parentView.getItemAtPosition(position).toString();
                // Toast.makeText(getApplicationContext(), "Selecionado: " + selectedItem, Toast.LENGTH_SHORT).show(); //código para exibir item selecionado
// Defina a cor do item selecionado para preto
                if (selectedItemView != null && selectedItemView instanceof TextView) {
                    ((TextView) selectedItemView).setTextColor(Color.BLACK);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Ação a ser executada quando nada é selecionado
            }
        }); //final da spinner bairro

        //inicio da spinner qualificacao 1
        Spinner spinnerQualificacao1 = findViewById(R.id.spinner_qualificacao_q1);
        ArrayAdapter<CharSequence> adapterQualificacaoQ1 = ArrayAdapter.createFromResource(
                this,
                R.array.itens_spinner_qualificacao_q1,
                android.R.layout.simple_spinner_item
        );
        adapterQualificacaoQ1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQualificacao1.setAdapter(adapterQualificacaoQ1);
        spinnerQualificacao1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String selectedItem = parentView.getItemAtPosition(position).toString();
                // Toast.makeText(getApplicationContext(), "Selecionado: " + selectedItem, Toast.LENGTH_SHORT).show(); //código para exibir item selecionado
// Defina a cor do item selecionado para preto
                if (selectedItemView != null && selectedItemView instanceof TextView) {
                    ((TextView) selectedItemView).setTextColor(Color.BLACK);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Ação a ser executada quando nada é selecionado
            }
        }); //final da spinner qualificacao 1

        //inicio da spinner qualificacao 2
        Spinner spinnerQualificacao2 = findViewById(R.id.spinner_qualificacao_q2);
        ArrayAdapter<CharSequence> adapterQualificacaoQ2 = ArrayAdapter.createFromResource(
                this,
                R.array.itens_spinner_qualificacao_q2,
                android.R.layout.simple_spinner_item
        );
        adapterQualificacaoQ2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQualificacao2.setAdapter(adapterQualificacaoQ2);
        spinnerQualificacao2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String selectedItem = parentView.getItemAtPosition(position).toString();
                // Toast.makeText(getApplicationContext(), "Selecionado: " + selectedItem, Toast.LENGTH_SHORT).show(); //código para exibir item selecionado
// Defina a cor do item selecionado para preto
                if (selectedItemView != null && selectedItemView instanceof TextView) {
                    ((TextView) selectedItemView).setTextColor(Color.BLACK);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Ação a ser executada quando nada é selecionado
            }
        }); //final da spinner como foi solicitado


    }
}


