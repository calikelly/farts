package com.fartsplugin;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;

import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.AnimationChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import java.util.Random;

@PluginDescriptor(
name = "FartsPlugin"
)
public class FartsPlugin extends Plugin
{
@Inject
private Client client;

@Inject
private FartsPluginConfig config;

@Inject
private ClientThread clientThread;

private final Random random = new Random();
private long ibsEndTime = 0;
private java.util.Timer ibsTimer;
private boolean ibsActive = false;

@Override
protected void startUp() throws Exception
{
// Plugin started silently
}

@Override
protected void shutDown() throws Exception
{
// Plugin stopped silently
// Clean up IBS timer
if (ibsTimer != null) {
ibsTimer.cancel();
ibsTimer = null;
}
ibsActive = false;
}

private void playFartSound()
{
// Only play sounds if logged in
if (!isLoggedIn()) {
return;
}

String fartMessage = getFartMessage();
// Send message to friends chat which should be more visible
System.out.println("[DEBUG] Fart: " + fartMessage);
client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", fartMessage, null);

// Try to play actual sound file
playSoundFile();
}

private void playRaspberrySound()
{
// Only play sounds if logged in
if (!isLoggedIn()) {
return;
}

String fartMessage = getFartMessage();
// Send message to friends chat which should be more visible
System.out.println("[DEBUG] Raspberry Fart: " + fartMessage);
client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", fartMessage, null);

// Try to play the specific raspberry sound file
playSoundFile("/sounds/raspberry.wav");
}

private void playIBSFartSound()
{
// Only play sounds if logged in
if (!isLoggedIn()) {
return;
}

// Play a random fart sound (bypassing config checks)
// Send cute chat message for IBS farts
String fartMessage = getFartMessage();
System.out.println("[DEBUG] IBS Fart: " + fartMessage);
client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", fartMessage, null);
playSoundFile();
}

private boolean isLoggedIn()
{
return client.getGameState() == GameState.LOGGED_IN;
}

private void playSoundFile()
{
// Use random sound selection for regular farts
playSoundFile(null);
}

private void playSoundFile(String specificSoundFile)
{
try
{
// Get the sound file path - use specific file if provided, otherwise random
String soundFile = specificSoundFile != null ? specificSoundFile : getSoundFilePath();

if (soundFile != null)
{
// Load sound file from resources
java.io.InputStream soundStream = getClass().getResourceAsStream(soundFile);

if (soundStream != null)
{
// Note: This is a basic example. For production, you'd want to use
// RuneLite's audio system or a proper audio library
playSoundStream(soundStream);
}
else
{
// Try alternative path
String altPath = soundFile.substring(1); // Remove leading slash
soundStream = getClass().getResourceAsStream(altPath);
if (soundStream != null) {
playSoundStream(soundStream);
}
}
}
}
catch (Exception e)
{
// Silent error handling
}
}

private String getSoundFilePath()
{
// Random sound selection (excluding raspberry which is played specifically for emotes)
String[] soundFiles = {
"/sounds/classic_fart.wav",
"/sounds/wet_fart.wav",
"/sounds/squeak_fart.wav",
"/sounds/long_fart.wav",
"/sounds/short_fart.wav"
};

return soundFiles[random.nextInt(soundFiles.length)];
}

private void playSoundStream(java.io.InputStream soundStream)
{
// Basic sound playback using Java's AudioSystem
// Note: This is a simplified example - production code should handle
// audio format detection, threading, and proper resource management
try
{
// Read the entire stream into a byte array to avoid mark/reset issues with JAR resources
byte[] soundData = readAllBytes(soundStream);
soundStream.close();

java.io.ByteArrayInputStream byteStream = new java.io.ByteArrayInputStream(soundData);
javax.sound.sampled.AudioInputStream audioIn = javax.sound.sampled.AudioSystem.getAudioInputStream(byteStream);

javax.sound.sampled.Clip clip = javax.sound.sampled.AudioSystem.getClip();
clip.open(audioIn);

// Set volume (0.0 to 1.0)
float volume = config.fartVolume() / 100.0f;
javax.sound.sampled.FloatControl gainControl = (javax.sound.sampled.FloatControl) clip.getControl(javax.sound.sampled.FloatControl.Type.MASTER_GAIN);
float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
gainControl.setValue(dB);

clip.start();

// Clean up after sound finishes
clip.addLineListener(event -> {
if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
clip.close();
try {
byteStream.close();
} catch (java.io.IOException e) {
// Silent error handling
}
}
});

}
catch (Exception e)
{
// Silent error handling
}
}

private byte[] readAllBytes(java.io.InputStream inputStream) throws java.io.IOException
{
java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
int nRead;
byte[] data = new byte[16384]; // 16KB buffer

while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
buffer.write(data, 0, nRead);
}

buffer.flush();
return buffer.toByteArray();
}

private String getFartMessage()
{
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
public void onChatMessage(ChatMessage chatMessage)
{
// Only process messages if logged in
if (!isLoggedIn()) {
return;
}

String message = chatMessage.getMessage().toLowerCase();

// Check for pizza eating messages to trigger IBS mode
if (config.enableIBSMode() && !ibsActive && (
message.contains("you eat half of the anchovy pizza") ||
message.contains("you eat the remaining anchovy pizza") ||
message.contains("you eat half of the meat pizza") ||
message.contains("you eat the remaining meat pizza") ||
message.contains("you eat half of the pineapple pizza") ||
message.contains("you eat the remaining pineapple pizza") ||
message.contains("you eat half of the pizza") ||
message.contains("you eat the remaining pizza")
)) {
activateIBSMode();
}

// Check for fart-related chat triggers
if (config.enableChatFart() && (
message.contains("(fart)") || message.contains("(farts)") ||
message.contains("*fart*") || message.contains("*farts*")
)) {
playFartSound();
}
}

@Subscribe
public void onAnimationChanged(AnimationChanged animationChanged)
{
// Only process animations if logged in
if (!isLoggedIn() || animationChanged.getActor() == null) {
return;
}

int animationId = animationChanged.getActor().getAnimation();

if (config.enableRaspberryFart())
{
// Raspberry emote animation ID is 2110
if (animationId == 2110)
{
playRaspberrySound();
}
}
}

@Subscribe
public void onItemContainerChanged(ItemContainerChanged event)
{
// Only process inventory changes if logged in
if (!isLoggedIn()) {
return;
}

if (config.enableIBSMode() && event.getContainerId() == 93) // Inventory container
{
// Check if any pizza items were consumed (disappeared from inventory)
// This is a simplified implementation - in practice you'd track item changes
checkForPizzaConsumption();
}
}
private void checkForPizzaConsumption()
{
// This is a simplified check - in a real implementation you'd compare
// before/after inventory states to detect pizza consumption
// For now, we'll just assume pizza consumption triggers IBS mode
// Pizza item IDs: 2289 (Plain pizza), 2291 (Meat pizza), 2293 (Anchovy pizza), etc.
if (random.nextInt(100) < 5) // 5% chance when inventory changes (simulating pizza eating)
{
activateIBSMode();
}
}

private void triggerIBSMode()
{
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
clientThread.invoke(() ->
client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Your stomach finally settles down.", null)
);
}
}
}, 0, getRandomIBSDelay()); // Random delay between 20-30 seconds
}

private void activateIBSMode()
{
// Only show activation message when actually triggered by pizza eating
try {
client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "You feel a rumbling in your stomach... IBS activated!", null);
} catch (Exception e) {
// Silent error handling
}
triggerIBSMode();
}

private int getRandomIBSDelay()
{
// Random delay between 20-30 seconds (20000-30000 milliseconds)
return 20000 + random.nextInt(10001);
}

@Provides
FartsPluginConfig provideConfig(ConfigManager configManager)
{
return configManager.getConfig(FartsPluginConfig.class);
}
}
