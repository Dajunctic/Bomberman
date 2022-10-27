package uet.oop.bomberman.others;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Mobile;
import uet.oop.bomberman.game.Gameplay;

import java.util.Objects;

public class SkillFrame {

    Image BAR = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/skill_bar.png")));
    Image POINT_BAR = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/point_bar.png")));
    Image FRAME = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/skill_frame.png")));
    Image SMALL_FRAME = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/small_skill_frame.png")));
    Image CHAR_FRAME = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/character_frame.png")));

    Image Q = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/skill_Q.png")));
    Image W = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/skill_W.png")));
    Image E = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/skill_E.png")));
    Image R = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/skill_R.png")));
    Image D = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/Flash.png")));
    Image F = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/Heal.png")));

    Image GrayQ = Basic.toGrayScale(Q);
    Image GrayW = Basic.toGrayScale(W);
    Image GrayE = Basic.toGrayScale(E);
    Image GrayR = Basic.toGrayScale(R);
    Image GrayD = Basic.toGrayScale(D);
    Image GrayF = Basic.toGrayScale(F);

    Image BlueQ = Basic.toBlueScale(Q);
    Image BlueW = Basic.toBlueScale(W);
    Image BlueE = Basic.toBlueScale(E);
    Image BlueR = Basic.toBlueScale(R);
    Image BlueD = Basic.toBlueScale(D);
    Image BlueF = Basic.toBlueScale(F);


    Color[][] healthPixels = new Color[10][300];
    int currentHealth;
    int maxHealth;
    Image health;

    Color[][] manaPixels = new Color[10][300];
    int currentMana;
    int maxMana;
    Image mana;

    final double xRatio = 0.1;
    final double yRatio = 0.8;
    public SkillFrame() {
        for (int i = 0 ; i < healthPixels.length; i ++) {
            for (int j = 0 ; j < healthPixels[0].length; j++) {
                healthPixels[i][j] = new Color(0 , 0, 0 , 0);
                manaPixels[i][j] = new Color(0, 0, 0, 0);
            }
        }
    }

    public void update(Bomber player) {
        maxMana = player.getMaxMana();
        maxHealth = player.getMaxHP();

        currentHealth = player.getCurrentHP();
        currentMana = player.getCurrentMana();

        int displayHealth = currentHealth * 300 / maxHealth;
        for (int i = 0 ; i < healthPixels.length; i ++) {
            for (int j = 0 ; j < healthPixels[0].length; j++) {
                if (j < displayHealth) {
                    healthPixels[i][j] = Bar.HP_COLOR;
                } else {
                    healthPixels[i][j] = new Color(0 , 0, 0 , 0);
                }

            }
        }
        health = Bar.getMatrixImage(healthPixels);


        int displayMana = currentMana * 300 / maxMana;
        for (int i = 0 ; i < manaPixels.length; i ++) {
            for (int j = 0 ; j < manaPixels[0].length; j++) {
                if (j < displayMana) {
                    manaPixels[i][j] = Bar.MANA_COLOR;
                } else {
                    manaPixels[i][j] = new Color(0 , 0, 0 , 0);
                }
            }
        }
        mana = Bar.getMatrixImage(manaPixels);

    }

    public void render(GraphicsContext gc, Gameplay game, Bomber player) {
        double barX = 0;
        double barY = 0;
        double offsetX = game.wholeScene.getWidth() * xRatio;
        double offsetY = game.wholeScene.getHeight() * yRatio;
        gc.drawImage(BAR, barX + offsetX, barY + offsetY);
        gc.drawImage(CHAR_FRAME, barX - 40 + offsetX, barY  + 30 + offsetY);


        gc.setFont(Font.font ("Segoe UI", FontWeight.BOLD,13));
        gc.setFill(Color.ORANGE);
        gc.setTextAlign(TextAlignment.CENTER);

        Color timeLeftColor = new Color(51 / 255.0, 153 / 255.0, 255 / 255.0, 1.0);

        /* ********************** Skill Q ******************************* */
        double skillQX = 80 + barX + offsetX;
        double skillQY = barY + 48 + offsetY;
        gc.drawImage(FRAME, skillQX, skillQY);


        if (player.getCoolDownTime('Q') < 0) {
            gc.drawImage(GrayQ, skillQX + 1, skillQY + 1);

            long timeLeft = - player.getCoolDownTime('Q');
            gc.setFill(timeLeftColor);
            gc.fillText(String.valueOf(timeLeft), skillQX + 20, skillQY + 24);

        } else if (player.getCurrentMana() < Mobile.Q_MANA_CONSUMING){
            gc.drawImage(BlueQ, skillQX + 1, skillQY + 1);
        } else {
            gc.drawImage(Q, skillQX + 1, skillQY + 1);
        }

        gc.setFill(Color.ORANGE);
        gc.fillText("Q", skillQX - 2, skillQY + 45);

        /* ********************** Skill W ******************************* */
        double skillWX = 135 + barX + offsetX;
        double skillWY = barY + 48 + offsetY;
        gc.drawImage(FRAME, skillWX, skillWY);

        if (player.getCoolDownTime('W') < 0) {
            gc.drawImage(GrayW, skillWX + 1, skillWY + 1);

            long timeLeft = - player.getCoolDownTime('W');
            gc.setFill(timeLeftColor);
            gc.fillText(String.valueOf(timeLeft), skillWX + 20, skillWY + 24);

        } else if (player.getCurrentMana() < Mobile.W_MANA_CONSUMING) {
            gc.drawImage(BlueW, skillWX + 1, skillWY + 1);
        } else {
            gc.drawImage(W, skillWX + 1, skillWY + 1);
        }

        gc.setFill(Color.ORANGE);
        gc.fillText("W", skillWX - 2, skillWY + 45);

        /* ********************** Skill E ******************************* */
        double skillEX = 190 + barX + offsetX;
        double skillEY = barY + 48 + offsetY;
        gc.drawImage(FRAME, skillEX, skillEY);


        if (player.getCoolDownTime('E') < 0) {
            gc.drawImage(GrayE, skillEX + 1, skillEY + 1);

            long timeLeft = - player.getCoolDownTime('E');
            gc.setFill(timeLeftColor);
            gc.fillText(String.valueOf(timeLeft), skillEX + 20, skillEY + 24);

        } else if (player.getCurrentMana() < Mobile.E_MANA_CONSUMING) {
            gc.drawImage(BlueE, skillEX + 1, skillEY + 1);
        } else {
            gc.drawImage(E, skillEX + 1, skillEY + 1);
        }

        gc.setFill(Color.ORANGE);
        gc.fillText("E", skillEX - 2, skillEY + 45);

        /* ********************** Skill R ******************************* */
        double skillRX = 245 + barX + offsetX;
        double skillRY = barY + 48 + offsetY;
        gc.drawImage(FRAME, skillRX, skillRY);

        if (player.getCoolDownTime('R') < 0) {
            gc.drawImage(GrayR, skillRX + 1, skillRY + 1);

            long timeLeft = - player.getCoolDownTime('R');
            gc.setFill(timeLeftColor);
            gc.fillText(String.valueOf(timeLeft), skillRX + 20, skillRY + 24);

        } else if (player.getCurrentMana() < Mobile.W_MANA_CONSUMING) {
            gc.drawImage(BlueR, skillRX + 1, skillRY + 1);
        } else {
            gc.drawImage(R, skillRX + 1, skillRY + 1);
        }

        gc.setFill(Color.ORANGE);
        gc.fillText("R", skillRX - 2, skillRY + 45);

        /* ********************** Skill D ******************************* */
        double skillDX = 315 + barX + offsetX;
        double skillDY = barY + 48 + offsetY;
        gc.drawImage(SMALL_FRAME, skillDX, skillDY);
        gc.drawImage(D, skillDX + 1, skillDY + 1);

        if (player.getCoolDownTime('D') < 0) {
            gc.drawImage(GrayD, skillDX + 1, skillDY + 1);

            long timeLeft = - player.getCoolDownTime('D');
            gc.setFill(timeLeftColor);
            gc.fillText(String.valueOf(timeLeft), skillDX + 14, skillDY + 17);
        } else {
            gc.drawImage(D, skillDX + 1, skillDY + 1);
        }

        gc.setFill(Color.ORANGE);
        gc.fillText("D", skillDX - 2, skillDY + 35);

        /* ********************** Skill F ******************************* */
        double skillFX = 350 + barX + offsetX;
        double skillFY = barY + 48 + offsetY;
        gc.drawImage(SMALL_FRAME, skillFX, skillFY);

        if (player.getCoolDownTime('F') < 0) {
            gc.drawImage(GrayF, skillFX + 1, skillFY + 1);

            long timeLeft = - player.getCoolDownTime('F');
            gc.setFill(timeLeftColor);
            gc.fillText(String.valueOf(timeLeft), skillFX + 14, skillFY + 17);
        } else {
            gc.drawImage(F, skillFX + 1, skillFY + 1);
        }

        gc.setFill(Color.ORANGE);
        gc.fillText("F", skillFX - 2, skillFY + 35);

        /* ********************** Health Point ******************************* */
        double pointBarX = 80 + barX + offsetX;
        double pointBarY = 100 + barY + offsetY;
        gc.drawImage(POINT_BAR, pointBarX, pointBarY);
        gc.drawImage(health, pointBarX + 2, pointBarY);

        gc.setFont(Font.font ("Segoe UI", FontWeight.BOLD, 9));
        gc.setFill(Color.WHITE);

        gc.fillText(currentHealth + " / " + maxHealth, pointBarX + 150, pointBarY + 8);

        if (currentHealth < maxHealth) {
            gc.fillText("+" + Mobile.HP_RECOVER_PER_SECOND, pointBarX + 290, pointBarY + 8 );
        }

        /* ********************** Mana Point ******************************* */
        gc.drawImage(POINT_BAR, pointBarX, pointBarY + 12);
        gc.drawImage(mana, pointBarX + 2, pointBarY + 12);

        gc.fillText(currentMana + " / " + maxMana, pointBarX + 150, pointBarY + 20 );

        if (currentMana < maxMana) {
            gc.fillText("+" + Mobile.MANA_RECOVER_PER_SECOND, pointBarX + 290, pointBarY + 20);
        }
    }


}
