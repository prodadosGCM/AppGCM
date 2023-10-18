package com.example.sisgcm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class TelaPesquisa extends AppCompatActivity {

    private Button shareButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_pesquisa);

        getSupportActionBar().hide(); //esconder a barra da tela com o nome do programa


        shareButton = findViewById(R.id.shareButton);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Recupere as informações do Firestore (substitua com sua lógica)
                String informationToShare = "Informações do Firestore: ...";

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
        });
    }// final on create

    @Override
    public void onBackPressed() {

        // Ou, para retornar à tela anterior (activity anterior), você pode usar o Intent:
        Intent intent = new Intent(this, TelaPrincipal.class); // Substitua TelaAnterior pela classe da sua atividade anterior
        startActivity(intent);
        finish(); // Finaliza a atividade atual
    }

}