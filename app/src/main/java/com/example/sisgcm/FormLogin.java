package com.example.sisgcm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class FormLogin extends AppCompatActivity {


    private EditText edit_matricula, edit_Senha;
    private Button btn_entrar;
    private ProgressBar progressBar;
    String[] mensagens = {"Preencha todos os campos", "Login efetuado com sucesso!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_form_login);



        getSupportActionBar().hide(); //esconder a barra da tela com o nome do programa
        IniciarComponentes();

        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String matricula = edit_matricula.getText().toString();
                String senha = edit_Senha.getText().toString();

                if (matricula.isEmpty() || senha.isEmpty()){
                    Snackbar snackbar =Snackbar.make(v,mensagens[0],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    AutenticarUsuario(v);


                }

            }
        });


        }

    private void AutenticarUsuario (View v){
        String matricula = edit_matricula.getText().toString();
        String senha = edit_Senha.getText().toString();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(matricula,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TelaPrincipal();
                        }
                    },3000);
                }else{
                    String erro;
                    try {
                        throw task.getException();

                    }catch (FirebaseAuthWeakPasswordException e){
                        erro = "Digite uma senha com no mínimo 6 caracteres";

                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "E-mail inválido";



                    }catch (Exception e){
                        erro = "Erro ao logar";
                    }
                    Snackbar snackbar =Snackbar.make(v,erro,Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        })  ;
    }


    private void TelaPrincipal(){
        Intent intent = new Intent(FormLogin.this,TelaPrincipal.class);
        startActivity(intent);
        finish();

    }



    private void IniciarComponentes(){
        edit_matricula=findViewById(R.id.edit_matricula);
        edit_Senha=findViewById(R.id.edit_senha);
        btn_entrar=findViewById(R.id.btn_entrar);
        progressBar=findViewById(R.id.progressbar);
    }

}