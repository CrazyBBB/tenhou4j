package tenhouvisualizer;

import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.scene.paint.*;


public class BoardControl extends Canvas {
    private GraphicsContext gc;

    private Image[] img_nt = new Image[37];
    private Image[] img_ny = new Image[37];
    private Image[] img_dt = new Image[37];
    private Image[] img_dy = new Image[37];
    private Image imgUra;
    public void drawScene(Scene scene) {
        init(scene);
    }

    public BoardControl() {
        for (int i = 0; i < 37; i++) {
            img_nt[i] = new Image("/img_nt/" + i + ".png");
            img_ny[i] = new Image("/img_ny/" + i + ".png");
            img_dt[i] = new Image("/img_dt/" + i + ".png");
            img_dy[i] = new Image("/img_dy/" + i + ".png");
        }
        imgUra = new Image("/img_nt/ura.png");

        gc = this.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, this.getWidth(), this.getHeight());
        gc.setFill(Color.valueOf("#cccccc"));
        gc.fillRect(200, 200, 200, 200);
    }

    private void init(Scene scene) {
        //label.setText(scene.toString());

        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        gc.setFill(javafx.scene.paint.Color.GREEN);
        gc.fillRect(0, 0, this.getWidth(), this.getHeight());
        gc.setFill(javafx.scene.paint.Color.valueOf("#cccccc"));
        gc.fillRect(200, 200, 200, 200);

        for (int i = 0; i < 4; i++) {
            int drawnPlayerId = (i + scene.playerId) % 4;

            if (drawnPlayerId != 3 || !scene.isSanma) {
                gc.setFill(javafx.scene.paint.Color.RED);
                gc.setFont(javafx.scene.text.Font.font(15));
                gc.fillText(scene.getZikaze(drawnPlayerId) + " " + String.valueOf(scene.point[drawnPlayerId]), 200, 368);

                gc.setFill(javafx.scene.paint.Color.BLACK);
                gc.fillText(scene.dan[drawnPlayerId] + "R" + scene.rate[drawnPlayerId], 200, 383);

                gc.setFont(javafx.scene.text.Font.font("MS Mincho", 15));
                gc.fillText(scene.players[drawnPlayerId], 200, 398);
                draw(scene, drawnPlayerId);
            }
            rotate();
        }

        gc.setFill(javafx.scene.paint.Color.BLACK);
        gc.setFont(javafx.scene.text.Font.font("MS Mincho", 24));
        gc.fillText(scene.getBaStr(), 240, 290);

        gc.drawImage(imgUra, 240, 300, 20, 29);
        for (int i = 0; i < 4; i++) {
            if (i < scene.dora.size()) {
                gc.drawImage(getImage(scene.dora.get(i), true, true), 260 + 20 * i, 300, 20, 29);
            } else {
                gc.drawImage(imgUra, 260 + 20 * i, 300, 20, 29);
            }
        }
        gc.drawImage(imgUra, 340, 300, 20, 29);
    }

    private void draw(Scene scene, int playerId) {
        drawTehai(scene, playerId);
        drawDahai(scene, playerId);
        drawNaki(scene, playerId);
    }

    private void drawTehai(Scene scene, int playerId) {
        int x = 70;
        int y = 555;
        for (int hai : scene.stehai[playerId]) {
            gc.drawImage(getImage(hai, true, true), x, y);
            x += 32;
        }
    }

    private void drawDahai(Scene scene, int playerId) {
        int x = 200;
        int y = 400;
        int i = 0;
        for (int hai : scene.dahai[playerId]) {
            if (i == scene.reach[playerId]) {
                gc.drawImage(getImage(hai, scene.tedashi[playerId].get(i), false), x, y + 13);
                x += 45;
            } else {
                gc.drawImage(getImage(hai, scene.tedashi[playerId].get(i), true), x, y);
                x += 32;
            }


            if (i == 5 || i == 11) {
                x = 200;
                y += 45;
            }

            i++;
        }
    }

    private void drawNaki(Scene scene, int playerId) {
        int x = 600;
        int y = 555;

        int nOfKita = scene.kita[playerId];
        if (nOfKita > 0) {
            x -= 32;

            gc.drawImage(img_nt[30], x, y);
            gc.setFill(javafx.scene.paint.Color.WHITE);
            gc.setFont(javafx.scene.text.Font.font(15));
            gc.fillText("×" + nOfKita, 577, 555);
        }

        for (Naki naki : scene.naki[playerId]) {
            if (naki.type == 0 || naki.type == 1) {
                for (int i = 2; i >= 0; i--) {
                    if (i == naki.nakiIdx) {
                        x -= 45;
                        gc.drawImage(getImage(naki.hai[i], true, false), x, y + 13);
                    } else {
                        x -= 32;
                        gc.drawImage(getImage(naki.hai[i], true, true), x, y);
                    }
                }
            } else if (naki.type == 2) {
                for (int i = 3; i >= 0; i--) {
                    x -= 32;
                    if (i == 0 || i == 3) {
                        gc.drawImage(imgUra, x, y);
                    } else {
                        gc.drawImage(getImage(naki.hai[i], true, true), x, y);
                    }
                }
            }
        }
    }

    private javafx.scene.image.Image getImage(int hai, boolean normal, boolean tate) {
        int haiId;
        if (hai == 16 || hai == 52 || hai == 88) {
            haiId = (hai - 16) / 36 + 34;
        } else {
            haiId = hai / 4;
        }

        if (normal) {
            if (tate) {
                return img_nt[haiId];
            } else {
                return img_ny[haiId];
            }
        } else {
            if (tate) {
                return img_dt[haiId];
            } else {
                return img_dy[haiId];
            }
        }
    }

    private void rotate() {
        gc.rotate(-90);
        gc.translate(-600, 0);
    }

}