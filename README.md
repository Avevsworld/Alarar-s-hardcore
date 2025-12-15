# Alarar's hardcore

Just another plugin for folia!

## Building

This project uses Gradle with Java 21.

```bash
./gradlew build
```

The plugin JAR will be produced at `build/libs/AlararsHardcore-0.1.0.jar`.

## Running locally on Paper/Folia 1.21.x

1. Download a Paper or Folia 1.21.x server JAR.
2. Place `AlararsHardcore-0.1.0.jar` in the server's `plugins/` directory.
3. Start the server once to generate the configuration at `plugins/AlararsHardcore/config.yml`.
4. Adjust any settings (world name, spawn radius, mount timings, etc.), then restart the server.

The plugin expects the configured world (default `aether`) to exist and will random teleport players there on join.
