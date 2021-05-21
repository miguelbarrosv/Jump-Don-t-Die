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

package es.miguelbv.jddprototype.juego.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import es.miguelbv.jddprototype.juego.Constantes;

public class entidadJugador extends Actor {

    /** Textura del jugador. */
    private Texture texture;

    /** Instancia del mundo donde esta el jugador. */
    private World world;

    /** El cuerpo del jugador. */
    private Body body;

    /** El fixture el jugador. */
    private Fixture fixture;


    private boolean alive = true;

    private boolean jumping = false;

    private boolean mustJump = false;

    public entidadJugador(World world, Texture texture, Vector2 position) {
        this.world = world;
        this.texture = texture;

        // Creamos elk cuerpo del jugador
        BodyDef def = new BodyDef();                // (1) Creamos la definicion del body.
        def.position.set(position);                 // (2) Ponemos el body en la posiicon inicial.
        def.type = BodyDef.BodyType.DynamicBody;    // (3) Lo ponemos dinamico.
        body = world.createBody(def);               // (4) Creamos el body.

        // Le damos forma
        PolygonShape box = new PolygonShape();      // (1) Creamos el shape.
        box.setAsBox(0.5f, 0.5f);                   // (2) caja de 1x1.
        fixture = body.createFixture(box, 3);       // (3) Creamos la fixture.
        fixture.setUserData("player");              // (4) Establecer los datos del usuario.
        box.dispose();                              // (5) Desechamos la forma.

        // Establecemos el tamaño en un valor que sea lo suficientemente grande para mostrarse en la pantalla.
        setSize(Constantes.PIXELS_IN_METER, Constantes.PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Actualizamos siempre la posición del actor cuando lo dibujamos, para que
        // la posición del actor en la pantalla sea lo más precisa posible a la posición actual
        //  del cuerpo Box2D.
        setPosition((body.getPosition().x - 0.5f) * Constantes.PIXELS_IN_METER,
                    (body.getPosition().y - 0.5f) * Constantes.PIXELS_IN_METER);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        // Salta cuando se toca la pantalla
        if (Gdx.input.justTouched()) {
            jump();
        }

        // Salta si tuviéramos que saltar durante una colisión.
        if (mustJump) {
            mustJump = false;
            jump();
        }

        // Si el jugador está vivo, cambia la velocidad.
        if (alive) {
            float speedY = body.getLinearVelocity().y;
            body.setLinearVelocity(Constantes.PLAYER_SPEED, speedY);
        }

        // Si el jugador está saltando, aplicamos una fuerza opuesta para que el jugador caiga más rápido.
        if (jumping) {
            body.applyForceToCenter(0, -Constantes.IMPULSE_JUMP * 1.15f, true);
        }
    }

    public void jump() {
        if (!jumping && alive) {
            jumping = true;

            // Aplicamos un impulso al jugador. Esto hará que la velocidad cambie
            // en este momento, a diferencia del uso de fuerzas, que cambia gradualmente la fuerza utilizada
            // durante el salto.
            Vector2 position = body.getPosition();
            body.applyLinearImpulse(0, Constantes.IMPULSE_JUMP, position.x, position.y, true);
        }
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public void setMustJump(boolean mustJump) {
        this.mustJump = mustJump;
    }
}
