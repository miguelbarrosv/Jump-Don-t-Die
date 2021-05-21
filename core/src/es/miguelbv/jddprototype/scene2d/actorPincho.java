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

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class actorPincho extends Actor {

    /** Texture de los pinchos */
    private TextureRegion spikes;

    /** Velocidad que tienen los pinchos para ir hacia la izquierda.. */
    private float speed;

    /**
     * Creamos algunos pinchos
     * @param spikes
     * @param x     posicion inicial
     * @param y     posicion inicial
     * @param speed    velocidad para viajar
     */
    public actorPincho(TextureRegion spikes, float x, float y, float speed) {
        this.spikes = spikes;
        this.speed = speed;

        // Colocamos el pincho donde nos indiquen
        setPosition(x, y);
        setSize(spikes.getRegionWidth(), spikes.getRegionHeight());
    }

    @Override
    public void act(float delta) {
        // Movemos los pinchos a la izquierda infinitamente
        setX(getX() - speed * delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(spikes, getX(), getY());
    }
}
