package uet.oop.bomberman.others;


import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import uet.oop.bomberman.entities.MenuSprite;
import uet.oop.bomberman.game.BombermanGame;
import uet.oop.bomberman.game.Gameplay;
import uet.oop.bomberman.graphics.Sprite;

import javafx.scene.control.TextField;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Color.*;
import static uet.oop.bomberman.game.BombermanGame.gc;
import static uet.oop.bomberman.game.BombermanGame.scene;
import static uet.oop.bomberman.graphics.Sprite.*;

public class Menu {

    protected Scene play;
    protected Scene menu_;
    protected Scene setting;
    protected Scene highscore;
    protected Scene language;

    protected static Point mouse_point=new Point();

    public enum Menu_Switch{
        MENU,PLAY, HIGHSCORE, SETTING, PAUSE,QUIT
    };
    // khoi tao cac entity MenuSprite
    public static MenuSprite menuBackground = new MenuSprite(0,0, Sprite.menu.getFxImage());
    public static MenuSprite play_button_red=new MenuSprite(8,3, Sprite.play_red.getFxImage());
    public static MenuSprite play_button_black=new MenuSprite(8,3, Sprite.play_black.getFxImage());

    public static MenuSprite continue_button_red=new MenuSprite(8,5, Sprite.continue_red.getFxImage());
    public static MenuSprite continue_button_black=new MenuSprite(8,5, Sprite.continue_black.getFxImage());

    public static MenuSprite setting_button_black=new MenuSprite(8,7, Sprite.setting_black.getFxImage());
    public static MenuSprite setting_button_red=new MenuSprite(8,7, Sprite.setting_red.getFxImage());

    public static MenuSprite exit_button_black=new MenuSprite(8,9, Sprite.quit_black.getFxImage());
    public static MenuSprite exit_button_red=new MenuSprite(8,9, Sprite.quit_red.getFxImage());

    public static MenuSprite back_button_black=new MenuSprite(14,10, Sprite.back_black.getFxImage());
    public static MenuSprite back_button_red=new MenuSprite(14,10, Sprite.back_red.getFxImage());


    public static MenuSprite pause_button_black=new MenuSprite(17,0, Sprite.pause_button_black.getFxImage());
    public static MenuSprite pause_button_red=new MenuSprite(17,0, Sprite.pause_button_red.getFxImage());

    public static MenuSprite playmini_button_black=new MenuSprite(9,7, Sprite.play_button_black.getFxImage());
    public static MenuSprite playmini_button_red=new MenuSprite(9,7, Sprite.play_button_red.getFxImage());

    public static MenuSprite music_button_black=new MenuSprite(3,0, Sprite.music_button_black.getFxImage());
    public static MenuSprite music_button_red=new MenuSprite(3,0, Sprite.music_button_red.getFxImage());
    // switch cac trang thai
    public void update(Gameplay gameplay){
        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mouse_point.setLocation(mouseEvent.getX(),mouseEvent.getY());
            }
        });

        //render them cac chữ và ô trong từng menu
        switch (Gameplay.state){
            case "MENU": {this.renderMenu(gc);
            break;}
            case "HIGHSCORE": {this.renderHighscore(gc);
            break;}
            case "SETTING": {this.renderSetting(gc);
            break;}
            case "PAUSE": {this.renderPause(gc);
            break;}
        }

    }
    public void renderMenu(GraphicsContext gc){
        gc.clearRect(0,0,980,672);

        //ve anh nen
        gc.drawImage(menuBackground.getImg(),0,0,menuBackground.getWidth(),menuBackground.getHeight());

        //ve cac button
        if(Physics.collisionPointToRect(mouse_point,play_button_red.getRect())) {
            play_button_red.render(gc, play_button_red.getX(), play_button_red.getY());
        }
        else play_button_black.render(gc, play_button_black.getX(), play_button_black.getY());
        if(Physics.collisionPointToRect(mouse_point,setting_button_red.getRect())) {
            setting_button_red.render(gc, setting_button_red.getX(), setting_button_red.getY());
        }
        else setting_button_black.render(gc, setting_button_black.getX(), setting_button_black.getY());

        if(Physics.collisionPointToRect(mouse_point,exit_button_red.getRect())) {
            exit_button_red.render(gc, exit_button_red.getX(), exit_button_red.getY());
        }
        else exit_button_black.render(gc, exit_button_black.getX(), exit_button_black.getY());

        if(Physics.collisionPointToRect(mouse_point,continue_button_red.getRect())) {
            continue_button_red.render(gc, continue_button_red.getX(), continue_button_red.getY());
        }
        else continue_button_black.render(gc, continue_button_black.getX(), continue_button_black.getY());


        //xu li chuot
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(Physics.collisionPointToRect(mouse_point,play_button_red.getRect()))
                    Gameplay.state=String.valueOf(Menu_Switch.PLAY);
                if(Physics.collisionPointToRect(mouse_point,continue_button_red.getRect()))
                    Gameplay.state=String.valueOf(Menu_Switch.PLAY);
                if(Physics.collisionPointToRect(mouse_point,setting_button_red.getRect()))
                    Gameplay.state=String.valueOf(Menu_Switch.SETTING);
                if(Physics.collisionPointToRect(mouse_point,exit_button_red.getRect()))
                    Gameplay.state=String.valueOf(Menu_Switch.QUIT);
            }
        });

    }
    public void renderHighscore(GraphicsContext gc){
        gc.clearRect(0,0,980,672);

        // ve anh nen
        gc.drawImage(menuBackground.getImg(),0,0,menuBackground.getWidth(),menuBackground.getHeight());
        //ve cac button
        if(Physics.collisionPointToRect(mouse_point,back_button_red.getRect())) {
            back_button_red.render(gc, back_button_red.getX(), back_button_red.getY());
        }
        else back_button_black.render(gc, back_button_black.getX(), back_button_black.getY());
        //xu ly chuot
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(Physics.collisionPointToRect(mouse_point,back_button_red.getRect()))
                    Gameplay.state=String.valueOf(Menu_Switch.MENU);
            }
        });

    }
    public void renderSetting(GraphicsContext gc){
        gc.clearRect(0,0,980,672);

        // ve anh nen
        gc.drawImage(menuBackground.getImg(),0,0,menuBackground.getWidth(),menuBackground.getHeight());
        //ve cac button
        if(Physics.collisionPointToRect(mouse_point,back_button_red.getRect())) {
            back_button_red.render(gc, back_button_red.getX(), back_button_red.getY());
        }
        else back_button_black.render(gc, back_button_black.getX(), back_button_black.getY());
        //xu ly chuot
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(Physics.collisionPointToRect(mouse_point,back_button_red.getRect()))
                    Gameplay.state=String.valueOf(Menu_Switch.MENU);
            }
        });

    }
    public void renderPause(GraphicsContext gc){
        gc.clearRect(0,0,980,672);
        if(Physics.collisionPointToRect(mouse_point,playmini_button_red.getRect())) {
            playmini_button_red.render(gc, playmini_button_red.getX(), playmini_button_red.getY());
        }
        else playmini_button_black.render(gc, playmini_button_black.getX(), playmini_button_black.getY());

        if(Physics.collisionPointToRect(mouse_point,back_button_red.getRect())) {
            back_button_red.render(gc, back_button_red.getX(), back_button_red.getY());
        }
        else back_button_black.render(gc, back_button_black.getX(), back_button_black.getY());
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(Physics.collisionPointToRect(mouse_point,playmini_button_red.getRect()))
                    Gameplay.state=String.valueOf(Menu_Switch.PLAY);
                if(Physics.collisionPointToRect(mouse_point,back_button_red.getRect()))
                    Gameplay.state=String.valueOf(Menu_Switch.MENU);
            }
        });

    }
    public static void renderPlay(GraphicsContext gc){
        if(Physics.collisionPointToRect(mouse_point,pause_button_red.getRect())) {
            pause_button_red.render(gc, pause_button_red.getX(), pause_button_red.getY());
        }
        else pause_button_black.render(gc, pause_button_black.getX(), pause_button_black.getY());
        if(Physics.collisionPointToRect(mouse_point,music_button_red.getRect())) {
            music_button_red.render(gc, music_button_red.getX(), music_button_red.getY());
        }
        else music_button_black.render(gc, music_button_black.getX(), music_button_black.getY());
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(Physics.collisionPointToRect(mouse_point,pause_button_red.getRect()))
                    Gameplay.state=String.valueOf(Menu_Switch.PAUSE);
            }
        });
    }

}
