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


public class entidadPincho extends Actor {

    /** Texturas de los pinchos. */
    private Texture texture;

    /** El mundo donde estaran los pinchos. */
    private World world;

    /** El cuerpo asignado a los pinchos. */
    private Body body;

    /** El fixture asignado a los pinchos. */
    private Fixture fixture;

    /**
     * Creamos variaos pinchos
     *
     * @param world
     * @param texture
     * @param x  posición horizontal para el centro del pincho
     * @param y  posición vertical de la base del pincho
     */
    public entidadPincho(World world, Texture texture, float x, float y) {
        this.world = world;
        this.texture = texture;

        // Creamos el cuerpo
        BodyDef def = new BodyDef();                // (1) Le damos una definicion
        def.position.set(x, y + 0.5f);              // (2) Posicionamos el cuerpo en el mundo
        body = world.createBody(def);               // (3) Creamos el cuerpo

        // Le damos forma
        PolygonShape box = new PolygonShape();      // (1) Hacemos un polígono
        Vector2[] vertices = new Vector2[3];        // (2) Sin embargo, los vértices se agregarán manualmente.
        vertices[0] = new Vector2(-0.5f, -0.5f);    // (3) Añadimos los vértices de un triángulo.
        vertices[1] = new Vector2(0.5f, -0.5f);
        vertices[2] = new Vector2(0, 0.5f);
        box.set(vertices);                          // (4) Les damos forma
        fixture = body.createFixture(box, 1);       // (5) Creamos el fixture.
        fixture.setUserData("spike");               // (6) Establecemos los datos del usuario
        box.dispose();                              // (7) Desechamos el pincho cuando no lo necesitamos

        // Posicionamos al actor en la pantalla convirtiendo los metros a píxeles.
        setPosition((x - 0.5f) * Constantes.PIXELS_IN_METER, y * Constantes.PIXELS_IN_METER);
        setSize(Constantes.PIXELS_IN_METER, Constantes.PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

}
