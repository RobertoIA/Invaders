package engine;

//Responsible for the sounds of the game

import javax.sound.sampled.*;
import java.io.File;
import screen.SettingsScreen;

public class Sound
{

    private static float decibels = 0;

    public static void playMusic() //background music class
    {
        File musicfile = new File("sounds/music.wav");
        try
        {
            AudioInputStream Audio = AudioSystem.getAudioInputStream(musicfile);
            Clip clip = AudioSystem.getClip();
            clip.open(Audio);
            setGain(clip);
            clip.start();
            SettingsScreen.adjustMusicVolume(clip);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch(Exception e)
        {

        }
    }

    public static void shoot() //shooting sound effect
    {
        File shot = new File("sounds/shot.wav");
        try
        {
            AudioInputStream Audio = AudioSystem.getAudioInputStream(shot);
            Clip clip = AudioSystem.getClip();
            clip.open(Audio);
            setGain(clip);
            clip.start();
        }
        catch(Exception e)
        {

        }
    }

    public static void death() //shooting sound effect
    {
        File dead = new File("sounds/death.wav");
        try
        {
            AudioInputStream Audio = AudioSystem.getAudioInputStream(dead);
            Clip clip = AudioSystem.getClip();
            clip.open(Audio);
            setGain(clip);
            clip.start();
        }
        catch(Exception e)
        {

        }
    }

    public static void setGain(Clip clip) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(gainControl.getValue()-decibels); // Reduce volume by decibels
        Core.LOGGER.info("Decibel value is " + decibels);
    }

    public static void setDecibels(float newDecibels) {
        decibels = newDecibels;
    }

    public static void enemydeath() //shooting sound effect
    {
        File bang = new File("sounds/boom.wav");
        try
        {
            AudioInputStream Audio = AudioSystem.getAudioInputStream(bang);
            Clip clip = AudioSystem.getClip();
            clip.open(Audio);
            clip.start();
        }
        catch(Exception e)
        {

        }
    }

    public static void enemyshot() //shooting sound effect
    {
        File zap = new File("sounds/zap.wav");
        try
        {
            AudioInputStream Audio = AudioSystem.getAudioInputStream(zap);
            Clip clip = AudioSystem.getClip();
            clip.open(Audio);
            clip.start();
        }
        catch(Exception e)
        {

        }
    }

    public static void gameover() //shooting sound effect
    {
        File over = new File("sounds/go.wav");
        try
        {
            AudioInputStream Audio = AudioSystem.getAudioInputStream(over);
            Clip clip = AudioSystem.getClip();
            clip.open(Audio);
            clip.start();
        }
        catch(Exception e)
        {

        }
    }

}
