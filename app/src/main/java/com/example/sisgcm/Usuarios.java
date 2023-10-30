package com.example.sisgcm;

public class Usuarios {
    private String idUsuario;
    private String matc;
    private String nivelUsuario;
    private String nomeGuerraUsuario;

    public Usuarios(){

    }


    public Usuarios(String idUsuario, String matc, String nivelUsuario, String nomeGuerraUsuario) {
        this.idUsuario = idUsuario;
        this.matc = matc;
        this.nivelUsuario = nivelUsuario;
        this.nomeGuerraUsuario = nomeGuerraUsuario;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMatc() {
        return matc;
    }

    public void setMatc(String matc) {
        this.matc = matc;
    }

    public String getNivelUsuario() {
        return nivelUsuario;
    }

    public void setNivelUsuario(String nivelUsuario) {
        this.nivelUsuario = nivelUsuario;
    }

    public String getNomeGuerraUsuario() {
        return nomeGuerraUsuario;
    }

    public void setNomeGuerraUsuario(String nomeGuerraUsuario) {
        this.nomeGuerraUsuario = nomeGuerraUsuario;
    }

    @Override
    public String toString() {
        return "Usuarios{" +
                "idUsuario='" + idUsuario + '\'' +
                ", matc='" + matc + '\'' +
                ", nivelUsuario='" + nivelUsuario + '\'' +
                ", nomeGuerraUsuario='" + nomeGuerraUsuario + '\'' +
                '}';
    }
}
