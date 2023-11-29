package com.example.sisgcm;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.*;
import android.text.InputType;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;

import android.widget.TimePicker;
import android.content.SharedPreferences;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import org.checkerframework.checker.nullness.qual.NonNull;


public class FormBOGCM extends AppCompatActivity {

    private ProgressBar progressBarBOGCM;
    private Button btn_ComOcorrencia; //botão salvar

    String idString;
    private String imageUrl;  // Adicione esta variável global
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


    private SharedPreferences sharedPreferences;
    private Uri mSelectedUri;
    private ImageView mImgPhoto;

    private String urlImagem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_bogcm);
        getSupportActionBar().hide(); //esconder a barra da tela com o nome do programa

        mImgPhoto=findViewById(R.id.imagem_foto_1);




        FloatingActionButton fab = findViewById(R.id.botao_flutuante_add_foto);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cria um objeto PopupMenu, passando o contexto e a view ancorada
                PopupMenu popupMenu = new PopupMenu(FormBOGCM.this, view);

                // Infla o menu a partir do arquivo XML
                popupMenu.getMenuInflater().inflate(R.menu.add_foto, popupMenu.getMenu());

                // Define um ouvinte de clique para lidar com eventos de item de menu
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Lidar com o clique do item de menu aqui
                        switch (menuItem.getItemId()) {
                            case R.id.obter_foto_dispositivo:
                                obterImagemGaleria();

                            return true;
                            case R.id.tirar_foto:
                                // Código para a Opção 2
                                Toast.makeText(getApplicationContext(), "tirar fotos", Toast.LENGTH_SHORT).show();

                                return true;
                            // Adicione mais casos conforme necessário
                            default:
                                return false;
                        }
                    }
                });

                // Exibe o menu popup
                popupMenu.show();
            }
        });



        progressBarBOGCM=findViewById(R.id.progressbarBOGCM);


        db = FirebaseFirestore.getInstance();

        //inicializar os edittext


// inicio persistencia de dados edittext
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);


        // inicio persistencia de dados endereco

        editTextEndereco = findViewById(R.id.edit_endereco);

// Recuperar o valor salvo, se existir
        String endereco = sharedPreferences.getString("endereco", "");
        editTextEndereco.setText(endereco);

        editTextEndereco.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Salvar o valor digitado nas SharedPreferences
                sharedPreferences.edit().putString("endereco", charSequence.toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // fim persistencia de dados endereco

        // inicio persistencia de dados nr endereco
        editTextNrdoEndereco = findViewById(R.id.edit_nr_endereco);

// Recuperar o valor salvo, se existir
        String nr_endereco = sharedPreferences.getString("nr_endereco", "");
        editTextNrdoEndereco.setText(nr_endereco);

        editTextNrdoEndereco.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Salvar o valor digitado nas SharedPreferences
                sharedPreferences.edit().putString("nr_endereco", charSequence.toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // fim persistencia de dados nr endereco


// inicio persistencia de dados complemento
        editTextComplementoEndereco = findViewById(R.id.edit_complemento);

// Recuperar o valor salvo, se existir
        String complemento = sharedPreferences.getString("complemento", "");
        editTextComplementoEndereco.setText(complemento);

        editTextComplementoEndereco.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Salvar o valor digitado nas SharedPreferences
                sharedPreferences.edit().putString("complemento", charSequence.toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // fim persistencia de dados complemento

        // inicio persistencia de dados ponto_referencia

        editTextPontoReferencia = findViewById(R.id.edit_PontoReferencia);

// Recuperar o valor salvo, se existir
        String ponto_referencia = sharedPreferences.getString("ponto_referencia", "");
        editTextPontoReferencia.setText(ponto_referencia);

        editTextPontoReferencia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Salvar o valor digitado nas SharedPreferences
                sharedPreferences.edit().putString("ponto_referencia", charSequence.toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // fim persistencia de dados ponto_referencia

        // inicio persistencia de dados relato/observacao

        editTextRelatoObservacao = findViewById(R.id.edit_observacao);

// Recuperar o valor salvo, se existir
        String relato_observacao = sharedPreferences.getString("relato_observacao", "");
        editTextRelatoObservacao.setText(relato_observacao);

        editTextRelatoObservacao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Salvar o valor digitado nas SharedPreferences
                sharedPreferences.edit().putString("relato_observacao", charSequence.toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // fim persistencia de dados relato_observacao

// inicio persistencia de dados material_apreendido

        editTextMaterialRecolApreendido = findViewById(R.id.edit_MaterialApreendido);

// Recuperar o valor salvo, se existir
        String material_apreendido = sharedPreferences.getString("material_apreendido", "");
        editTextMaterialRecolApreendido.setText(material_apreendido);

        editTextMaterialRecolApreendido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Salvar o valor digitado nas SharedPreferences
                sharedPreferences.edit().putString("material_apreendido", charSequence.toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // fim persistencia de dados material_apreendido

        // inicio persistencia de dados nome q1

        editTextNomeQ1 = findViewById(R.id.edit_nome_Q1);

// Recuperar o valor salvo, se existir
        String nomeq1 = sharedPreferences.getString("nomeq1", "");
        editTextNomeQ1.setText(nomeq1);

        editTextNomeQ1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Salvar o valor digitado nas SharedPreferences
                sharedPreferences.edit().putString("nomeq1", charSequence.toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // fim persistencia de dados nome q1


// inicio persistencia de dados cpf q1

        editTextCpfQ1 = findViewById(R.id.edit_CPF_Q1);

// Recuperar o valor salvo, se existir
        String cpfq1 = sharedPreferences.getString("cpfq1", "");
        editTextCpfQ1.setText(cpfq1);

        editTextCpfQ1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Salvar o valor digitado nas SharedPreferences
                sharedPreferences.edit().putString("cpfq1", charSequence.toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // fim persistencia de dados cpfq1 q1


        // inicio persistencia de dados telefone q1

        editTextTelefoneQ1 = findViewById(R.id.edit_telefone_Q1);

// Recuperar o valor salvo, se existir
        String telefoneq1 = sharedPreferences.getString("telefoneq1", "");
        editTextTelefoneQ1.setText(telefoneq1);

        editTextTelefoneQ1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Salvar o valor digitado nas SharedPreferences
                sharedPreferences.edit().putString("telefoneq1", charSequence.toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // fim persistencia de dados telefone q1

        // inicio persistencia de dados nome q2

        editTextNomeQ2 = findViewById(R.id.edit_nome_Q2);

// Recuperar o valor salvo, se existir
        String nomeq2 = sharedPreferences.getString("nomeq2", "");
        editTextNomeQ2.setText(nomeq2);

        editTextNomeQ2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Salvar o valor digitado nas SharedPreferences
                sharedPreferences.edit().putString("nomeq2", charSequence.toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // fim persistencia de dados nomeq2

        // inicio persistencia de dados cpf q2

        editTextCpfq2 = findViewById(R.id.edit_CPF_Q2);

// Recuperar o valor salvo, se existir
        String cpfq2 = sharedPreferences.getString("cpfq2", "");
        editTextCpfq2.setText(cpfq2);

        editTextCpfq2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Salvar o valor digitado nas SharedPreferences
                sharedPreferences.edit().putString("cpfq2", charSequence.toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // fim persistencia de dados cpfq2


        // inicio persistencia de dados telefone q2

        editTextTelefoneQ2 = findViewById(R.id.edit_telefone_Q2);

// Recuperar o valor salvo, se existir
        String telefoneq2 = sharedPreferences.getString("telefoneq2", "");
        editTextTelefoneQ2.setText(telefoneq2);

        editTextTelefoneQ2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Salvar o valor digitado nas SharedPreferences
                sharedPreferences.edit().putString("telefoneq2", charSequence.toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // fim persistencia de dados telefoneq2

        // inicio persistencia de dados nr_registroOutroorgao

        editTextNrRegistroOutroOrgao = findViewById(R.id.edit_nr_registro_EncaminhamentoOrgao);

// Recuperar o valor salvo, se existir
        String nr_registroOutroorgao = sharedPreferences.getString("nr_registroOutroorgao", "");
        editTextNrRegistroOutroOrgao.setText(nr_registroOutroorgao);

        editTextNrRegistroOutroOrgao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Salvar o valor digitado nas SharedPreferences
                sharedPreferences.edit().putString("nr_registroOutroorgao", charSequence.toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // fim persistencia de dados nr_registroOutroorgao

        // inicio persistencia de dados nome_gcmcondutor

        editTextNomeGCMCondutorOcorrencia = findViewById(R.id.edit_nomeGCM);

// Recuperar o valor salvo, se existir
        String nome_gcmcondutor = sharedPreferences.getString("nome_gcmcondutor", "");
        editTextNomeGCMCondutorOcorrencia.setText(nome_gcmcondutor);

        editTextNomeGCMCondutorOcorrencia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Salvar o valor digitado nas SharedPreferences
                sharedPreferences.edit().putString("nome_gcmcondutor", charSequence.toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // fim persistencia de dados nome_gcmcondutor

        // inicio persistencia de dados nome_gcmapoio

        editTextNomeGCMApoioOcorrencia = findViewById(R.id.edit_nomeGCM_EquipeAPoio);

// Recuperar o valor salvo, se existir
        String nome_gcmapoio = sharedPreferences.getString("nome_gcmapoio", "");
        editTextNomeGCMApoioOcorrencia.setText(nome_gcmapoio);

        editTextNomeGCMApoioOcorrencia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Salvar o valor digitado nas SharedPreferences
                sharedPreferences.edit().putString("nome_gcmapoio", charSequence.toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // fim persistencia de dados nome_gcmapoio





        //fim da persistencia de dados edittext


        btn_ComOcorrencia=findViewById(R.id.btn_ComOcorrencia);
        btn_ComOcorrencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Desabilitar o botão durante o processo
                progressBarBOGCM.setVisibility(View.VISIBLE);
                btn_ComOcorrencia.setVisibility(View.INVISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (validarCampos()) {

                            exemploChamadaFuncoes(mSelectedUri);
                            //chamar todas funcoes

                        } else {
                            Toast.makeText(getApplicationContext(), "Preencha os campos obrigatórios!", Toast.LENGTH_SHORT).show();

                            // Se os campos não forem preenchidos, sinalize-os com asterisco
                            sinalizarCamposObrigatorios();
                            progressBarBOGCM.setVisibility(View.INVISIBLE);
                            btn_ComOcorrencia.setVisibility(View.VISIBLE);
                        }
                    }
                }, 3000); // 3000 milissegundos (3 segundos) de atraso
            }
        });




// inicio do calendario
        eText=(EditText) findViewById(R.id.edit_data);
        eText.setInputType(InputType.TYPE_NULL);

        // Recuperar a data selecionada, se existir
        String selectedDate = sharedPreferences.getString("selectedDate", "");

        if (!selectedDate.isEmpty()) {
            eText.setText(selectedDate);
        }


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
                                // Salvar a data selecionada nas SharedPreferences
                                sharedPreferences.edit().putString("selectedDate", selectedDate).apply();
                            }
                        }, year, month, day);
                picker.show();
            }
        });
//fim do calendario


// inicio do relogio hora inicial
        editTextTime=(EditText) findViewById(R.id.edit_hora_inicio);
        editTextTime.setInputType(InputType.TYPE_NULL);
        // Recuperar o valor salvo, se existir
        String horaInicio = sharedPreferences.getString("hora_inicio", "");

        if (!horaInicio.isEmpty()) {
            editTextTime.setText(horaInicio);
        }
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
                                // Salvar o horário inicial nas SharedPreferences
                                sharedPreferences.edit().putString("hora_inicio", selectedTime).apply();

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
// Recuperar o valor salvo, se existir
        String horaFinal = sharedPreferences.getString("hora_final", "");

        if (!horaFinal.isEmpty()) {
            editTextTimehorafinal.setText(horaFinal);
        }

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
// Salvar o horário final nas SharedPreferences
                                sharedPreferences.edit().putString("hora_final", selectedTime).apply();

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
// Recuperar o valor salvo, se existir
        String horaChegadaOutroOrgao = sharedPreferences.getString("hora_chegada_outro_orgao", "");

        if (!horaChegadaOutroOrgao.isEmpty()) {
            editTextHoraChegadaOutroOrgao.setText(horaChegadaOutroOrgao);
        }

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
                                // Salvar a hora de chegada em outro órgão nas SharedPreferences
                                sharedPreferences.edit().putString("hora_chegada_outro_orgao", selectedTime).apply();

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

        // Recuperar o valor salvo, se existir
        String horaSaidaOutroOrgao = sharedPreferences.getString("hora_saida_outro_orgao", "");

        if (!horaSaidaOutroOrgao.isEmpty()) {
            editTextHoraSaidaOutroOrgao.setText(horaSaidaOutroOrgao);
        }
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
                                // Salvar a hora de saída em outro órgão nas SharedPreferences
                                sharedPreferences.edit().putString("hora_saida_outro_orgao", selectedTime).apply();

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

// Recuperar o valor selecionado, se existir
        int selectedPosition = sharedPreferences.getInt("spinnerComoPosition", 0); // 0 é o valor padrão se não houver nada salvo

        spinner.setSelection(selectedPosition);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = parentView.getItemAtPosition(position).toString();
                // Toast.makeText(getApplicationContext(), "Selecionado: " + selectedItem, Toast.LENGTH_SHORT).show(); //código para exibir item selecionado
                // Defina a cor do item selecionado para preto

                if (selectedItemView != null && selectedItemView instanceof TextView) {
                    ((TextView) selectedItemView).setTextColor(Color.BLACK);

                    // Salvar a posição da seleção na SharedPreferences
                    sharedPreferences.edit().putInt("spinnerComoPosition", position).apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Ação a ser executada quando nada é selecionado
            }
        }); //final da spinner como foi solicitado
        //final da spinner como foi solicitado









        //inicio da spinner BAIRRO
        Spinner spinnerBairro = findViewById(R.id.spinnerBairro);
        ArrayAdapter<CharSequence> adapterBairro = ArrayAdapter.createFromResource(
                this,
                R.array.itens_spinnerBairro,
                android.R.layout.simple_spinner_item
        );
        adapterBairro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBairro.setAdapter(adapterBairro);

        // Recuperar o valor selecionado, se existir
        int selectedPositionBairro = sharedPreferences.getInt("spinnerBairroPosition", 0); // 0 é o valor padrão se não houver nada salvo

        spinnerBairro.setSelection(selectedPositionBairro);

        spinnerBairro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String selectedItem = parentView.getItemAtPosition(position).toString();
                // Toast.makeText(getApplicationContext(), "Selecionado: " + selectedItem, Toast.LENGTH_SHORT).show(); //código para exibir item selecionado
// Defina a cor do item selecionado para preto
                if (selectedItemView != null && selectedItemView instanceof TextView) {
                    ((TextView) selectedItemView).setTextColor(Color.BLACK);
                    // Salvar a posição da seleção na SharedPreferences
                    sharedPreferences.edit().putInt("spinnerBairroPosition", position).apply();

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Ação a ser executada quando nada é selecionado
            }
        }); //final da spinner bairro


        //inicio da spinner grupamento
        Spinner spinnerGrupamento = findViewById(R.id.spinner_grupamento);
        ArrayAdapter<CharSequence> adapterGrupamento = ArrayAdapter.createFromResource(
                this,
                R.array.itens_spinner_grupamento,
                android.R.layout.simple_spinner_item
        );
        adapterGrupamento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrupamento.setAdapter(adapterGrupamento);

        // Recuperar a posição salva, se existir
        int selectedPositionGrupamento = sharedPreferences.getInt("spinnerGrupamentoPosition", 0); // 0 é o valor padrão se não houver nada salvo

        spinnerGrupamento.setSelection(selectedPositionGrupamento);
        spinnerGrupamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String selectedItem = parentView.getItemAtPosition(position).toString();
                // Toast.makeText(getApplicationContext(), "Selecionado: " + selectedItem, Toast.LENGTH_SHORT).show(); //código para exibir item selecionado
// Defina a cor do item selecionado para preto
                if (selectedItemView != null && selectedItemView instanceof TextView) {
                    ((TextView) selectedItemView).setTextColor(Color.BLACK);
                    // Salvar a posição da seleção na SharedPreferences
                    sharedPreferences.edit().putInt("spinnerGrupamentoPosition", position).apply();

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

        // Recuperar a posição salva, se existir
        int selectedPositionOutroOrgao = sharedPreferences.getInt("spinnerOutroOrgaoPosition", 0); // 0 é o valor padrão se não houver nada salvo

        spinnerOutroOrgao.setSelection(selectedPositionOutroOrgao);

        spinnerOutroOrgao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String selectedItem = parentView.getItemAtPosition(position).toString();
                // Toast.makeText(getApplicationContext(), "Selecionado: " + selectedItem, Toast.LENGTH_SHORT).show(); //código para exibir item selecionado
// Defina a cor do item selecionado para preto
                if (selectedItemView != null && selectedItemView instanceof TextView) {
                    ((TextView) selectedItemView).setTextColor(Color.BLACK);
                    // Salvar a posição da seleção na SharedPreferences
                    sharedPreferences.edit().putInt("spinnerOutroOrgaoPosition", position).apply();

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
        // Recuperar a posição salva, se existir
        int selectedPositionGrupamentoApoio = sharedPreferences.getInt("spinnerGrupamentoApoioPosition", 0); // 0 é o valor padrão se não houver nada salvo

        spinnerGrupamentoApoio.setSelection(selectedPositionGrupamentoApoio);

        spinnerGrupamentoApoio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String selectedItem = parentView.getItemAtPosition(position).toString();
                // Toast.makeText(getApplicationContext(), "Selecionado: " + selectedItem, Toast.LENGTH_SHORT).show(); //código para exibir item selecionado
// Defina a cor do item selecionado para preto
                if (selectedItemView != null && selectedItemView instanceof TextView) {
                    ((TextView) selectedItemView).setTextColor(Color.BLACK);
                    // Salvar a posição da seleção na SharedPreferences
                    sharedPreferences.edit().putInt("spinnerGrupamentoApoioPosition", position).apply();

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

        // Recuperar o texto digitado anteriormente, se existir
        String savedText = sharedPreferences.getString("autoCompleteText", "");
        autoCompleteTextView.setText(savedText);


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
                    //Toast.makeText(getApplicationContext(), "Entrada inválida", Toast.LENGTH_SHORT).show();
                    //autoCompleteTextView.requestFocus(); // Mantenha o foco no campo de texto

                } else {
                    autoCompleteTextView.setError(null); // Limpa o erro
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                // Salvar o texto digitado nas SharedPreferences após cada alteração
                sharedPreferences.edit().putString("autoCompleteText", editable.toString()).apply();

            }
        });
        //FINAL DO AUTO COMPLETE TIPO OCORRENCIA

    }//FINAL ON CREATE

    private void obterImagemGaleria() {
        Intent intent = new Intent (Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivity(intent);
        startActivityForResult(Intent.createChooser(intent, "Escolha uma imagem"), 0);

    }

    @Override // necessario para o obterimagemgaleria
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0){
            mSelectedUri = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mSelectedUri);
                mImgPhoto.setImageDrawable(new BitmapDrawable(bitmap));
            }catch (IOException e){

            }
            }

    }

    private void uploadImageToStorage(Uri imageUri, OnSuccessListener<String> urlSuccessListener) {
        Long id;
        id = System.currentTimeMillis();//id automatica falta colocar para consultar o banco e não repetir
        idString = Long.toString(id);// transforma em string para nomear o documento com a mesma numeração da id

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("seu_caminho_no_storage/" + idString);

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Imagem enviada com sucesso
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Obtenha a URL da imagem
                        imageUrl = uri.toString();
                        // Agora, você pode salvar a URL no Firestore
                        urlSuccessListener.onSuccess(imageUrl); // Chamando o listener com a URL

                    });
                })
                .addOnFailureListener(e -> {
                    // Tratamento de erro ao enviar a imagem para o Storage
                });
    }





    //função salvar dados

    private void salvarDadosBOGCM(){



        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();



        // Obtenha a data e hora atual
        Date dataHoraAtualRegistro = new Date();

// Crie um formato de data e hora desejado
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

// Converta a data e hora atual em uma string no formato desejado
        String dataHoraRegistroFormatada = formato.format(dataHoraAtualRegistro);


        String idBOGCM = idString;// recebi o id do bogcm atraves da geraçao do uploadfoto1

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


        Spinner spinnerOutroOrgao = findViewById(R.id.spinner_encaminhamento_orgao);
        String outroOrgaoSelecionado = spinnerOutroOrgao.getSelectedItem().toString();

        Spinner spinnerGrupamentoApoio = findViewById(R.id.spinner_grupamentoEquipeApoio);
        String grupamentoApoioSelecionado = spinnerGrupamentoApoio.getSelectedItem().toString();


        // Crie um objeto para armazenar esses dados
        Map<String, Object> dataToSave = new HashMap<>();


        dataToSave.put("urlFoto1", imageUrl);


        dataToSave.put("idBOGCM", idBOGCM);

        //salvar edittext
        dataToSave.put("data", dataBogcm);
        dataToSave.put("horaInicio", horaInicio);
        dataToSave.put("horaFinal", horaFinal);


        dataToSave.put("comoFoisolicitado", comoFoiSolicitadoSelecionado);



        dataToSave.put("endereco", endereco);
        dataToSave.put("nrEndereco", nrEndereco);
        dataToSave.put("complementoendereco", complementoEnd);
        dataToSave.put("pontoReferencia", pontoReferencia);
        dataToSave.put("bairro", bairroSelecionado);        // salvar spinner


        dataToSave.put("tipoOcorrencia", tipoOcorrencia);
        dataToSave.put("relatoObsevacao", relatoObservacao);
        dataToSave.put("materialRecolhidoApreendido", materialRecApreendido);

        dataToSave.put("cpfQ1", cpfQ1);
        dataToSave.put("nomeQ1", nomeQ1);
        dataToSave.put("telefoneQ1", telefoneQ1);

        dataToSave.put("cpfQ2", cpfQ2);
        dataToSave.put("nomeQ2", nomeq2);
        dataToSave.put("telefoneQ2", telefoneq2);

        dataToSave.put("encaminhamentoOutroOrgao", outroOrgaoSelecionado);        // salvar spinner
        dataToSave.put("horaChegadaOutroOrgao", horaChegadaOutroOrgao);
        dataToSave.put("horaSaidaOutroOrgao", horaSaidaOutroOrgao);
        dataToSave.put("nrRegistroOutroOrgao", nrRegistroOutroOrgao);

        dataToSave.put("nomeGcmCondutorOcorrencia", nomeGCMCondutorOcorrencia);
        dataToSave.put("grupamentoCondutorOcorrencia", grupamentoSelecionado);        // salvar spinner


        dataToSave.put("nomeGcmApoioOcorrencia", nomeGCMApoioOcorrencia);
        dataToSave.put("grupamentoApoio", grupamentoApoioSelecionado);



        //salvar data e hora cadastro
        dataToSave.put("dataHoraCadastro", dataHoraRegistroFormatada);

        dataToSave.put("idUsuarioRegistrou", usuarioId);


        System.currentTimeMillis();

        // ...

        DocumentReference documentReference = db.collection("DB_BOGCM").document(idBOGCM); // .document(idString) coloca o nr da id no documento
        documentReference.set(dataToSave)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(getApplicationContext(), "Dados salvos com sucesso", Toast.LENGTH_SHORT).show();
                        progressBarBOGCM.setVisibility(View.INVISIBLE);
                        // habilita o botão depois do processo
                        btn_ComOcorrencia.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Erro ao salvar dados", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Erro ao salvar dados: " + e.getMessage());
                        progressBarBOGCM.setVisibility(View.INVISIBLE);
                        // habilita o botão depois do processo
                        btn_ComOcorrencia.setVisibility(View.VISIBLE);
                    }
                });
    }// final da funçAo salvar


    // Exemplo de como você poderia chamar as funções
    private void exemploChamadaFuncoes(Uri imageUri) {
        uploadImageToStorage(imageUri, new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String url) {
                // A URL da imagem está disponível aqui
                salvarDadosBOGCM();
                limparCampos();
            }
        });
    }

















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


    // Função para validar os campos
    private boolean validarCampos() {
       // String dataBogcm = eText.getText().toString();
        //String horaInicio = editTextTime.getText().toString();
       // String horaFinal = editTextTimehorafinal.getText().toString();

        //return !dataBogcm.isEmpty() && !horaInicio.isEmpty() && !horaFinal.isEmpty();

        String dataBogcm = eText.getText().toString();
        String horaInicio = editTextTime.getText().toString();
        String horaFinal = editTextTimehorafinal.getText().toString();
        String endereco = editTextEndereco.getText().toString();
        String nomeGCMCondutorOcorrencia = editTextNomeGCMCondutorOcorrencia.getText().toString();




        // Verifique se as Spinner têm seleções válidas
        Spinner spinner = findViewById(R.id.spinnercomo);
        String comoFoiSolicitadoSelecionado = spinner.getSelectedItem().toString();

        Spinner spinnerBairro = findViewById(R.id.spinnerBairro);
        String bairroSelecionado = spinnerBairro.getSelectedItem().toString();

        Spinner spinnerGrupamento = findViewById(R.id.spinner_grupamento);
        String grupamentoSelecionado = spinnerGrupamento.getSelectedItem().toString();




        return !dataBogcm.isEmpty() && !horaInicio.isEmpty() && !horaFinal.isEmpty() &&
                !endereco.isEmpty() && !nomeGCMCondutorOcorrencia.isEmpty() &&
                !comoFoiSolicitadoSelecionado.equals("COMO FOI SOLICITADO?") &&
                !bairroSelecionado.equals("BAIRRO?") && !grupamentoSelecionado.equals("GRUPAMENTO?");






    }

    // Função para sinalizar os campos obrigatórios
    private void sinalizarCamposObrigatorios() {
        if (eText.getText().toString().isEmpty()) {
            eText.setError("Campo obrigatório *");
        }
        if (editTextTime.getText().toString().isEmpty()) {
            editTextTime.setError("Campo obrigatório *");
        }
        if (editTextTimehorafinal.getText().toString().isEmpty()) {
            editTextTimehorafinal.setError("Campo obrigatório *");
        }
        if (editTextEndereco.getText().toString().isEmpty()) {
            editTextEndereco.setError("Campo obrigatório *");
        }
        if (editTextNomeGCMCondutorOcorrencia.getText().toString().isEmpty()) {
            editTextNomeGCMCondutorOcorrencia.setError("Campo obrigatório *");
        }

        Spinner spinner = findViewById(R.id.spinnercomo);
        if (spinner.getSelectedItemPosition() == 0) {
            View selectedView = spinner.getSelectedView();
            if (selectedView instanceof TextView) {
                ((TextView) selectedView).setError("Campo obrigatório *");
            }
        }
        Spinner spinnerBairro = findViewById(R.id.spinnerBairro);
        if (spinnerBairro.getSelectedItemPosition() == 0) {
            View selectedView = spinnerBairro.getSelectedView();
            if (selectedView instanceof TextView) {
                ((TextView) selectedView).setError("Campo obrigatório *");
            }
        }
        Spinner spinnerGrupamento = findViewById(R.id.spinner_grupamento);
        if (spinnerGrupamento.getSelectedItemPosition() == 0) {
            View selectedView = spinnerGrupamento.getSelectedView();
            if (selectedView instanceof TextView) {
                ((TextView) selectedView).setError("Campo obrigatório *");
            }
        }


    }

    private void limparCampos() {
        eText.setText("");
        editTextTime.setText("");
        editTextTimehorafinal.setText("");
        editTextEndereco.setText("");
        editTextNrdoEndereco.setText("");
        editTextComplementoEndereco.setText("");
        editTextPontoReferencia.setText("");
        editTextRelatoObservacao.setText("");
        editTextMaterialRecolApreendido.setText("");
        editTextNomeQ1.setText("");
        editTextNomeQ2.setText("");
        editTextCpfQ1.setText("");
        editTextCpfq2.setText("");
        editTextTelefoneQ1.setText("");
        editTextTelefoneQ2.setText("");
        editTextHoraChegadaOutroOrgao.setText("");
        editTextHoraSaidaOutroOrgao.setText("");
        editTextNrRegistroOutroOrgao.setText("");
        editTextNomeGCMCondutorOcorrencia.setText("");
        editTextNomeGCMApoioOcorrencia.setText("");
        autoCompleteTextView.setText(""); // Limpando o AutoCompleteTextView
        // Limpando as spinners
        Spinner spinnerComo = findViewById(R.id.spinnercomo);
        spinnerComo.setSelection(0); // Seleciona o primeiro item (ou outro item padrão)
        Spinner spinnerBairro = findViewById(R.id.spinnerBairro);
        spinnerBairro.setSelection(0);
        Spinner spinnerGrupamento = findViewById(R.id.spinner_grupamento);
        spinnerGrupamento.setSelection(0);
        Spinner spinnerOutroOrgao = findViewById(R.id.spinner_encaminhamento_orgao);
        spinnerOutroOrgao.setSelection(0);
        Spinner spinnerGrupamentoApoio = findViewById(R.id.spinner_grupamentoEquipeApoio);
        spinnerGrupamentoApoio.setSelection(0);
    }


}//FINAL PUBLIC CLASS







