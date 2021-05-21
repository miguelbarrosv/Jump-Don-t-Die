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

public class Constantes {

    /**
     * Cuántos píxeles hay en un metro. tenemos que convertir estos metros a píxeles para que
     * pueden ser renderizados a un tamaño visible por el usuario.
     */
    public static final float PIXELS_IN_METER = 90f;

    /**
     * La fuerza en que usa el jugador para saltar en un impulso. Esta fuerza también se aplicará
     * en la dirección opuesta para hacer que el jugador caiga más rápido.
     */
    public static final int IMPULSE_JUMP = 20;

    /**
     * Esta es la velocidad que tiene el jugador. Cuanto mayor sea este valor, más rápido ira el jugador.
     * Si hacemos este valor más alto tendremos que aumentar la distancia entre obstñáculos.
     */
    public static final float PLAYER_SPEED = 8f;
}
