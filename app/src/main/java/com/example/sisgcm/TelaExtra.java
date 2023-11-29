package com.example.sisgcm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


public class TelaExtra extends AppCompatActivity {


    private SignaturePad signaturePad;
    private Button btn_limpar, btn_salvar; //botão salvar

    private String edit_CPF_Q1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_extra);

        getSupportActionBar().hide(); //esconder a barra da tela com o nome do programa



        btn_limpar=findViewById(R.id.btn_limpar);

        btn_limpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clearSignature(view);

            }
        });


        btn_salvar=findViewById(R.id.btn_salvar);

        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveSignature(view);

            }
        });


        signaturePad = findViewById(R.id.signaturePad);

        // Configurar a espessura da caneta (opcional)
        signaturePad.setMinWidth(5);
        signaturePad.setMaxWidth(10);

        // Configurar a cor da caneta (opcional)
        signaturePad.setPenColor(Color.BLACK);
    }

    // Método para limpar a assinatura
    public void clearSignature(View view) {
        signaturePad.clear();
    }

    // Método para salvar a assinatura

    public void saveSignature(View view) {
        // Obtenha o CPF digitado pelo usuário
        EditText edit_CPF_Q1 = findViewById(R.id.edit_CPF_ExtraQ1);
        String cpf = edit_CPF_Q1.getText().toString();

        Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Crie uma referência para o local onde deseja armazenar a imagem, usando o CPF como parte do nome do arquivo
        StorageReference signatureRef = storageRef.child("assinaturas/" + cpf + "_assinatura.png");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = signatureRef.putBytes(data);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
                    // Imagem enviada com sucesso
                    storageRef.child("assinaturas/" + cpf + "_assinatura.png").getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Map<String, Object> signatureData = new HashMap<>();
                                signatureData.put("assinaturaUrl", imageUrl);

                                db.collection("DB_BOGCM")
                                        .document("documento_que_voce_deseja")
                                        .collection("assinatura_Q1")
                                        .add(signatureData)
                                        .addOnSuccessListener(documentReference -> {
                                            // Sucesso ao salvar a assinatura no Firestore
                                            Toast.makeText(this, "Assinatura salva com sucesso!", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            // Falha ao salvar a assinatura no Firestore
                                            Toast.makeText(this, "Erro ao salvar a assinatura: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                // Falha ao obter o URL da imagem
                                Toast.makeText(this, "Erro ao obter o URL da imagem: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    // Falha ao fazer upload da imagem
                    Toast.makeText(this, "Erro ao fazer upload da imagem: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }



    @Override
    public void onBackPressed() {

        // Ou, para retornar à tela anterior (activity anterior), você pode usar o Intent:
        Intent intent = new Intent(this, TelaPrincipal.class); // Substitua TelaAnterior pela classe da sua atividade anterior
        startActivity(intent);
        finish(); // Finaliza a atividade atual
    }
}

