package com.example.listahugo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ArrayList<Videojuego> videojuegos = new ArrayList<>();
        videojuegos.add(new Videojuego("The Legend of Zelda: Tears of the Kingdom", "Nintendo Switch", R.drawable.ic_game_1));
        videojuegos.add(new Videojuego("Elden Ring", "PlayStation 5 / PC", R.drawable.ic_game_2));
        videojuegos.add(new Videojuego("God of War Ragnarök", "PlayStation 5", R.drawable.ic_game_3));
        videojuegos.add(new Videojuego("Cyberpunk 2077", "PC / Xbox Series X", R.drawable.ic_game_4));
        videojuegos.add(new Videojuego("Super Mario Odyssey", "Nintendo Switch", R.drawable.ic_game_5));
        videojuegos.add(new Videojuego("Halo Infinite", "Xbox Series X / PC", R.drawable.ic_game_6));
        videojuegos.add(new Videojuego("Red Dead Redemption 2", "PlayStation 4 / PC", R.drawable.ic_game_7));
        videojuegos.add(new Videojuego("Hollow Knight", "Multiplataforma", R.drawable.ic_game_8));



    }
}