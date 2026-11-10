package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Swerve;

public class RobotContainer {
    private final CommandXboxController driverController = new CommandXboxController(0);

    // Drivetrain speed limits, matching the working Drivetrain-Test project.
    private final double maxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond);
    private final double maxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);

    // Field-centric swerve request used by the working drivetrain test.
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
        .withDeadband(maxSpeed * 0.1)
        .withRotationalDeadband(maxAngularRate * 0.1)
        .withDriveRequestType(DriveRequestType.OpenLoopVoltage);

    // Subsystems
    private final Swerve swerve = new Swerve();
    private final Intake intake = new Intake();
    private final Feeder feeder = new Feeder();
    private final Shooter shooter = new Shooter();

    public RobotContainer() {
        configureButtonBindings();
    }

    private void configureButtonBindings() {
        // Default command: drive with the left stick and rotate with the right stick.
        swerve.setDefaultCommand(
            swerve.applyRequest(() ->
                drive.withVelocityX(-driverController.getLeftY() * maxSpeed)
                    .withVelocityY(-driverController.getLeftX() * maxSpeed)
                    .withRotationalRate(-driverController.getRightX() * maxAngularRate)
            )
        );

        // Reset field-centric heading with the left bumper.
        driverController.leftBumper()
            .onTrue(swerve.runOnce(swerve::seedFieldCentric));

        // Right Trigger: Run Intake Rollers
        driverController.rightTrigger()
            .whileTrue(Commands.run(() -> intake.setRollers(0.8), intake))
            .onFalse(Commands.runOnce(intake::stop, intake));

        // Left Trigger: Run Shooter at 85%
        driverController.leftTrigger()
            .whileTrue(Commands.run(() -> shooter.setSpeed(0.85), shooter))
            .onFalse(Commands.runOnce(shooter::stop, shooter));

        // A Button: Run Feeder to load game piece into shooter
        driverController.a()
            .whileTrue(Commands.run(() -> feeder.runFeeder(0.7), feeder))
            .onFalse(Commands.runOnce(feeder::stop, feeder));
    }

    public Command getAutonomousCommand() {
        return Commands.none();
    }
}
