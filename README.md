# FartsPlugin

A fun RuneLite plugin that adds fart sounds and effects to the game!

## Features

- 🎵 **Multiple Fart Types**: Classic, Wet, Squeaker, Long, and Short farts
- 🎮 **Event Triggers**: Farts on login, death, and level ups
- ⚙️ **Configurable Settings**: Enable/disable different triggers and choose fart types
- 🎲 **Random Sounds**: Different fart sounds for variety
- 📝 **Customizable Greeting**: Personalize your welcome message

## Installation

1. Clone or download this repository
2. **Add sound files** (see Sound Files section below)
3. Build the plugin: `.\gradlew.bat build`
4. The built JAR will be in `build/libs/farts-plugin-1.0-SNAPSHOT-all.jar`
5. Or run the test: `java -ea -jar build/libs/farts-plugin-1.0-SNAPSHOT-all.jar`

## Sound Files

The plugin supports actual audio playback! Add WAV sound files to `src/main/resources/sounds/`:

- `classic_fart.wav` - Classic fart sound
- `wet_fart.wav` - Wet/squelchy fart sound
- `squeak_fart.wav` - High-pitched squeaky fart
- `long_fart.wav` - Long drawn-out fart sound
- `short_fart.wav` - Quick short fart

**Audio Requirements:**
- Format: WAV (16-bit, 44100 Hz)
- Length: 1-5 seconds
- Channels: Mono or Stereo

See `src/main/resources/sounds/README.md` for detailed instructions.

## Configuration

You can configure the plugin through the RuneLite settings:
- **Death Fart**: Play a fart sound when you die
- **Chat Fart**: Play a fart sound when someone says (fart), (farts), *fart*, *farts*
- **Raspberry Fart**: Play a fart sound when someone does the raspberry emote
- **IBS Mode**: Enable IBS mode that triggers farts after eating pizza
- **Fart Volume**: Volume level for fart sounds (0-100)

## Features

### 🎮 **Event Triggers**
- **Death**: Farts when you die in-game
- **Chat Words**: Farts when anyone says (fart), (farts), *fart*, or *farts*
- **Raspberry Emote**: Farts when anyone performs the raspberry emote (animation ID 1835)
- **IBS Mode**: Special mode triggered by eating pizza items

### 🍕 **IBS Mode**
- **Trigger**: Eating any pizza-type item (Plain, Meat, Anchovy, etc.)
- **Effect**: Farts every 30 seconds for 5 minutes
- **Messages**: Special chat messages during the IBS episode
- **Cure**: None - just wait it out!

### 🎵 **Random Sound Selection**
The plugin randomly selects from 5 different fart sound types:
- **Classic**: Traditional fart sounds
- **Wet**: Squelchy, bubbly farts
- **Squeaker**: High-pitched, squeaky farts
- **Long**: Extended, drawn-out farts
- **Short**: Quick, brief farts

Each trigger plays a randomly selected sound for variety!

## Development

This plugin is built using:
- Java 11
- Gradle
- RuneLite API
- Lombok for logging

## Building

```bash
# Build the plugin
.\gradlew.bat build

# Run tests
.\gradlew.bat test

# Create shadow JAR (includes all dependencies)
.\gradlew.bat shadowJar
```

## License

This project is licensed under the BSD-2-Clause License.
