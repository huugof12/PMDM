package com.example.loginhugo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.material.materialswitch.MaterialSwitch;

public class MainActivity extends AppCompatActivity {

    private static final String CORREO_VALIDO = "hugo@correo.com";
    private static final String CLAVE_VALIDA = "1234";

    private SharedPreferences prefs;
    private int intentos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("sesion", MODE_PRIVATE);

        EditText cajaTextoCorreo = findViewById(R.id.cajaTextoCorreo);
        EditText cajaTextoPass = findViewById(R.id.cajaTextoPass);
        MaterialSwitch swRecordar = findViewById(R.id.swRecordar);
        Button btnContinuar = findViewById(R.id.btnContinuar);
        TextView tvMensaje = findViewById(R.id.tvMensaje);

        String guardado = prefs.getString("correo", "");
        if (!guardado.isEmpty()) {
            cajaTextoCorreo.setText(guardado);
            swRecordar.setChecked(true);
            cajaTextoPass.requestFocus();
        }

        btnContinuar.setOnClickListener(v -> {
            String correo = cajaTextoCorreo.getText().toString().trim();
            String clave = cajaTextoPass.getText().toString();

            if (correo.isEmpty()) {
                cajaTextoCorreo.setError(getString(R.string.error_vacio_correo));
                return;
            }
            if (clave.isEmpty()) {
                cajaTextoPass.setError(getString(R.string.error_vacio_clave));
                return;
            }

            if (correo.equals(CORREO_VALIDO) && clave.equals(CLAVE_VALIDA)) {
                intentos = 0;

                if (swRecordar.isChecked()) {
                    prefs.edit().putString("correo", correo).apply();
                } else {
                    prefs.edit().remove("correo").apply();
                }

                Intent intent = new Intent(this, MenuActivity.class);
                intent.putExtra(MenuActivity.EXTRA_CORREO, correo);
                startActivity(intent);
                finish();
            } else {
                intentos++;
                if (intentos >= 3) {
                    btnContinuar.setEnabled(false);
                    tvMensaje.setTextColor(ContextCompat.getColor(this, R.color.colorIncorrecto));
                    tvMensaje.setText(R.string.demasiados_intentos);
                    Toast.makeText(this, R.string.demasiados_intentos, Toast.LENGTH_SHORT).show();
                } else {
                    tvMensaje.setTextColor(ContextCompat.getColor(this, R.color.colorIncorrecto));
                    tvMensaje.setText(R.string.acceso_incorrecto);
                }
            }
        });
    }
}