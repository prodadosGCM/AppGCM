package com.example.sisgcm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class TelaPrincipal extends AppCompatActivity {
    private Button btn_bogcm, btn_pesquisa, btn_dinamica, btn_deslogar;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

            getSupportActionBar().hide(); //esconder a barra da tela com o nome do programa

            btn_bogcm=findViewById(R.id.btn_BOGCM);


            btn_bogcm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TelaPrincipal.this,FormBOGCM.class);
                    startActivity(intent);
                    finish();
                };
});
            //tela pesquisa
            btn_pesquisa=findViewById(R.id.btn_Pesquisar);

            btn_pesquisa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TelaPrincipal.this,TelaPesquisa.class);
                    startActivity(intent);
                    finish();


                };
            });

            //tela dinamica
            btn_dinamica=findViewById(R.id.btn_Dinamica);
            btn_dinamica.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TelaPrincipal.this,TelaDinamica.class);
                    startActivity(intent);
                    finish();
                };
            });

            //botão deslogar
            btn_deslogar=findViewById(R.id.btn_Deslogar);
            btn_deslogar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(TelaPrincipal.this,FormLogin.class);
                    startActivity(intent);
                    finish();
                };
            });


        } // final do oncreate
            }