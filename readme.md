# RPG-Style-LevelUp

A Fabric Minecraft server mod (Java / Gradle) that provides RPG-style player leveling and UI updates based on configurable skill data and experience mechanics.
Works for Minecraft 1.21 and runs purely serverside, no client mod needed.

## Features
- Per-player skill leveling system.
- Configurable skill settings via JSON.
- Periodic server-side updates (every 200 ticks).
- Player join handling to initialize player data and UI.
- Console/admin commands to inspect and modify player and mod settings.

## Build from Source
1. Clone the repository.
2. Ensure you have JDK 17+ and Gradle installed.
3. Navigate to the project directory.
4. Run `gradlew build` to compile the mod and generate the JAR file.
5. The output JAR will be located in `build/libs` or `build/devlibs`.

## Development
- Java, Gradle, Fabric API.
- Project source: `src/main/java`
- Resources: `src/main/resources`
- Use IntelliJ IDEA for editing, running, and debugging.

## Configuration
- Main config file: `config/rpg-style-leveling.json`
- Settings are loaded at initialization and saved when admin commands change configuration.

## Commands
- `/rpg` — Open basic RPG UI / status.
