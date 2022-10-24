package uet.oop.bomberman.game;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;

public class KeySet {
    public static ArrayList<ArrayList<KeyCode>> keySet = new ArrayList<>();
    static {
        keySet.add(new ArrayList<>());
        keySet.get(0).add(KeyCode.getKeyCode("A"));
    }
}
