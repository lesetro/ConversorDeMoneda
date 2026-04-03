package com.trabajopractico.conversordemoneda.model;

//Model - Clase que representa la lógica de conversión de monedas.
//Contiene el tipo de cambio y los métodos para realizar las conversiones.
public class ConversorMoneda {
        private double tipoDeCambio;

    // Constructor con valor por defecto
    public ConversorMoneda() {
        this.tipoDeCambio = 0.92; // Para el Valor inicial por defecto
    }

    // Constructor con tipo de cambio personalizado
    public ConversorMoneda(double tipoDeCambio) {
        this.tipoDeCambio = tipoDeCambio;
    }

    //Convierte de Dólares a Euros
    public double convertirAEuros(double dolares) {
        return dolares * tipoDeCambio;
    }

    // Convierte de Euros a Dólares

    public double convertirADolares(double euros) {
        return euros / tipoDeCambio;
    }

    // Getter del tipo de cambio
    public double getTipoDeCambio() {
        return tipoDeCambio;
    }

    // Setter del tipo de cambio
    public void setTipoDeCambio(double tipoDeCambio) {
        this.tipoDeCambio = tipoDeCambio;
    }

}