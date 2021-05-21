/*
 * This file is part of Jump Don't Die
 * Copyright (C) 2015 Dani Rodríguez <danirod@outlook.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.miguelbv.jddprototype.juego;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class MainGame extends Game {

    private AssetManager manager;


     // Estas son las pantallas que usamos en este juego.
    public pantallaBase loadingScreen, menuScreen, gameScreen, gameOverScreen;

    @Override
    public void create() {
        // Inicializamos el AssetManager. Agregamos cada asset al administrador para que se pueda cargar
        // dentro de la pantalla LoadingScreen.
        manager = new AssetManager();
        manager.load("suelo.png", Texture.class);
        manager.load("gameover.png", Texture.class);
        manager.load("suelo2.png", Texture.class);
        manager.load("logo.png", Texture.class);
        manager.load("pincho.png", Texture.class);
        manager.load("jugador.png", Texture.class);
        manager.load("audio/muerte.ogg", Sound.class);
        manager.load("audio/salto.ogg", Sound.class);
        manager.load("audio/musica.ogg", Music.class);

        // Añadimos a la pantalla de carga para cargar los assets.
        loadingScreen = new pantallaCarga(this);
        setScreen(loadingScreen);
    }

     // LoadingScreen invoca este método cuando se cargan todos los assets.
    public void finishLoading() {
        menuScreen = new pantallaMenu(this);
        gameScreen = new pantallaJuego(this);
        gameOverScreen = new pantallaGameOver(this);
        setScreen(menuScreen);
    }

    public AssetManager getManager() {
        return manager;
    }

}
