package com.example.sisgcm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.checkerframework.checker.nullness.qual.NonNull;


public class TelaPesquisa extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_pesquisa);

        getSupportActionBar().hide(); //esconder a barra da tela com o nome do programa

        recyclerView = findViewById(R.id.idRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query = db.collection("DB_BOGCM")
                .orderBy("dataHoraCadastro", Query.Direction.DESCENDING) // Substitua "data" pelo nome do campo relevante
                .limit(3);

        FirestoreRecyclerOptions<BoGCM> options = new FirestoreRecyclerOptions.Builder<BoGCM>()
                .setQuery(query, BoGCM.class)
                .build();


         adapter = new FirestoreRecyclerAdapter<BoGCM, ViewHolder>(options) {
            @Override
            public void onBindViewHolder(ViewHolder holder, int position, BoGCM model) {
               holder.bind(model);
            }


            @Override
            public ViewHolder onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item_bogcm, group, false);

                return new ViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);

    }// final on create

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView idBOGCM, dataBOGCM, horaInicio, horaFinal, comoFoiSolicitado, endereco, nrEndereco, complemento,
                pontoReferencia, bairro, tipoOcorrencia, relatoObsevacao, materialRecolhidoApreendido, nomeQ1, cpfQ1, telefoneQ1,
                nomeQ2, cpfQ2, telefoneQ2, encaminhamentoOutroOrgao, horaChegadaOutroOrgao, horaSaidaOutroOrgao,
                nrRegistroOutroOrgao, nomeGcmCondutorOcorrencia, grupamentoCondutorOcorrencia, nomeGcmApoioOcorrencia, grupamentoApoio, dataHoraCadastro, idUsuarioRegistrou;

        ImageView imgFoto1;

        public ViewHolder(@NonNull View itemView){
            super(itemView);


            idBOGCM = itemView.findViewById(R.id.textViewidBOGCM);
            dataBOGCM = itemView.findViewById(R.id.textViewData);
            horaInicio = itemView.findViewById(R.id.textViewhoraInicial);
            horaFinal = itemView.findViewById(R.id.textViewHoraFinal);
            comoFoiSolicitado = itemView.findViewById(R.id.textViewComoFoiSolicitado);
            endereco = itemView.findViewById(R.id.textViewEndereco);
            nrEndereco = itemView.findViewById(R.id.textViewNrEndereco);
            complemento = itemView.findViewById(R.id.textViewComplemento);
            pontoReferencia = itemView.findViewById(R.id.textViewPontoReferencia);
            bairro = itemView.findViewById(R.id.textViewBairro);
            tipoOcorrencia = itemView.findViewById(R.id.textViewTipoOcorrencia);
            materialRecolhidoApreendido = itemView.findViewById(R.id.textViewMaterialRecolhidoApreendido);
            relatoObsevacao = itemView.findViewById(R.id.textViewRelatoObservacao);
            nomeQ1 = itemView.findViewById(R.id.textViewNomeQ1);
            cpfQ1 = itemView.findViewById(R.id.textViewCpfQ1);
            telefoneQ1 = itemView.findViewById(R.id.textViewTelefoneQ1);
            nomeQ2 = itemView.findViewById(R.id.textViewNomeQ2);
            cpfQ2 = itemView.findViewById(R.id.textViewCpfQ2);
            telefoneQ2 = itemView.findViewById(R.id.textViewTelefoneQ2);
            encaminhamentoOutroOrgao = itemView.findViewById(R.id.textViewOrgaoEncaminhado);
            horaChegadaOutroOrgao = itemView.findViewById(R.id.textViewHoraChegadaOrgaoEncam);
            horaSaidaOutroOrgao = itemView.findViewById(R.id.textViewHoraSaidaOrgaoEncam);
            nrRegistroOutroOrgao = itemView.findViewById(R.id.textViewNrRegOrgaoEncam);
            nomeGcmCondutorOcorrencia = itemView.findViewById(R.id.textViewNomeGCMCondutorOcorrencia);
            grupamentoCondutorOcorrencia = itemView.findViewById(R.id.textViewGrupamentoCondutorOcorrencia);
            nomeGcmApoioOcorrencia = itemView.findViewById(R.id.textViewNomeGcmApoio);
            grupamentoApoio = itemView.findViewById(R.id.textViewGrupamentoGcmApoio);
            dataHoraCadastro = itemView.findViewById(R.id.textViewDataHoraSalvou);
            idUsuarioRegistrou = itemView.findViewById(R.id.textViewIdUsuario);
            imgFoto1 = itemView.findViewById(R.id.imgFoto1);




        }

        public void bind(BoGCM boGCM){
            idBOGCM.setText(boGCM.getIdBOGCM());
            dataBOGCM.setText(boGCM.getData());
            horaInicio.setText(boGCM.getHoraInicio());
            horaFinal.setText(boGCM.getHoraFinal());
            comoFoiSolicitado.setText(boGCM.getComoFoisolicitado());
            endereco.setText(boGCM.getEndereco());
            nrEndereco.setText(boGCM.getNrEndereco());
            complemento.setText(boGCM.getComplementoendereco());
            pontoReferencia.setText(boGCM.getPontoReferencia());
            bairro.setText(boGCM.getBairro());
            tipoOcorrencia.setText(boGCM.getTipoOcorrencia());
            relatoObsevacao.setText(boGCM.getRelatoObsevacao());
            materialRecolhidoApreendido.setText(boGCM.getMaterialRecolhidoApreendido());
            cpfQ1.setText(boGCM.getCpfQ1());
            nomeQ1.setText(boGCM.getNomeQ1());
            telefoneQ1.setText(boGCM.getTelefoneQ1());
            cpfQ2.setText(boGCM.getCpfQ2());
            nomeQ2.setText(boGCM.getNomeQ2());
            telefoneQ2.setText(boGCM.getTelefoneQ2());
            encaminhamentoOutroOrgao.setText(boGCM.getEncaminhamentoOutroOrgao());
            horaChegadaOutroOrgao.setText(boGCM.getHoraChegadaOutroOrgao());
            horaSaidaOutroOrgao.setText(boGCM.getHoraSaidaOutroOrgao());
            nrRegistroOutroOrgao.setText(boGCM.getNrRegistroOutroOrgao());
            nomeGcmCondutorOcorrencia.setText(boGCM.getNomeGcmCondutorOcorrencia());
            grupamentoCondutorOcorrencia.setText(boGCM.getGrupamentoCondutorOcorrencia());
            nomeGcmApoioOcorrencia.setText(boGCM.getNomeGcmApoioOcorrencia());
            grupamentoApoio.setText(boGCM.getGrupamentoApoio());
            dataHoraCadastro.setText(boGCM.getDataHoraCadastro());
            idUsuarioRegistrou.setText(boGCM.getIdUsuarioRegistrou());
            Glide.with(itemView.getContext()).load(boGCM.getUrlFoto1()).into(imgFoto1);



        }

    }


    @Override
    public void onBackPressed() {

        // Ou, para retornar à tela anterior (activity anterior), você pode usar o Intent:
        Intent intent = new Intent(this, TelaPrincipal.class); // Substitua TelaAnterior pela classe da sua atividade anterior
        startActivity(intent);
        finish(); // Finaliza a atividade atual
    }

}