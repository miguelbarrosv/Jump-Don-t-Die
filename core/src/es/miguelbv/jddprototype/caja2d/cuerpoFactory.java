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

import com.badlogic.gdx.physics.box2d.BodyDef;

public class cuerpoFactory {

    public static BodyDef createPlayer() {
        BodyDef def = new BodyDef();
        def.position.set(0, 0.5f);

        // Necesitamos hacerlo dinámico para que pueda moverse
        // y ser afectado por fuerzas. Los cuerpos estáticos (este es el valor predeterminado) no reaccionan a las fuerzas
        // aunque todavía se usan en contactos.
        def.type = BodyDef.BodyType.DynamicBody;
        return def;
    }

    public static BodyDef createSpikes(float x) {
        // Le damos a los pinchos la posición que desea el usuario. Verticalmente siempre se coloca en
        // 0,5 metros. Porque Los pinchos miden 1 metro de altura y porque la posición es siempre
        // dado en términos de medio ancho y medio alto, esto hará que la base de los picos
        // esté en el suelo.
        BodyDef def = new BodyDef();
        def.position.set(x, 0.5f);
        return def;
    }

    public static BodyDef createFloor() {
        BodyDef def = new BodyDef();
        def.position.set(0, -1);
        return def;
    }
}
