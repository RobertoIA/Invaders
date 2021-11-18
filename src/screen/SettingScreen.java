package screen;

import engine.Cooldown;
import engine.Core;

import java.awt.event.KeyEvent;

public class SettingScreen extends Screen{


    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;

    /** Time between changes in user selection. */
    private final Cooldown selectionCooldown;

    /** pause 버튼 눌러져 있는 거 확인하기*/
    private boolean isPause = true;


    public SettingScreen(final int width, final int height, final int fps) {
        super(width, height, fps);

        this.returnCode = 1;

        this.settingCode = 1;
        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.reset();
    }

    public final int run() {
        super.run();
        if (isPause == false) {
            this.returnCode = 2;
            this.logger.info("Return to title");
        }

        return this.returnCode;
    }

    protected final void update() {
        super.update();

        draw();

        /**고르기*/
        if (this.selectionCooldown.checkFinished()
                && this.inputDelay.checkFinished()) {
            if (inputManager.isKeyDown(KeyEvent.VK_UP)
                    || inputManager.isKeyDown(KeyEvent.VK_W)) {
                previousMenuItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
                    || inputManager.isKeyDown(KeyEvent.VK_S)) {
                nextMenuItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                this.returnCode = 1;
                this.isRunning=false;
            }


            if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
                // Return to game screen.
                this.returnCode = 2;
                this.isRunning = false;
            }
        }
    }

    /**다음 선택지*/
    private void nextMenuItem() {
        if(this.settingCode==3)
            this.settingCode=1;
        else
            settingCode++;
    }

    /**그전 선택지*/
    private void previousMenuItem() {
        if (this.settingCode == 1)
            this.settingCode = 3;
        else
            settingCode--;
    }
    /**여기에서 settingcode에 따라 값 배정*/
    public int updatesetting(){
        return 0;
    }


    private void draw(){
        /**화면 초기화*/
        drawManager.initDrawing(this);

        /**그리기*/
        drawManager.drawSettingScreen(this);
        drawManager.drawSettingmenu(this, this.settingCode);


        /**다 그렸다고 업데이트*/
        drawManager.completeDrawing(this);
    }
}

