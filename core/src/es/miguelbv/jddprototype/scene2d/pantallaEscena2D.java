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

package es.miguelbv.jddprototype.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import es.miguelbv.jddprototype.juego.pantallaBase;
import es.miguelbv.jddprototype.juego.MainGame;


public class pantallaEscena2D extends pantallaBase {

    /** El escenario de Scene2D donde se añaden todos los actores. */
    private Stage stage;

    /** Actor del jugador */
    private actorJugador player;

    /** Actor de los pinchos */
    private actorPincho spikes;

    /** Textures que se usan en la pantalla */
    private Texture playerTexture, spikeTexture;

    /** Regiones utilizadas en la pantalla. */
    private TextureRegion spikeRegion;

    public pantallaEscena2D(MainGame game) {
        super(game);

        // Cargamos los assets utilizando la nueva textura en lugar del asset manager.
        playerTexture = new Texture("player.png");
        spikeTexture = new Texture("spike.png");
        spikeRegion = new TextureRegion(spikeTexture, 0, 64, 128, 64);
    }

    @Override
    public void show() {
        // Creamos un escenario
        stage = new Stage(new FitViewport(640, 400));

        // Cargamos los actores
        player = new actorJugador(playerTexture);
        spikes = new actorPincho(spikeRegion, 2100, 100, 500);
        player.setPosition(20, 100);

        // Añadimos los actores al escenario
        stage.addActor(player);
        stage.addActor(spikes);
    }

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        checkCollisions();
        stage.draw();
    }

    /**
     * Este método verifica las colisiones entre el jugador y los pinchos.
     */
    private void checkCollisions() {
        if (player.isAlive() &&  (player.getX() + player.getWidth()) > spikes.getX()) {
            System.out.println("A collision has happened.");
            player.setAlive(false);
        }
    }

    @Override
    public void dispose() {
        playerTexture.dispose();
        spikeTexture.dispose();
        stage.dispose();
    }
}
