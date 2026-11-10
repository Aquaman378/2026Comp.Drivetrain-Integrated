# Drivetrain integration notes

This project combines the original `2026CompetitiveConcept` project with the working teleop drivetrain setup from `Drivetrain-Test`.

## Changes made

1. `RobotContainer.java`
   - Instantiates the existing `frc.robot.subsystems.Swerve` subsystem.
   - Assigns it a default CTRE `SwerveRequest.FieldCentric` command.
   - Uses controller 0: left stick Y = forward/back, left stick X = strafe, right stick X = rotate.
   - Uses left bumper to reseed field-centric heading.
   - Preserves the existing intake, shooter, and feeder bindings.

2. `generated/TunerConstants.java`
   - Uses the tested drivetrain project's Tuner-generated swerve configuration, including drive/steer/encoder CAN IDs, gear ratios, encoder offsets, gains, and current limits.
   - Retains the `TunerSwerveDrivetrain` base class expected by the main project's `Swerve.java`.
   - Removes the test project's `CommandSwerveDrivetrain` factory method because the main project keeps its Choreo-capable `Swerve` subsystem.

3. `vendordeps/Phoenix6-frc2026-latest.json`
   - Updated to the Phoenix 6 vendor dependency version used by `Drivetrain-Test` (26.3.0).

## Important hardware checks before deploying

- Confirm the robot's actual CAN bus name is `main`.
- Confirm the tested drive motor IDs, steer motor IDs, CANcoder IDs, and Pigeon ID match the wired robot.
- Confirm the encoder offsets and gear ratios are the values from the working robot's Tuner project. Incorrect offsets can make modules steer to the wrong angles.
- Test with the robot safely supported off the floor first. Verify each wheel's steering direction and drive direction at low output before placing it on the carpet.
- Verify controller 0 is the intended driver controller.

## Build status

A Gradle build was attempted, but this environment could not download the Gradle 8.11 distribution because network access to `services.gradle.org` was unavailable. The merged Java source has therefore not been confirmed by a successful Gradle compile here. Run `gradlew.bat build` locally in WPILib VS Code and resolve any reported errors before deploying.

## Not changed

The original `Swerve.java` subsystem and its Choreo path-following methods were retained. The existing autonomous setup was not reworked; `RobotContainer.getAutonomousCommand()` still returns `Commands.none()` as it did in the uploaded main project.
