package engine;

//Responsible for the sounds of the game

import javax.sound.sampled.*;
import java.io.File;

public class Sound
{

    public static void playMusic() //background music class
    {
        File musicfile = new File("/Users/josholeary/Documents/KEDDIT/Courses/SoftwareDevPrac/Invaders/sounds/music.wav");
        try
        {
            AudioInputStream Audio = AudioSystem.getAudioInputStream(musicfile);
            Clip clip = AudioSystem.getClip();
            clip.open(Audio);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch(Exception e)
        {

        }
    }

    public static void shoot() //shooting sound effect
    {
        File shot = new File("/Users/josholeary/Documents/KEDDIT/Courses/SoftwareDevPrac/Invaders/sounds/shot.wav");
        try
        {
            AudioInputStream Audio = AudioSystem.getAudioInputStream(shot);
            Clip clip = AudioSystem.getClip();
            clip.open(Audio);
            clip.start();
        }
        catch(Exception e)
        {

        }
    }

    public static void death() //shooting sound effect
    {
        File dead = new File("/Users/josholeary/Documents/KEDDIT/Courses/SoftwareDevPrac/Invaders/sounds/death.wav");
        try
        {
            AudioInputStream Audio = AudioSystem.getAudioInputStream(dead);
            Clip clip = AudioSystem.getClip();
            clip.open(Audio);
            clip.start();
        }
        catch(Exception e)
        {

        }
    }

}
