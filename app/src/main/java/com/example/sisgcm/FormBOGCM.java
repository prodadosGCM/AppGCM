package com.example.sisgcm;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;
import android.widget.*;
import android.text.InputType;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.Bidi;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;

import android.widget.TimePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;


public class FormBOGCM extends AppCompatActivity {

    private Button btn_ComOcorrencia; //botão salvar

    String usuarioId;
    private FirebaseFirestore db;

    DatePickerDialog picker; // datepicker
    EditText eText; // datepicker

    TimePickerDialog pickertime;//relogio hora inicial
    EditText editTextTime; //relogio hora inicial
    TimePickerDialog pickertimeHoraFinal;//relogio hora final
    EditText editTextTimehorafinal; //relogio hora final

    EditText editTextEndereco, editTextNrdoEndereco, editTextComplementoEndereco,
            editTextPontoReferencia,editTextRelatoObservacao, editTextMaterialRecolApreendido,
            editTextNomeQ1,editTextNomeQ2,editTextCpfQ1, editTextCpfq2, editTextTelefoneQ1,
            editTextTelefoneQ2,editTextHoraChegadaOutroOrgao, editTextHoraSaidaOutroOrgao,
            editTextNrRegistroOutroOrgao,editTextNomeGCMCondutorOcorrencia, editTextNomeGCMApoioOcorrencia;

    TimePickerDialog pickertimeHoraChegadaOutroOrgao, pickertimeHoraSaidaOutroOrgao;//relogio hora chegada outro orgao


    //AUTO COMPLETE TIPO OCORRENCIA
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> autoCompleteAdapter;
    String[] sugestoes = {"T-ACIDENTE DE TRÂNSITO (COM VÍTIMA)", "T-ACIDENTE DE TRÂNSITO (SEM VÍTIMA)",
            "R-COBRANÇA IRREGULAR DE ESTACIONAMENTO (FLANELAGEM)", "R-DESACATO AO SERVIDOR PÚBLICO","R-FURTO",
    "R-RECUPERAÇÃO DE VEÍCULO ROUBADO", "R-RELACIONADO À POPULAÇÃO DE RUA(ESPECIFICAR NA OBSERVAÇÃO)","M-CRIME AMBIENTAL (ESPECIFICAR NA OSERVAÇÃO)",
    "M-MAUS TRATOS A ANIMAIS","V-VIOLÊNCIA DOMÉSTICA (LEI MARIA DA PENHA)", "OUTROS (ESPECIFICAR NA OBSERVAÇÃO)"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_bogcm);
        getSupportActionBar().hide(); //esconder a barra da tela com o nome do programa

        db = FirebaseFirestore.getInstance();

        //inicializar os edittext
        editTextEndereco=findViewById(R.id.edit_endereco);
        editTextNrdoEndereco=findViewById(R.id.edit_nr_endereco);
        editTextComplementoEndereco=findViewById(R.id.edit_complemento);
        editTextPontoReferencia=findViewById(R.id.edit_PontoReferencia);
        editTextRelatoObservacao=findViewById(R.id.edit_observacao);
        editTextMaterialRecolApreendido=findViewById(R.id.edit_MaterialApreendido);
        editTextNomeQ1=findViewById(R.id.edit_nome_Q1);
        editTextNomeQ2=findViewById(R.id.edit_nome_Q2);
        editTextCpfQ1=findViewById(R.id.edit_CPF_Q1);
        editTextCpfq2=findViewById(R.id.edit_CPF_Q2);
        editTextTelefoneQ1=findViewById(R.id.edit_telefone_Q1);
        editTextTelefoneQ2=findViewById(R.id.edit_telefone_Q2);
        editTextNrRegistroOutroOrgao=findViewById(R.id.edit_nr_registro_EncaminhamentoOrgao);
        editTextNomeGCMCondutorOcorrencia=findViewById(R.id.edit_nomeGCM);
        editTextNomeGCMApoioOcorrencia=findViewById(R.id.edit_nomeGCM_EquipeAPoio);

        btn_ComOcorrencia=findViewById(R.id.btn_ComOcorrencia);
        btn_ComOcorrencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            salvarDadosBOGCM();

            }
        });




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

        // inicio do relogio hora chegada outro orgao
        editTextHoraChegadaOutroOrgao=(EditText) findViewById(R.id.edit_hora_chegada_encaminhamentoorgao);
        editTextHoraChegadaOutroOrgao.setInputType(InputType.TYPE_NULL);
        editTextHoraChegadaOutroOrgao.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minute = cldr.get(Calendar.MINUTE);
                // time picker dialog
                pickertimeHoraChegadaOutroOrgao = new TimePickerDialog(FormBOGCM.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                                editTextHoraChegadaOutroOrgao.setText(selectedTime);
                            }
                        },
                        hour,
                        minute,
                        true
                );
                pickertimeHoraChegadaOutroOrgao.show();
            }
        });
//fim do relogio hora chegada outro orgao


        // inicio do relogio hora saida outro orgao
        editTextHoraSaidaOutroOrgao=(EditText) findViewById(R.id.edit_hora_saida_encaminhamentoorgao);
        editTextHoraSaidaOutroOrgao.setInputType(InputType.TYPE_NULL);
        editTextHoraSaidaOutroOrgao.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minute = cldr.get(Calendar.MINUTE);
                // time picker dialog
                pickertimeHoraSaidaOutroOrgao = new TimePickerDialog(FormBOGCM.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                                editTextHoraSaidaOutroOrgao.setText(selectedTime);
                            }
                        },
                        hour,
                        minute,
                        true
                );
                pickertimeHoraSaidaOutroOrgao.show();
            }
        });
//fim do relogio hora saida outro orgao

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
        }); //final da spinner qualificacao 2

        //inicio da spinner grupamento
        Spinner spinnerGrupamento = findViewById(R.id.spinner_grupamento);
        ArrayAdapter<CharSequence> adapterGrupamento = ArrayAdapter.createFromResource(
                this,
                R.array.itens_spinner_grupamento,
                android.R.layout.simple_spinner_item
        );
        adapterGrupamento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrupamento.setAdapter(adapterGrupamento);
        spinnerGrupamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        }); //final da spinner grupamento


        //inicio da spinner outro orgao
        Spinner spinnerOutroOrgao = findViewById(R.id.spinner_encaminhamento_orgao);
        ArrayAdapter<CharSequence> adapterOutroOrgao = ArrayAdapter.createFromResource(
                this,
                R.array.itens_spinner_encaminhamento_orgao,
                android.R.layout.simple_spinner_item
        );
        adapterOutroOrgao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOutroOrgao.setAdapter(adapterOutroOrgao);
        spinnerOutroOrgao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        }); //final da spinner outro orgao


        //inicio da spinner grupamento apoio
        Spinner spinnerGrupamentoApoio = findViewById(R.id.spinner_grupamentoEquipeApoio);
        ArrayAdapter<CharSequence> adapterGrupamentoApoio = ArrayAdapter.createFromResource(
                this,
                R.array.itens_spinner_grupamento,
                android.R.layout.simple_spinner_item
        );
        adapterGrupamentoApoio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrupamentoApoio.setAdapter(adapterGrupamentoApoio);
        spinnerGrupamentoApoio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        }); //final da spinner grupamento apoio




// INICIO DO AUTO COMPLETE TIPO OCORRENCIA
        // Inicialize o AutoCompleteTextView
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        // Inicialize o ArrayAdapter com seus dados de sugestão
        autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, sugestoes);

        // Configure o AutoCompleteTextView com o ArrayAdapter
        autoCompleteTextView.setAdapter(autoCompleteAdapter);

        // Adicione um TextWatcher para verificar se o texto digitado corresponde a uma sugestão
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String textoDigitado = autoCompleteTextView.getText().toString();
                if (!isSugestaoValida(textoDigitado)) {
                    autoCompleteTextView.setError("Selecione uma sugestão válida");
                    Toast.makeText(getApplicationContext(), "Entrada inválida", Toast.LENGTH_SHORT).show();
                    autoCompleteTextView.requestFocus(); // Mantenha o foco no campo de texto

                } else {
                    autoCompleteTextView.setError(null); // Limpa o erro
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        //FINAL DO AUTO COMPLETE TIPO OCORRENCIA




    }//FINAL ON CREATE

    //função salvar dados

    private void salvarDadosBOGCM(){

        Long id;
        id = System.currentTimeMillis();//id automatica falta colocar para consultar o banco e não repetir
        String idString = Long.toString(id);// transforma em string para nomear o documento com a mesma numeração da id

        String dataBogcm = eText.getText().toString();
        String horaInicio = editTextTime.getText().toString();
        String horaFinal = editTextTimehorafinal.getText().toString();
        String horaChegadaOutroOrgao = editTextHoraChegadaOutroOrgao.getText().toString();
        String horaSaidaOutroOrgao = editTextHoraSaidaOutroOrgao.getText().toString();
        String endereco = editTextEndereco.getText().toString();
        String nrEndereco = editTextNrdoEndereco.getText().toString();
        String complementoEnd = editTextComplementoEndereco.getText().toString();
        String pontoReferencia = editTextPontoReferencia.getText().toString();
        String relatoObservacao = editTextRelatoObservacao.getText().toString();
        String materialRecApreendido = editTextMaterialRecolApreendido.getText().toString();
        String nomeQ1 = editTextNomeQ1.getText().toString();
        String nomeq2 = editTextNomeQ2.getText().toString();
        String cpfQ1 = editTextCpfQ1.getText().toString();
        String cpfQ2 = editTextCpfq2.getText().toString();
        String telefoneQ1 = editTextTelefoneQ1.getText().toString();
        String telefoneq2 = editTextTelefoneQ2.getText().toString();
        String nrRegistroOutroOrgao = editTextNrRegistroOutroOrgao.getText().toString();
        String nomeGCMCondutorOcorrencia = editTextNomeGCMCondutorOcorrencia.getText().toString();
        String nomeGCMApoioOcorrencia = editTextNomeGCMApoioOcorrencia.getText().toString();

        //gravar dados autocomplete
        String tipoOcorrencia = autoCompleteTextView.getText().toString();

        //gravar dados das spinnes
        Spinner spinner = findViewById(R.id.spinnercomo);
        String comoFoiSolicitadoSelecionado = spinner.getSelectedItem().toString();

        Spinner spinnerBairro = findViewById(R.id.spinnerBairro);
        String bairroSelecionado = spinnerBairro.getSelectedItem().toString();

        Spinner spinnerGrupamento = findViewById(R.id.spinner_grupamento);
        String grupamentoSelecionado = spinnerGrupamento.getSelectedItem().toString();

        Spinner spinnerQualificacao1 = findViewById(R.id.spinner_qualificacao_q1);
        String qualificacao1Selecionado = spinnerQualificacao1.getSelectedItem().toString();

        Spinner spinnerQualificacao2 = findViewById(R.id.spinner_qualificacao_q2);
        String qualificacao2Selecionado = spinnerQualificacao2.getSelectedItem().toString();

        Spinner spinnerOutroOrgao = findViewById(R.id.spinner_encaminhamento_orgao);
        String outroOrgaoSelecionado = spinnerOutroOrgao.getSelectedItem().toString();

        Spinner spinnerGrupamentoApoio = findViewById(R.id.spinner_grupamentoEquipeApoio);
        String grupamentoApoioSelecionado = spinnerGrupamentoApoio.getSelectedItem().toString();


        // Crie um objeto para armazenar esses dados
        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put("id", id);

        //salvar edittext
        dataToSave.put("data", dataBogcm);
        dataToSave.put("horaInicio", horaInicio);
        dataToSave.put("horaFinal", horaFinal);
        dataToSave.put("TipoOcorrencia", tipoOcorrencia);

        dataToSave.put("horaChegadaOutroOrgao", horaChegadaOutroOrgao);
        dataToSave.put("horaSaidaOutroOrgao", horaSaidaOutroOrgao);
        dataToSave.put("endereco", endereco);
        dataToSave.put("nrEndereco", nrEndereco);
        dataToSave.put("complementoendereco", complementoEnd);
        dataToSave.put("pontoReferencia", pontoReferencia);
        dataToSave.put("RelatoObsevacao", relatoObservacao);
        dataToSave.put("materialRecolhidoApreendido", materialRecApreendido);
        dataToSave.put("nomeQ1", nomeQ1);
        dataToSave.put("nomeQ2", nomeq2);
        dataToSave.put("cpfQ1", cpfQ1);
        dataToSave.put("cpfQ2", cpfQ2);
        dataToSave.put("telefoneQ1", telefoneQ1);
        dataToSave.put("TelefoneQ2", telefoneq2);
        dataToSave.put("nrRegistroOutroOrgao", nrRegistroOutroOrgao);
        dataToSave.put("nomeGcmCondutorOcorrencia", nomeGCMCondutorOcorrencia);
        dataToSave.put("nomeGcmApoioOcorrencia", nomeGCMApoioOcorrencia);

        // salvar spinner
        dataToSave.put("Bairro", bairroSelecionado);
        dataToSave.put("comoFoisolicitado", comoFoiSolicitadoSelecionado);
        dataToSave.put("GrupamentoCondutorOcorrencia", grupamentoSelecionado);
        dataToSave.put("qualificacaoQ1",qualificacao1Selecionado);
        dataToSave.put("qualificacaoQ2", qualificacao2Selecionado);
        dataToSave.put("encaminhamentoOutroOrgao", outroOrgaoSelecionado);
        dataToSave.put("grupamentoApoio", grupamentoApoioSelecionado);



        //salvar data e hora cadastro
        dataToSave.put("dataHoraCadastro", FieldValue.serverTimestamp());



        System.currentTimeMillis();

        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // ...

        DocumentReference documentReference = db.collection("DB_BOGCM").document(idString); // .document(idString) coloca o nr da id no documento
        documentReference.set(dataToSave)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Dados salvos com sucesso", Toast.LENGTH_SHORT).show();
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



    //FUNÇÃO DO AUTO COMPLETE TIPO OCORRENCIA
    private boolean isSugestaoValida(String texto) {
        for (String sugestao : sugestoes) {
            if (sugestao.equalsIgnoreCase(texto)) {
                return true;
            }
        }
        return false;
    }


}//FINAL PUBLIC CLASS







