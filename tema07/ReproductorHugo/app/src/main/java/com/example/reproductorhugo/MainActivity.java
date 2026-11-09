package com.example.reproductorhugo;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ExoPlayer player;
    private SeekBar seekBar;
    private TextView tvActual;
    private TextView tvTotal;
    private boolean arrastrando = false;

    private SoundPool soundPool;
    private int idMaullido;
    private int idTrueno;
    private int idViento;
    private int idPato;
    private int sonidosCargados = 0;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable actualizar = new Runnable() {
        @Override
        public void run() {
            if (!arrastrando) {
                long posicion = player.getCurrentPosition();
                seekBar.setProgress((int) posicion);
                tvActual.setText(formatear(posicion));
            }
            handler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        seekBar = findViewById(R.id.seekBar);
        tvActual = findViewById(R.id.tvActual);
        tvTotal = findViewById(R.id.tvTotal);
        Button btnPlay = findViewById(R.id.btnPlay);
        Button btnPausa = findViewById(R.id.btnPausa);

        Button btnMaullido = findViewById(R.id.btnMaullido);
        Button btnTrueno = findViewById(R.id.btnTrueno);
        Button btnViento = findViewById(R.id.btnViento);
        Button btnPato = findViewById(R.id.btnPato);

        player = new ExoPlayer.Builder(this).build();
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.cancion);
        player.setMediaItem(MediaItem.fromUri(uri));

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int estado) {
                if (estado == Player.STATE_READY) {
                    long duracion = player.getDuration();
                    seekBar.setMax((int) duracion);
                    tvTotal.setText(formatear(duracion));
                }
            }
        });

        player.prepare();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar barra) {
                arrastrando = true;
            }

            @Override
            public void onProgressChanged(SeekBar barra, int progreso, boolean delUsuario) {
                if (delUsuario) {
                    tvActual.setText(formatear(progreso));
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar barra) {
                player.seekTo(barra.getProgress());
                arrastrando = false;
            }
        });

        btnPlay.setOnClickListener(v -> {
            if (player.getPlaybackState() == Player.STATE_ENDED) {
                player.seekTo(0);
            }
            player.play();
        });

        btnPausa.setOnClickListener(v -> player.pause());

        AudioAttributes atributos = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(atributos)
                .build();

        idMaullido = soundPool.load(this, R.raw.maullido, 1);
        idTrueno = soundPool.load(this, R.raw.trueno, 1);
        idViento = soundPool.load(this, R.raw.viento, 1);
        idPato = soundPool.load(this, R.raw.pato, 1);

        soundPool.setOnLoadCompleteListener((pool, idSonido, estado) -> {
            if (estado == 0) {
                sonidosCargados++;
                if (sonidosCargados == 4) {
                    btnMaullido.setEnabled(true);
                    btnTrueno.setEnabled(true);
                    btnViento.setEnabled(true);
                    btnPato.setEnabled(true);
                }
            }
        });

        btnMaullido.setOnClickListener(v -> soundPool.play(idMaullido, 1f, 1f, 1, 0, 1f));
        btnTrueno.setOnClickListener(v -> soundPool.play(idTrueno, 1f, 1f, 1, 0, 1f));
        btnViento.setOnClickListener(v -> soundPool.play(idViento, 1f, 1f, 1, 0, 1f));
        btnPato.setOnClickListener(v -> soundPool.play(idPato, 1f, 1f, 1, 0, 1f));
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.post(actualizar);
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.pause();
        handler.removeCallbacks(actualizar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
        soundPool.release();
    }

    private String formatear(long ms) {
        long segundos = ms / 1000;
        return String.format(Locale.getDefault(), "%02d:%02d", segundos / 60, segundos % 60);
    }
}