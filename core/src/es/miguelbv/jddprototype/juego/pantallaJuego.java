/*
 * This file is part of Jump Don't Die.
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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FitViewport;

import es.miguelbv.jddprototype.juego.entidades.entidadFactory;
import es.miguelbv.jddprototype.juego.entidades.entidadSuelo;
import es.miguelbv.jddprototype.juego.entidades.entidadJugador;
import es.miguelbv.jddprototype.juego.entidades.entidadPincho;

import java.util.ArrayList;
import java.util.List;

public class pantallaJuego extends pantallaBase {

    /** Instancia del escenario para renderizarlo con Scene2D */
    private Stage stage;

    /** Instancia del mundo para el motor Box2D. */
    private World world;

    /** entidad Jugador. */
    private entidadJugador player;

    /** lista de suelos que estan en el mundo. */
    private List<entidadSuelo> floorList = new ArrayList<entidadSuelo>();

    /** Array de pinchos que esntan en el mundo. */
    private List<entidadPincho> spikeList = new ArrayList<entidadPincho>();

    /** Sonido del salto que tiene que reproducirse cuando el jugador salta. */
    private Sound jumpSound;

    /** Sonido de muerte que tiene que reproducirse cuando el jugador colisiona con una spike. */
    private Sound dieSound;

    /** Música de fondo que tiene que sonar de fondo todo el tiempo.. */
    private Music backgroundMusic;

    /** Posición inicial de la cámara. */
    private Vector3 position;

    /**
     * Creamos la pantalla.
     * @param game
     */
    public pantallaJuego(es.miguelbv.jddprototype.juego.MainGame game) {
        super(game);

        // Creamos un nuevo escenario con Scene2D para mostrar cosas.
        stage = new Stage(new FitViewport(640, 360));
        position = new Vector3(stage.getCamera().position);

        // Creamos un nuevo mundo con Box2D para gestionar las cosas.
        world = new World(new Vector2(0, -10), true);
        world.setContactListener(new GameContactListener());

        // GET de los efectos de sonido que se reproducirán durante el juego.
        jumpSound = game.getManager().get("audio/salto.ogg");
        dieSound = game.getManager().get("audio/muerte.ogg");
        backgroundMusic = game.getManager().get("audio/musica.ogg");
    }

    /**
     * Este método se ejecutará cuando esta pantalla esté a punto de ser renderizada.
     * Aquí, utilizo este método para configurar la posición inicial del escenario.
     */
    @Override
    public void show() {
        entidadFactory factory = new entidadFactory(game.getManager());

        // Creamos el jugador. Tiene una posición inicial.
        player = factory.createPlayer(world, new Vector2(1.5f, 1.5f));

        // Este es el suelo principal. Por eso es tan largo.
        floorList.add(factory.createFloor(world, 0, 1000, 1));

        // Ahora generamos algunos suelos secundarios sobre el suelo principal.
        floorList.add(factory.createFloor(world, 15, 10, 2));
        floorList.add(factory.createFloor(world, 30, 8, 2));
        floorList.add(factory.createFloor(world, 65, 10, 2));
        floorList.add(factory.createFloor(world, 80, 8, 2));

        // Generamos algunos pinchos
        spikeList.add(factory.createSpikes(world, 8, 1));
        spikeList.add(factory.createSpikes(world, 23, 2));
        spikeList.add(factory.createSpikes(world, 35, 2));
        spikeList.add(factory.createSpikes(world, 50, 1));

        spikeList.add(factory.createSpikes(world, 58, 1));
        spikeList.add(factory.createSpikes(world, 73, 2));
        spikeList.add(factory.createSpikes(world, 85, 2));
        spikeList.add(factory.createSpikes(world, 100, 1));

        // Añadimos el suelo y los pinchos al escenario
        for (entidadSuelo floor : floorList)
            stage.addActor(floor);
        for (entidadPincho spike : spikeList)
            stage.addActor(spike);

        // Añadimos el jugador al escenario
        stage.addActor(player);

        // Reinicio de la cámara por si esta volviendo a volver a jugar
        stage.getCamera().position.set(position);
        stage.getCamera().update();

        // Subimos el volumen de nuevo
        backgroundMusic.setVolume(0.75f);
        backgroundMusic.play();
    }

    /**
     * Este método se ejecutará cuando esta pantalla ya no este activa.
     * Utilizo este método para destruir todas las cosas que se han utilizado en el escenario.
     */
    @Override
    public void hide() {
        // Limpia el escenario. Esto eliminará a TODOS los actores del escenario y es más rápido que
        // eliminando a todos los actores uno por uno.
        stage.clear();

        // Separar todas las entidades del mundo en el que han estado.
        player.detach();
        for (entidadSuelo floor : floorList)
            floor.detach();
        for (entidadPincho spike : spikeList)
            spike.detach();

        // Limpiamos las listas
        floorList.clear();
        spikeList.clear();
    }

    /**
     * Este método se ejecuta siempre que el juego requiera renderizar esta pantalla.
     * Este método también se usa para actualizar el juego.
     */
    @Override
    public void render(float delta) {
        // Limpiamos la pantalla
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Actualizamos el escenario. Esto actualizará la velocidad del jugador.
        stage.act();

        world.step(delta, 6, 2);

        // Hacemos que la cámara siga al jugador. Mientras el jugador esté vivo, si el jugador está
        // en movimiento, hacemos que la cámara se mueva a la misma velocidad, de modo que el jugador esté siempre
        // centrado en la misma posición.
        if (player.getX() > 150 && player.isAlive()) {
            float speed = Constantes.PLAYER_SPEED * delta * Constantes.PIXELS_IN_METER;
            stage.getCamera().translate(speed, 0, 0);
        }

        // Renderizamos la pantalla
        stage.draw();
    }

    /**
     *  Este método se ejecuta cuando la pantalla se puede desechar de forma segura.
     */
    @Override
    public void dispose() {
        stage.dispose();

        world.dispose();
    }

    /**
     * Este es el contact listener que revisa si hay colisiones y contactos en el mundo
     * Utilizo este método para evaluar cuándo chocan las cosas, como cuando un jugador choca con el suelo
     */
    private class GameContactListener implements ContactListener {

        private boolean areCollided(Contact contact, Object userA, Object userB) {
            Object userDataA = contact.getFixtureA().getUserData();
            Object userDataB = contact.getFixtureB().getUserData();

            if (userDataA == null || userDataB == null) {
                return false;
            }

            return (userDataA.equals(userA) && userDataB.equals(userB)) ||
                    (userDataA.equals(userB) && userDataB.equals(userA));
        }

        /**
         * Este método se ejecuta cuando un contacto ha comenzado: cuando dos fixtures chocan.
         */
        @Override
        public void beginContact(Contact contact) {
            // El jugador ha chocado con el suelo.
            if (areCollided(contact, "player", "floor")) {
                player.setJumping(false);

                // Si aún esta tocando la pantalla, debe saltar de nuevo.
                if (Gdx.input.isTouched()) {
                    jumpSound.play();

                    player.setMustJump(true);
                }
            }

            // El jugador ha chocado con algo que mata.
            if (areCollided(contact, "player", "spike")) {

                // Comprobamos que está vivo. A veces rebotas, no queremos que se muera más de una vez.
                if (player.isAlive()) {
                    player.setAlive(false);

                    // Paramos lamusica de fondo
                    backgroundMusic.stop();
                    dieSound.play();

                    stage.addAction(
                            Actions.sequence(
                                    Actions.delay(1.5f),
                                    Actions.run(new Runnable() {

                                        @Override
                                        public void run() {
                                            game.setScreen(game.gameOverScreen);
                                        }
                                    })
                            )
                    );
                }
            }
        }

        /**
         * Este método se ejecuta cuando un contacto ha terminado: dos textures ya no chocan.
         */
        @Override
        public void endContact(Contact contact) {
            // El jugador está saltando y no está tocando el suelo.
            if (areCollided(contact, "player", "floor")) {
                if (player.isAlive()) {
                    jumpSound.play();
                }
            }
        }

        @Override public void preSolve(Contact contact, Manifold oldManifold) { }
        @Override public void postSolve(Contact contact, ContactImpulse impulse) { }
    }
}
