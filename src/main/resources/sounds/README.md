# FartsPlugin Sound Files

This directory contains the audio files for the FartsPlugin sound effects.

## Required Sound Files

Place the following WAV files in this directory:

- `classic_fart.wav` - Classic fart sound
- `wet_fart.wav` - Wet/squelchy fart sound
- `squeak_fart.wav` - High-pitched squeaky fart
- `long_fart.wav` - Long drawn-out fart sound
- `short_fart.wav` - Quick short fart

## Audio Format Requirements

- **Format**: WAV (uncompressed)
- **Sample Rate**: 44100 Hz (CD quality)
- **Bit Depth**: 16-bit
- **Channels**: Mono or Stereo
- **Length**: 1-5 seconds (keep them short!)

## Getting Sound Files

You can:

1. **Record your own**: Use any audio recording software
2. **Download free sounds**: From freesound.org, zapsplat.com, etc.
3. **Use sound effects libraries**: Many free sound effect packs available online
4. **Create with software**: Use Audacity, GarageBand, or other audio editors

## Tips

- Keep files small for plugin performance
- Test volume levels - sounds should be normalized
- Consider different variations for each type
- Make sure sounds aren't copyrighted if distributing the plugin

## Example File Structure

```
sounds/
├── README.md (this file)
├── classic_fart.wav
├── wet_fart.wav
├── squeak_fart.wav
├── long_fart.wav
└── short_fart.wav
```

## Testing

After adding sound files:
1. Rebuild the plugin: `.\gradlew.bat build`
2. Run the test: `java -ea -jar build/libs/farts-plugin-1.0-SNAPSHOT-all.jar`
3. Trigger a fart event (login, death, etc.) to test the sound

