package com.example.trabalhomobile_;

public class Veiculo {

    private String placa;
    private String entrada;
    private String saida;

    public Veiculo() {} // Firebase precisa de um construtor vazio

    public Veiculo(String placa, String entrada, String saida) {
        this.placa = placa;
        this.entrada = entrada;
        this.saida = saida;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
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
