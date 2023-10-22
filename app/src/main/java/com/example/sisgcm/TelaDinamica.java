package com.example.sisgcm;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TelaDinamica extends AppCompatActivity {

    private FirebaseFirestore db;
    DatePickerDialog datapicker; // datepicker
    TimePickerDialog pickertimeHoraDinamica;//relogio hora dinamica
    EditText editTextDataDinaminca,editTextTimehoraDinamica, editTextNomeGMDinamica,
            editTextLocalDinamica, editTextEnderecoDinamica, editTextRelatoDinamica, editTextObservacaoDinamica;
    Button btn_enviar_dinamica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_dinamica);

        getSupportActionBar().hide(); //esconder a barra da tela com o nome do programa

        db = FirebaseFirestore.getInstance();


        editTextNomeGMDinamica =findViewById(R.id.edit_nomeGCM_Dinamica);
        editTextLocalDinamica=findViewById(R.id.edit_local_dinamica);
        editTextEnderecoDinamica=findViewById(R.id.edit_endereco_Dinamica);
        editTextRelatoDinamica=findViewById(R.id.edit_Dinamica_Relato);
        editTextObservacaoDinamica=findViewById(R.id.edit_observacao_DINAMICA);

        btn_enviar_dinamica=findViewById(R.id.btn_enviar_Dinamica);
        btn_enviar_dinamica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                salvarDadosDinamica();

            }
        });


// inicio do calendario
        editTextDataDinaminca=(EditText) findViewById(R.id.edit_data_Dinamica);
        editTextDataDinaminca.setInputType(InputType.TYPE_NULL);
        editTextDataDinaminca.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                datapicker = new DatePickerDialog(TelaDinamica.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, (monthOfYear + 1), year);
                                editTextDataDinaminca.setText(selectedDate);
                            }
                        }, year, month, day);
                datapicker.show();
            }
        });
//fim do calendario

// inicio do relogio hora dinamica
        editTextTimehoraDinamica=(EditText) findViewById(R.id.edit_hora_inicio_dinamica);
        editTextTimehoraDinamica.setInputType(InputType.TYPE_NULL);
        editTextTimehoraDinamica.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minute = cldr.get(Calendar.MINUTE);
                // time picker dialog
                pickertimeHoraDinamica = new TimePickerDialog(TelaDinamica.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                                editTextTimehoraDinamica.setText(selectedTime);
                            }
                        },
                        hour,
                        minute,
                        true
                );
                pickertimeHoraDinamica.show();
            }
        });
//fim do relogio hora dinamica

        //inicio da spinner grupamento dinamica
        Spinner spinnerGrupamentoDinamica = findViewById(R.id.spinner_grupamento_dinamica);
        ArrayAdapter<CharSequence> adapterGrupamentoDinamica = ArrayAdapter.createFromResource(
                this,
                R.array.itens_spinner_grupamento,
                android.R.layout.simple_spinner_item
        );
        adapterGrupamentoDinamica.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrupamentoDinamica.setAdapter(adapterGrupamentoDinamica);
        spinnerGrupamentoDinamica.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        }); //final da spinner grupamento dinamica

        //inicio da spinner bairro dinamica
        Spinner spinnerBairroDinamica = findViewById(R.id.spinnerBairroDinamica);
        ArrayAdapter<CharSequence> adapterBairroDinamica = ArrayAdapter.createFromResource(
                this,
                R.array.itens_spinnerBairro,
                android.R.layout.simple_spinner_item
        );
        adapterBairroDinamica.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBairroDinamica.setAdapter(adapterBairroDinamica);
        spinnerBairroDinamica.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        }); //final da spinner bairro dinamica

//inicio da spinner tipo de dinamica
        Spinner spinnerTipoDinamica = findViewById(R.id.spinner_tipo_Dinamica);
        ArrayAdapter<CharSequence> adapterTipooDinamica = ArrayAdapter.createFromResource(
                this,
                R.array.itens_spinner_tipo_Dinamica,
                android.R.layout.simple_spinner_item
        );
        adapterTipooDinamica.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoDinamica.setAdapter(adapterTipooDinamica);
        spinnerTipoDinamica.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        }); //final da spinner tipo de dinamica


    }//final onCreate

    private void salvarDadosDinamica(){

        Long id;
        id = System.currentTimeMillis();//id automatica falta colocar para consultar o banco e não repetir
        String idString = Long.toString(id);// transforma em string para nomear o documento com a mesma numeração da id

        String dataDinamica = editTextDataDinaminca.getText().toString();
        String horaDinamica = editTextTimehoraDinamica.getText().toString();
        String nomeGCMDinamica = editTextNomeGMDinamica.getText().toString();
        String localDinamica = editTextLocalDinamica.getText().toString();
        String enderecoDinamica = editTextEnderecoDinamica.getText().toString();
        String relatoDinamica = editTextRelatoDinamica.getText().toString();
        String observacaoDinamica = editTextObservacaoDinamica.getText().toString();

        //gravar dados das spinnes
        Spinner spinnerGrupamentoDinamica = findViewById(R.id.spinner_grupamento_dinamica);
        String grupamentoDinamicaSelecionado = spinnerGrupamentoDinamica.getSelectedItem().toString();

        Spinner spinnerBairroDinamica = findViewById(R.id.spinnerBairroDinamica);
        String bairroDinamicaSelecionado = spinnerBairroDinamica.getSelectedItem().toString();

        Spinner spinnerTipoDinamica = findViewById(R.id.spinner_tipo_Dinamica);
        String tipoDinamicaSelecionado = spinnerTipoDinamica.getSelectedItem().toString();

        // Crie um objeto para armazenar esses dados
        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put("id", id);

        //salvar edittext
        dataToSave.put("data", dataDinamica);
        dataToSave.put("hora", horaDinamica);
        dataToSave.put("nomeGCMDinamica", nomeGCMDinamica);
        dataToSave.put("local", localDinamica);

        dataToSave.put("endereco", enderecoDinamica);
        dataToSave.put("relatoDinamic", relatoDinamica);
        dataToSave.put("observacao", observacaoDinamica);

        // salvar spinner
        dataToSave.put("Bairro", bairroDinamicaSelecionado);
        dataToSave.put("grupamento", grupamentoDinamicaSelecionado);
        dataToSave.put("tipoDinamica", tipoDinamicaSelecionado);

        //salvar data e hora cadastro
        dataToSave.put("dataHoraCadastro", FieldValue.serverTimestamp());

        System.currentTimeMillis();
        // ...

        DocumentReference documentReference = db.collection("DB_Dinamica").document(idString); // .document(idString) coloca o nr da id no documento
        documentReference.set(dataToSave)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Dados salvos com sucesso", Toast.LENGTH_SHORT).show();

                        // prepara a mensengem para o whatsapp
                        String emojiSirene = "\n🚨 ";
                        String emojiSeta = "\n➡ ";
                        String informationToShare = emojiSirene + "SEDHSEG"+ emojiSirene + "GUARDA CIVIL MUNICIPAL DE CABO FRIO"  + emojiSirene + "Grupamento: "
                                + grupamentoDinamicaSelecionado + emojiSeta + "Data: " + dataDinamica + emojiSeta + "Horário: "
                                + horaDinamica + emojiSeta + "Equipe: " + nomeGCMDinamica + emojiSeta + "Local: " +
                                localDinamica + emojiSeta + "Dinâmica: " + relatoDinamica;
                        //String informationToShare = "Texto do EditText 1: ""\nTexto do EditText 2: " + editText2Text;
                        //adaptar a mensagem para compartilhar

                        // Crie uma intenção para compartilhar via WhatsApp
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, informationToShare);
                        sendIntent.setType("text/plain");
                        sendIntent.setPackage("com.whatsapp"); // Para garantir que o WhatsApp seja usado

                        try {
                            startActivity(sendIntent);
                        } catch (android.content.ActivityNotFoundException e) {
                            // Trate o caso em que o WhatsApp não está instalado
                            // Você pode abrir a Play Store para instalação, por exemplo
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Erro ao salvar dados", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Erro ao salvar dados: " + e.getMessage());
                    }
                });
    }// final da funçAo salvar


    @Override
    public void onBackPressed() {

        // Ou, para retornar à tela anterior (activity anterior), você pode usar o Intent:
        Intent intent = new Intent(this, TelaPrincipal.class); // Substitua TelaAnterior pela classe da sua atividade anterior
        startActivity(intent);
        finish(); // Finaliza a atividade atual
    }

}