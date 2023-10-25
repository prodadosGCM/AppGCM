package com.example.sisgcm;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_extra);

        FirebaseStorage storage = FirebaseStorage.getInstance();


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
        Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
        // Crie uma referência para o Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        // Crie uma referência para o local onde deseja armazenar a imagem
        StorageReference signatureRef = storageRef.child("assinatura_Q1/assinatura.png");

        // Converte o bitmap em um array de bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        // Faz o upload da imagem para o Firebase Storage
        UploadTask uploadTask = signatureRef.putBytes(data);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
                    // Imagem enviada com sucesso
                    // Agora você pode obter o URL da imagem
                    storageRef.child("assinatura_Q1/assinatura.png").getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                // URL da imagem
                                String imageUrl = uri.toString();

                                // Agora você pode salvar o URL no Firestore
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
        // Agora você pode salvar ou fazer o que quiser com a assinatura
    }
}

