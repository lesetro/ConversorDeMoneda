package com.trabajopractico.conversordemoneda.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trabajopractico.conversordemoneda.model.ConversorMoneda;

// ViewModel - Contiene los datos observables y la lógica de conversión.
// Utiliza LiveData para comunicar cambios a la vista.

public class MonedaViewModel extends ViewModel {

    // Modelo de conversión
    private ConversorMoneda conversor;

    // LiveData para el resultado de la conversión
    private MutableLiveData<String> resultadoDolares;
    private MutableLiveData<String> resultadoEuros;

    // LiveData para el tipo de cambio actual
    private MutableLiveData<String> textoTipoCambio;

    // LiveData para mensajes de error
    private MutableLiveData<String> mensajeError;

    // Constructor
    public MonedaViewModel() {
        conversor = new ConversorMoneda();
        resultadoDolares = new MutableLiveData<>("");
        resultadoEuros = new MutableLiveData<>("");
        textoTipoCambio = new MutableLiveData<>();
        mensajeError = new MutableLiveData<>();

        // Inicializar el texto del tipo de cambio
        actualizarTextoTipoCambio();
    }

    // Getters para los LiveData (la Vista observará estos)
    public LiveData<String> getResultadoDolares() {
        return resultadoDolares;
    }

    public LiveData<String> getResultadoEuros() {
        return resultadoEuros;
    }

    public LiveData<String> getTextoTipoCambio() {
        return textoTipoCambio;
    }

    public LiveData<String> getMensajeError() {
        return mensajeError;
    }

    // Realiza la conversión según el tipo seleccionado
    // valorIngresado valor ingresado por el usuario
    // convertirAEuros true si convierte a euros, false si convierte a dólares

    public void convertir(String valorIngresado, boolean convertirAEuros) {
        // Validar que el campo no esté vacío
        if (valorIngresado == null || valorIngresado.trim().isEmpty()) {
            mensajeError.setValue("Por favor, ingrese un valor");
            return;
        }

        try {
            double valor = Double.parseDouble(valorIngresado.trim());

            // Validar que sea un número positivo
            if (valor < 0) {
                mensajeError.setValue("El valor debe ser positivo");
                return;
            }

            // Realizar la conversión según la opción seleccionada
            if (convertirAEuros) {
                // El usuario ingresó dólares, convertir a euros
                double euros = conversor.convertirAEuros(valor);
                resultadoDolares.setValue(String.format("%.2f", valor));
                resultadoEuros.setValue(String.format("%.2f", euros));
            } else {
                // El usuario ingresó euros, convertir a dólares
                double dolares = conversor.convertirADolares(valor);
                resultadoEuros.setValue(String.format("%.2f", valor));
                resultadoDolares.setValue(String.format("%.2f", dolares));
            }

            // Limpiar mensaje de error
            mensajeError.setValue(null);

        } catch (NumberFormatException e) {
            mensajeError.setValue("Ingrese un valor numérico válido");
        }
    }

    public void cambiarTipoCambio(String nuevoTipoCambio) {
        if (nuevoTipoCambio == null || nuevoTipoCambio.trim().isEmpty()) {
            mensajeError.setValue("Ingrese un tipo de cambio válido");
            return;
        }

        try {
            double tipoCambio = Double.parseDouble(nuevoTipoCambio.trim());

            if (tipoCambio <= 0) {
                mensajeError.setValue("El tipo de cambio debe ser mayor a 0");
                return;
            }

            conversor.setTipoDeCambio(tipoCambio);
            actualizarTextoTipoCambio();
            mensajeError.setValue(null);

            // Limpiar resultados anteriores
            resultadoDolares.setValue("");
            resultadoEuros.setValue("");

        } catch (NumberFormatException e) {
            mensajeError.setValue("Ingrese un valor numérico válido para el tipo de cambio");
        }
    }

    private void actualizarTextoTipoCambio() {
        textoTipoCambio.setValue(String.format("1$ = %.2f€", conversor.getTipoDeCambio()));
    }


    public String getTipoCambioActual() {
        return String.format("%.2f", conversor.getTipoDeCambio());
    }
}