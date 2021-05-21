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

package es.miguelbv.jddprototype.caja2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import es.miguelbv.jddprototype.juego.pantallaBase;

public class cajaPantalla2D extends pantallaBase {

    public cajaPantalla2D(es.miguelbv.jddprototype.juego.MainGame game) {
        super(game);
    }

    /** Instancia de World. Todo en Box2D debe agregarse al mundo. */
    private World world;

    /** Procesador de depuración. Muestra mundos en la pantalla para que sea posible verlos. */
    private Box2DDebugRenderer renderer;

    /** Cámara. Tenemos que crear una cámara para decirle al renderizador cómo dibujar el mundo. */
    private OrthographicCamera camera;

    private Body cuerpoJugador, cuerpoSuelo, cuerpoPincho;

    private Fixture jugadorFixture, sueloFixture, pinchoFixture;

    private boolean mustJumpp, isJumping, isAlive = true;

    @Override
    public void show() {
        // Crea el mundo. Le damos una gravedad similar a la de la tierra.
        // Box2D, trata las coordenadas Y hacia arriba. Por lo tanto,
        // al usar y = -10, hacemos que la gravedad baje.

        world = new World(new Vector2(0, -10), true);

        // Creamos un renderizador y una cámara para que podamos ver lo que hay en el mundo.
        renderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(16, 9);
        camera.translate(0, 1);

        // Creamos los cuerpos de las entidades de este mundo.
        cuerpoJugador = world.createBody(cuerpoFactory.createPlayer());
        cuerpoSuelo = world.createBody(cuerpoFactory.createFloor());
        cuerpoPincho = world.createBody(cuerpoFactory.createSpikes(6f));

        // Creamos el ficture para las entidades en este mundo.
        jugadorFixture = objetosFactory.createPlayerFixture(cuerpoJugador);
        sueloFixture = objetosFactory.createFloorFixture(cuerpoSuelo);
        pinchoFixture = objetosFactory.createSpikeFixture(cuerpoPincho);

        // Configuramos los datos del usuario en algunas categorías que nos permitirán manejar colisiones mejor.
        // El jugador puede chocar con el suelo y con un pincho.
        jugadorFixture.setUserData("player");
        sueloFixture.setUserData("floor");
        pinchoFixture.setUserData("spike");

        // Configuramos el contact listener para el mundo. El contact listener manejará los contactos.
        world.setContactListener(new Box2DScreenContactListener());
    }

    @Override
    public void dispose() {
        // Destruimos todos los fixture de sus cuerpos.
        cuerpoSuelo.destroyFixture(sueloFixture);
        cuerpoJugador.destroyFixture(jugadorFixture);
        cuerpoPincho.destroyFixture(pinchoFixture);

        // Destruimos todos los body del mundo.
        world.destroyBody(cuerpoJugador);
        world.destroyBody(cuerpoSuelo);
        world.destroyBody(cuerpoPincho);

        // Desechamos todas las cosas.
        world.dispose();
        renderer.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // El jugador debe saltar si la pantalla ha sido tocada y si aún no está saltando.
        // Al establecer debeSaltar en verdadero, comenzará a saltar en el siguiente frame..
        if (Gdx.input.justTouched() && !isJumping) {
            mustJumpp = true;
        }

        // Si el jugador debe saltar, entonces salta.
        if (mustJumpp) {
            mustJumpp = false;
            makePlayerJump();
        }

        // Sigue moviendo al jugador mientras está vivo.
        if (isAlive) {
            float velocidadY = cuerpoJugador.getLinearVelocity().y;
            cuerpoJugador.setLinearVelocity(8, velocidadY);
        }

        // Iteramos el mundo
        world.step(delta, 6, 2);

        // Rendeerizamos el mundo
        camera.update();
        renderer.render(world, camera.combined);
    }

    private void makePlayerJump() {
        // Aplicamos un impulso para cambiar la velocidad del cuerpo en ese momento.
        Vector2 position = cuerpoJugador.getPosition();
        cuerpoJugador.applyLinearImpulse(0, 20, position.x, position.y, true);
    }

    /**
     * Este es el contact listener que utilizo para manejar las colisiones.
     */
    private class Box2DScreenContactListener implements ContactListener {
        @Override
        public void beginContact(Contact contact) {
            // Cogemos las fixtures.
            Fixture fixtureA = contact.getFixtureA(), fixtureB = contact.getFixtureB();

            // Verificación de seguridad. Si no hay datos del usuario, no entramos en esta colisión.
            if (fixtureA.getUserData() == null || fixtureB.getUserData() == null) {
                return;
            }

            if ((fixtureA.getUserData().equals("player") && fixtureB.getUserData().equals("floor")) ||
                    (fixtureA.getUserData().equals("floor") && fixtureB.getUserData().equals("player"))) {
                // Así que el jugador y el suelo han colisionado.

                if (Gdx.input.isTouched()) {
                    // Si aún se toca la pantalla, salta de nuevo.
                    mustJumpp = true;
                }

                // Deja de saltar.
                isJumping = false;
            }

            if ((fixtureA.getUserData().equals("player") && fixtureB.getUserData().equals("spike")) ||
                    (fixtureA.getUserData().equals("spike") && fixtureB.getUserData().equals("player"))) {
                // El pincho y el jugador han chocado. Insta-muerte.
                isAlive = false;
            }
        }

        @Override
        public void endContact(Contact contact) {
            // Acaba la colisión
            Fixture fixtureA = contact.getFixtureA(), fixtureB = contact.getFixtureB();

            if (fixtureA == jugadorFixture && fixtureB == sueloFixture) {
                isJumping = true;
            }

            if (fixtureA == sueloFixture && fixtureB == jugadorFixture) {
                isJumping = true;
            }
        }

        @Override public void preSolve(Contact contact, Manifold oldManifold) { }
        @Override public void postSolve(Contact contact, ContactImpulse impulse) { }
    }
}
