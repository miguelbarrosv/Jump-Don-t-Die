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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import es.miguelbv.jddprototype.juego.Constantes;

public class entidadSuelo extends Actor {

    /** Las texturas que usamos para exhibir el piso */
    private Texture floor, overfloor;

    /** La instancia del mundo en el que estará el suelo */
    private World world;

    /** Los cuerpos del suelo, primero el del cuerpo principal y el otro el del borde izquierdo */
    private Body body, leftBody;

    /** Los fixtures asignados a ambos cuerpos. Esto le da forma a los cuerpos */
    private Fixture fixture, leftFixture;

    /**
     * Create a new floor
     *
     * @param world
     * @param floor
     * @param overfloor
     * @param x  borde izquierdo para el suelo
     * @param width  que ancho es el suelo
     * @param y  top border for the suelo
     */
    public entidadSuelo(World world, Texture floor, Texture overfloor, float x, float width, float y) {
        this.world = world;
        this.floor = floor;
        this.overfloor = overfloor;

        // Creamos el cuerpo del suelo.
        BodyDef def = new BodyDef();                // (1) Definicion del cuerpo
        def.position.set(x + width / 2, y - 0.5f);  // (2) Centramos el suelo en las coordenadas dadas
        body = world.createBody(def);               // (3) Creamos el suelo

        // Le damos forma de caja.
        PolygonShape box = new PolygonShape();      // (1) Creamos la forma del polígono.
        box.setAsBox(width / 2, 0.5f);              // (2) Le damos un tamaño
        fixture = body.createFixture(box, 1);       // (3) Creamos el fixture
        fixture.setUserData("floor");               // (4) Configuramos los datos del usuario para el dispositivo.
        box.dispose();                              // (5) Desechamosla forma

        // Ahora creamos el cuerpo izquierdo. Este cuerpo es puntiagudo, si usuario gplpea este cuerpo, muere. Es
        // en el borde izquierdo del suelo y solo se usa cuando tiene que saltar unas escaleras.
        // Funciona igual que el anterior.
        BodyDef leftDef = new BodyDef();
        leftDef.position.set(x, y - 0.55f);
        leftBody = world.createBody(leftDef);

        // Así como el fixture. Usamos los datos de usuario del pincho para que actúe como un enemigo.
        PolygonShape leftBox = new PolygonShape();
        leftBox.setAsBox(0.02f, 0.45f);
        leftFixture = leftBody.createFixture(leftBox, 1);
        leftFixture.setUserData("spike");
        leftBox.dispose();

        // Ahora colocamos al actor en el escenario convirtiendo las coordenadas dadas en metros a px.
        setSize(width * Constantes.PIXELS_IN_METER, Constantes.PIXELS_IN_METER);
        setPosition(x * Constantes.PIXELS_IN_METER, (y - 1) * Constantes.PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Renderizamos ambas texturas.
        batch.draw(floor, getX(), getY(), getWidth(), getHeight());
        batch.draw(overfloor, getX(), getY() + 0.9f * getHeight(), getWidth(), 0.1f * getHeight());
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
