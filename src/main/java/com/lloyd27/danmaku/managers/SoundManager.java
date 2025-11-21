package com.lloyd27.danmaku.managers;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.HashMap;

public class SoundManager {
    private AudioClip clip;
    private MediaPlayer currentMusic;
    private final HashMap<String, AudioClip> soundCache = new HashMap<>();

    // Précharge un son dès le lancement du Stage
    public void preloadSound(String fileName) {
        URL resource = SoundManager.class.getResource("/sound/" + fileName);
        if (resource == null) {
            System.err.println("⚠️ Sound not found: " + fileName);
            return;
        }

        // Crée et stocke le son en RAM → instantané au moment du play()
        AudioClip clip = new AudioClip(resource.toString());
        soundCache.put(fileName, clip);
    }

    // Joue une musique (avec option de boucle)
    public void playMusic(String fileName, double volume, boolean loop) {
        try {
            URL resource = SoundManager.class.getResource("/music/" + fileName);
            if (resource == null) {
                System.err.println("⚠️ Music not found: " + fileName);
                return;
            }

            // Stoppe la musique précédente si elle existe
            if (currentMusic != null) {
                currentMusic.stop();
            }

            Media media = new Media(resource.toString());
            currentMusic = new MediaPlayer(media);
            currentMusic.setVolume(volume);
            if (loop) {
                currentMusic.setCycleCount(MediaPlayer.INDEFINITE);
            }
            currentMusic.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void PauseMusic(){
        currentMusic.pause();
    }

    public void UnPauseMusic(){
        currentMusic.play();
    }

    public void playSound(String fileName, double volume) {
        try {
            clip = soundCache.get(fileName);

            // Si non préchargé → fallback mais avec latence
            if (clip == null) {
                URL resource = SoundManager.class.getResource("/sound/" + fileName);
                if (resource == null) {
                    System.err.println("⚠️ Sound not found: " + fileName);
                    return;
                }
                clip = new AudioClip(resource.toString());
                soundCache.put(fileName, clip);
            }

            clip.setVolume(volume);
            clip.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isMusicPlaying() {
        return currentMusic != null && currentMusic.getStatus() == MediaPlayer.Status.PLAYING;
    }

    // Stoppe la musique en cours
    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
            currentMusic = null; 
        }
    }

    public void stopSound(){
        if (clip != null) {
            clip.stop();
            clip = null; 
        }
    }
}
