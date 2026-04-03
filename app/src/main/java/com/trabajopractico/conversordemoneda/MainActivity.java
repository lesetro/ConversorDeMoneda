package com.trabajopractico.conversordemoneda;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.trabajopractico.conversordemoneda.viewmodel.MonedaViewModel;

// View (Activity) - Maneja la interfaz gráfica.
// Observa los datos del ViewModel.
// NO contiene lógica de negocio.

public class MainActivity extends AppCompatActivity {

    // ViewModel
    private MonedaViewModel viewModel;

    // Elementos de la UI
    private EditText etDolares;
    private EditText etEuros;
    private TextView tvTipoCambio;
    private RadioGroup rgConversion;
    private RadioButton rbADolares;
    private RadioButton rbAEuros;
    private Button btnConvertir;
    private Button btnCambiarValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar el ViewModel
        viewModel = new ViewModelProvider(this).get(MonedaViewModel.class);

        // Inicializar las vistas
        inicializarVistas();

        // Configurar los observers para LiveData
        configurarObservers();

        // Configurar los listeners de los botones
        configurarListeners();
    }

    // Inicializa las referencias a los elementos de la UI

    private void inicializarVistas() {
        etDolares = findViewById(R.id.etDolares);
        etEuros = findViewById(R.id.etEuros);
        tvTipoCambio = findViewById(R.id.tvTipoCambio);
        rgConversion = findViewById(R.id.rgConversion);
        rbADolares = findViewById(R.id.rbADolares);
        rbAEuros = findViewById(R.id.rbAEuros);
        btnConvertir = findViewById(R.id.btnConvertir);
        btnCambiarValor = findViewById(R.id.btnCambiarValor);
    }

    // Configura los observers para observar cambios en el ViewModel

    private void configurarObservers() {
        // Observar cambios en el resultado de dólares
        viewModel.getResultadoDolares().observe(this, resultado -> {
            if (resultado != null) {
                etDolares.setText(resultado);
            }
        });

        // Observar cambios en el resultado de euros
        viewModel.getResultadoEuros().observe(this, resultado -> {
            if (resultado != null) {
                etEuros.setText(resultado);
            }
        });

        // Observar cambios en el tipo de cambio
        viewModel.getTextoTipoCambio().observe(this, textoTipoCambio -> {
            if (textoTipoCambio != null) {
                tvTipoCambio.setText(textoTipoCambio);
            }
        });

        // Observar mensajes de error
        viewModel.getMensajeError().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Configura los listeners de los botones

    private void configurarListeners() {
        // Listener del botón Convertir
        btnConvertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarConversion();
            }
        });

        // Listener del botón Cambiar valor
        btnCambiarValor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoCambiarTipoCambio();
            }
        });
    }

    // Realiza la conversión según la opción seleccionada

    private void realizarConversion() {
        // Verificar qué opción está seleccionada
        int idSeleccionado = rgConversion.getCheckedRadioButtonId();

        if (idSeleccionado == -1) {
            Toast.makeText(this, "Seleccione un tipo de conversión", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean convertirAEuros = (idSeleccionado == R.id.rbAEuros);

        // Obtener el valor de entrada según la conversión seleccionada
        String valorIngresado;
        if (convertirAEuros) {
            // Si convierte a euros, el input es dólares
            valorIngresado = etDolares.getText().toString();
        } else {
            // Si convierte a dólares, el input es euros
            valorIngresado = etEuros.getText().toString();
        }

        // Llamar al ViewModel para realizar la conversión
        viewModel.convertir(valorIngresado, convertirAEuros);
    }

    // Muestra un diálogo para cambiar el tipo de cambio

    private void mostrarDialogoCambiarTipoCambio() {
        // Crear un EditText para el diálogo
        final EditText input = new EditText(this);
        input.setHint("Nuevo tipo de cambio");
        input.setText(viewModel.getTipoCambioActual());
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER |
                android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

        // Padding para el EditText
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        input.setPadding(padding, padding, padding, padding);

        // Crear y mostrar el diálogo
        new AlertDialog.Builder(this)
                .setTitle("Cambiar tipo de cambio")
                .setMessage("1 USD = X EUR")
                .setView(input)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    String nuevoTipoCambio = input.getText().toString();
                    viewModel.cambiarTipoCambio(nuevoTipoCambio);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}