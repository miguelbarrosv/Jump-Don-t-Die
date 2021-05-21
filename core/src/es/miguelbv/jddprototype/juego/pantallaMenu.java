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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class pantallaMenu extends pantallaBase {

    /** El escenario donde aladiremos los botones */
    private Stage stage;

    private Skin skin;

    /** El logo que pondremos en la aplicación. */
    private Image logo;

    /** El boton play para ir a la pantalla de juego */
    private TextButton play,exit;

    public pantallaMenu(final MainGame game) {
        super(game);

        // Creamos un escenario.
        stage = new Stage(new FitViewport(640, 360));

        // Cargamos el fichero skin.
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        // Creamos el boton play y exit
        play = new TextButton("Jugar", skin);
        exit = new TextButton("Salir",skin);

        // Tambien creamos el logo
        logo = new Image(game.getManager().get("logo.png", Texture.class));

        // Agregamos capture listeners. Los capture listeners tienen un método, modificado, que se ejecuta
        // cuando se presiona el botón o cuando el usuario interactúa de alguna manera con el widget.
        play.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // nos lleva a la pantalla de juego
                game.setScreen(game.gameScreen);
            }
        });

        exit.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                System.exit(0);
            }
        });

        // Colocamos las cosas en la pantalla a nuestro gusto.
        logo.setPosition(440 - logo.getWidth() / 2, 320 - logo.getHeight());
        play.setSize(200, 80);
        play.setPosition(40, 170);
        exit.setSize(200, 80);
        exit.setPosition(40, 60);

        // Agregamos los actores al escenario si no no veriamos nada
        stage.addActor(play);
        stage.addActor(logo);
        stage.addActor(exit);
    }

    @Override
    public void show() {
        // Si queremos poder hacer clic en el botón, tenemos que escribir lo siguiente
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        // Cuando la pantalla ya no es visible tenemos que ponerlo a null
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.5f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }
}
