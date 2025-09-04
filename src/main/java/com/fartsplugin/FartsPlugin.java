package com.fartsplugin;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;

import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.AnimationChanged;
import net.runelite.client.audio.AudioPlayer;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import java.util.Random;

@PluginDescriptor(name = "FartsPlugin")
public class FartsPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private FartsPluginConfig config;

    @Inject
    private ClientThread clientThread;

    @Inject
    private AudioPlayer audioPlayer;

    private final Random random = new Random();
    private long ibsEndTime = 0;
    private java.util.Timer ibsTimer;
    private boolean ibsActive = false;

    @Override
    protected void startUp() throws Exception {
        // Plugin started silently
    }



    @Override
    protected void shutDown() throws Exception {
        // Plugin stopped silently
        // Clean up IBS timer
        if (ibsTimer != null) {
            ibsTimer.cancel();
            ibsTimer = null;
        }
        ibsActive = false;
    }

    private void playFartSound() {
        // Only play sounds if logged in
        if (!isLoggedIn()) {
            return;
        }

        String fartMessage = getFartMessage();
        // Send message to friends chat which should be more visible
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", fartMessage, null);

        // Try to play actual sound file
        playSoundFile();
    }

    private void playRaspberrySound() {
        // Only play sounds if logged in
        if (!isLoggedIn()) {
            return;
        }

        String fartMessage = getFartMessage();
        // Send message to friends chat which should be more visible
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", fartMessage, null);

        // Try to play the specific raspberry sound file
        playSoundFile("/sounds/raspberry.wav");
    }

    private void playIBSFartSound() {
        // Only play sounds if logged in
        if (!isLoggedIn()) {
            return;
        }

        // Play a random fart sound (bypassing config checks)
        // Send cute chat message for IBS farts
        String fartMessage = getFartMessage();
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", fartMessage, null);
        playSoundFile();
    }

    private boolean isLoggedIn() {
        return client.getGameState() == GameState.LOGGED_IN;
    }

    private void playSoundFile() {
        // Use random sound selection for regular farts
        playSoundFile(null);
    }

    private void playSoundFile(String specificSoundFile) {
        try {
            // Get the sound file path - use specific file if provided, otherwise random
            String soundFile = specificSoundFile != null ? specificSoundFile : getSoundFilePath();

            if (soundFile != null) {
                // Load sound file from resources using getResourceAsStream (per RuneLite docs)
                // This works correctly with JAR-packaged plugins
                java.io.InputStream soundStream = getClass().getResourceAsStream(soundFile);

                if (soundStream != null) {
                    playSoundStream(soundStream);
                }
            }
        } catch (Exception e) {
            // Silent error handling
        }
    }

    private String getSoundFilePath() {
        // Random sound selection (excluding raspberry which is played specifically for
        // emotes)
        // JAR resources need leading slash to access from classpath root
        String[] soundFiles = {
                "/sounds/classic_fart.wav",
                "/sounds/wet_fart.wav",
                "/sounds/squeak_fart.wav",
                "/sounds/long_fart.wav",
                "/sounds/short_fart.wav"
        };

        return soundFiles[random.nextInt(soundFiles.length)];
    }

    private void playSoundStream(java.io.InputStream soundStream) {
        try {
            // Convert volume from config (0-100) to AudioPlayer format (0.0-1.0)
            float volume = Math.max(0.5f, config.fartVolume() / 100.0f); // Ensure minimum volume of 50%

            // JAR resources don't support mark/reset, so buffer the entire file into memory
            // This creates a ByteArrayInputStream that supports mark/reset operations
            java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
            byte[] data = new byte[8192]; // Larger buffer for efficiency
            int bytesRead;
            while ((bytesRead = soundStream.read(data)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            soundStream.close();

            // Create a ByteArrayInputStream that supports mark/reset
            java.io.ByteArrayInputStream bufferedStream = new java.io.ByteArrayInputStream(buffer.toByteArray());

            audioPlayer.play(bufferedStream, volume);

        } catch (Exception e) {
            try {
                soundStream.close();
            } catch (Exception closeException) {
                // Ignore close errors
            }
        }
    }



    private String getFartMessage() {
        // Random text message selection with cute one-liners
        String[] allFartMessages = {
                "Oops, sorry!",
                "I tooted!",
                "Excuse me!",
                "Someone's gassy today!",
                "Pardon my flatulence!",
                "Oopsie daisy!",
                "My bad!",
                "Excuse my manners!",
                "Sorry about that!",
                "Pardon me!",
                "Oops!",
                "Excuse me!",
                "My stomach had something to say!",
                "Pardon my interruption!",
                "Oops, that was me!",
                "Excuse my outburst!",
                "Sorry, couldn't hold it!",
                "Pardon my gas!",
                "Oops, my tummy tooted!",
                "Excuse my flatulence!"
        };

        return allFartMessages[random.nextInt(allFartMessages.length)];
    }

    @Subscribe
    public void onChatMessage(ChatMessage chatMessage) {
        // Only process messages if logged in
        if (!isLoggedIn()) {
            return;
        }

        String message = chatMessage.getMessage().toLowerCase();

        // Check for pizza eating messages to trigger IBS mode
        if (config.enableIBSMode() && !ibsActive && (message.contains("you eat half of the anchovy pizza") ||
                message.contains("you eat the remaining anchovy pizza") ||
                message.contains("you eat half of the meat pizza") ||
                message.contains("you eat the remaining meat pizza") ||
                message.contains("you eat half of the pineapple pizza") ||
                message.contains("you eat the remaining pineapple pizza") ||
                message.contains("you eat half of the pizza") ||
                message.contains("you eat the remaining pizza"))) {
            activateIBSMode();
        }

        // Check for fart-related chat triggers
        if (config.enableChatFart() && (message.contains("(fart)") || message.contains("(farts)") ||
                message.contains("*fart*") || message.contains("*farts*"))) {
            playFartSound();
        }
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged animationChanged) {
        // Only process animations if logged in
        if (!isLoggedIn() || animationChanged.getActor() == null) {
            return;
        }

        int animationId = animationChanged.getActor().getAnimation();

        if (config.enableRaspberryFart()) {
            // Raspberry emote animation ID is 2110
            if (animationId == 2110) {
                playRaspberrySound();
            }
        }
    }



    private void triggerIBSMode() {
        if (ibsTimer != null) {
            ibsTimer.cancel();
        }

        ibsEndTime = System.currentTimeMillis() + (5 * 60 * 1000); // 5 minutes
        ibsActive = true;

        ibsTimer = new java.util.Timer();
        ibsTimer.scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run() {
                if (System.currentTimeMillis() < ibsEndTime) {
                    clientThread.invoke(() -> playIBSFartSound());
                } else {
                    ibsTimer.cancel();
                    ibsTimer = null;
                    ibsActive = false;
                    // Post completion message on client thread
                    clientThread.invoke(() -> client.addChatMessage(ChatMessageType.GAMEMESSAGE, "",
                            "Your stomach finally settles down.", null));
                }
            }
        }, 0, getRandomIBSDelay()); // Random delay between 20-30 seconds
    }

    private void activateIBSMode() {
        // Only show activation message when actually triggered by pizza eating
        try {
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "",
                    "You feel a rumbling in your stomach... IBS activated!", null);
        } catch (Exception e) {
            // Silent error handling
        }
        triggerIBSMode();
    }

    private int getRandomIBSDelay() {
        // Random delay between 20-30 seconds (20000-30000 milliseconds)
        return 20000 + random.nextInt(10001);
    }

    @Provides
    FartsPluginConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(FartsPluginConfig.class);
    }
}
