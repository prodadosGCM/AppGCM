package com.example.sisgcm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

public class TelaPrincipal extends AppCompatActivity {
    private Button btn_bogcm;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

            getSupportActionBar().hide(); //esconder a barra da tela com o nome do programa
            IniciarComponentes2();

            btn_bogcm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TelaPrincipal.this,FormBOGCM.class);
                    startActivity(intent);
                    finish();


                };
});}
            private void IniciarComponentes2(){
                btn_bogcm=findViewById(R.id.btn_BOGCM);

        }}