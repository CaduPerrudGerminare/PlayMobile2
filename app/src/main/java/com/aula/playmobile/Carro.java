package com.aula.playmobile;

import java.util.Date;

public class Carro {
    private String placa;
    private Date data;
    private String entrada;
    private String saida;
    public Carro(){

    }

    public Carro(String placa, Date data, String entrada, String saida) {
        this.placa = placa;
        this.data = data;
        this.entrada = entrada;
        this.saida = saida;
    }


    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getEntrada() {
        return entrada;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public String getSaida() {
        return saida;
    }

    public void setSaida(String saida) {
        this.saida = saida;
    }
}
