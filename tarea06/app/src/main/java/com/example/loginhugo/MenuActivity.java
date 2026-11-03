package com.example.loginhugo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

public class MenuActivity extends AppCompatActivity {

    public static final String EXTRA_CORREO = "correo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tvSaludo = findViewById(R.id.tvSaludo);
        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        String correo = getIntent().getStringExtra(EXTRA_CORREO);
        if (correo != null) {
            tvSaludo.setText(getString(R.string.saludo_correo, correo));
        }
    }
}