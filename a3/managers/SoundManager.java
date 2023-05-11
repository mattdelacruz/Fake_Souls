package a3.managers;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector3f;

import tage.Camera;
import tage.audio.AudioManagerFactory;
import tage.audio.AudioResource;
import tage.audio.AudioResourceType;
import tage.audio.IAudioManager;
import tage.audio.Sound;
import tage.audio.SoundType;

public class SoundManager {
    private IAudioManager audioManager;
    private Map<String, Sound> soundMap = new HashMap<String, Sound>();

    public SoundManager() {
        audioManager = AudioManagerFactory.createAudioManager(
                "tage.audio.joal.JOALAudioManager");
        if (!audioManager.initialize()) {
            System.out.println("Sound manager has failed to initalize!");
            return;
        }
    }

    public void addSound(String soundName, String soundPath, int volume, boolean toLoop, float maxDistance,
            float minDistance, float rollOff, Vector3f soundLocation, SoundType type, AudioResourceType resourceType) {
        AudioResource resource = audioManager.createAudioResource(soundPath, resourceType);

        Sound sound = new Sound(resource, type, volume, toLoop);
        sound.initialize(audioManager);
        sound.setMaxDistance(maxDistance);
        sound.setMinDistance(minDistance);
        sound.setRollOff(rollOff);
        sound.setLocation(soundLocation);
        soundMap.put(soundName, sound);
    }

    public void addSound(String soundName, String soundPath, int volume, boolean toLoop, float maxDistance,
            float minDistance, float rollOff, Vector3f soundLocation, SoundType type) {
        AudioResource resource = audioManager.createAudioResource(soundPath, AudioResourceType.AUDIO_SAMPLE);

        Sound sound = new Sound(resource, type, volume, toLoop);
        sound.initialize(audioManager);
        sound.setMaxDistance(maxDistance);
        sound.setMinDistance(minDistance);
        sound.setRollOff(rollOff);
        sound.setLocation(soundLocation);
        soundMap.put(soundName, sound);
    }

    public void setEarParameters(Camera cam, Vector3f location, Vector3f orientationForward, Vector3f orientationUp) {
        audioManager.getEar().setLocation(location);
        audioManager.getEar().setOrientation(orientationForward, orientationUp);
    }

    public void playSound(String soundName) {
        if (!soundMap.get(soundName).getIsPlaying())
            soundMap.get(soundName).play();
    }

    public void updateSoundLocation(String soundName, Vector3f location) {
        soundMap.get(soundName).setLocation(location);
    }

    public boolean isPlaying(String soundName) {
        return soundMap.get(soundName).getIsPlaying();
    }

    public void stopSound(String soundName) {
        soundMap.get(soundName).stop();
    }
}
