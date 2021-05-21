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

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * This class creates entities using Factory Methods.
 */
public class entidadFactory {

    private AssetManager manager;

    /**
     * Creamos una nueva entidad llamada entidadFactory
     * @param manager
     */
    public entidadFactory(AssetManager manager) {
        this.manager = manager;
    }

    /**
     * Creamos un jugador usando la textura predeterminada.
     * @param world     mundo en el que estara el jugador
     * @param position  posición inicial del jugador en el mundo.
     * @return          el jugador.
     */
    public entidadJugador createPlayer(World world, Vector2 position) {
        Texture playerTexture = manager.get("jugador.png");
        return new entidadJugador(world, playerTexture, position);
    }

    /**
     * Creamos el suelo utilizando el conjunto de texturas predeterminado.
     * @param world     mundo donde estará el suelo.
     * @param x         posición horizontal para los pinchos en el mundo.
     * @param width     ancho para el suelo
     * @param y         posición vertical para la parte superior de el suelo
     * @return          el suelo.
     */
    public entidadSuelo createFloor(World world, float x, float width, float y) {
        Texture floorTexture = manager.get("suelo.png");
        Texture overfloorTexture = manager.get("suelo2.png");
        return new entidadSuelo(world, floorTexture, overfloorTexture, x, width, y);
    }

    /**
     * Creamos los pinchos usando la textura predeterminada.
     * @param world     mundo donde estarán los pinchos.
     * @param x         posición horizontal para los pinchos en el mundo.
     * @param y         posición vertical de la base de los pinchos en el mundo.
     * @return          los pinchos
     */
    public entidadPincho createSpikes(World world, float x, float y) {
        Texture spikeTexture = manager.get("pincho.png");
        return new entidadPincho(world, spikeTexture, x, y);
    }

}
