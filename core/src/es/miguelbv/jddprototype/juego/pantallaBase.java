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

import com.badlogic.gdx.Screen;

public abstract class pantallaBase implements Screen {

    /**
     * Instancia del juego.
     */
    protected MainGame game;

    public pantallaBase(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Este método se invoca cuando se muestra una pantalla.
    }

    @Override
    public void render(float delta) {
        // Este método se invoca cuando una pantalla debe renderizarse en un frame.
        // delta es la cantidad de segundos (del orden de 0,01 aproximadamente) entre este y el último fotograma.
    }

    @Override
    public void resize(int width, int height) {
        //Este método se invoca cuando se cambia el tamaño del juego (desktop).
    }

    @Override
    public void pause() {
        // Este método se invoca cuando el juego está en pausa.
    }

    @Override
    public void resume() {
        // Este método se invoca cuando se reanuda el juego.
    }

    @Override
    public void hide() {
        // Este método se invoca cuando la pantalla ya no se muestra.
    }

    @Override
    public void dispose() {
        // Este método se invoca cuando se cierra el juego.
    }
}
