# Haru2.0

Original Minecraft 1.8.9 Forge client targeting Java 8 and PojavLauncher.

## Implemented modules

- **JumpReset**: configurable chance and ground-only option; detects local-player hurt-time changes on client ticks.
- **WTap**: configurable chance and delay; briefly releases sprint after a melee hit.
- **BlockHit**: performs one short sword-use action per melee hit, with a configurable delay and sword-only option.
- **ClickGUI**: dark purple category panels, module toggles, setting controls, and responsive three-column layout on narrow screens. Right Shift opens/closes it.

These features are an independent implementation. They are not copied from Haru, Vape, Raven, or another proprietary client.

## Important limitations

The remaining catalog entries are currently placeholders. Their presence in the GUI does not mean their behavior has been implemented. In particular, do not assume AutoClick, AimAssist, Velocity, Reach, inventory automation, ESP, or exploit-category modules are functional.

The current source has passed earlier GitHub Actions compilation checks; the latest combined source still needs its own clean build and in-game behavior test on the target Forge 1.8.9/PojavLauncher setup. Compatibility with third-party clients such as Raven XD is not guaranteed until tested.

## Build

- Minecraft: 1.8.9
- Forge: 11.15.1.2318
- Java: 8
- Mappings: stable_22
- Build: `gradle build`
