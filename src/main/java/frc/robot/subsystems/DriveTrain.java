// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.PigeonIMU;
// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.sensors.PigeonIMU_StatusFrame;

//import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.commands.driveWithJoyStick;

/**
 *
 */
public class DriveTrain extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
private WPI_TalonFX leftFollower;
private WPI_TalonFX rightFollower;
private PigeonIMU pigeon;
private WPI_TalonFX rightMaster;
private WPI_TalonFX leftMaster;
private DifferentialDrive tankDrive;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private double y = 0;
    private double twist = 0;
    // private double timer = 0;
    TalonFXInvertType _rightInvert = TalonFXInvertType.CounterClockwise; // Same as invert = "false"
    TalonFXInvertType _leftInvert = TalonFXInvertType.Clockwise; // Same as invert = "true"
    TalonFXConfiguration _rightConfig = new TalonFXConfiguration();
    TalonFXConfiguration _leftConfig = new TalonFXConfiguration();
    // private boolean coolingOn = false;

    public DriveTrain() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
leftFollower = new WPI_TalonFX(0);


        
rightFollower = new WPI_TalonFX(2);


        
pigeon = new PigeonIMU(4);


        
rightMaster = new WPI_TalonFX(3);


        
leftMaster = new WPI_TalonFX(1);


        
tankDrive = new DifferentialDrive(rightMaster, leftMaster);
addChild("TankDrive",tankDrive);
tankDrive.setSafetyEnabled(false);
tankDrive.setExpiration(0.1);
tankDrive.setMaxOutput(1.0);

        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        motorConfig();
    }

    @Override
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        setDefaultCommand(new driveWithJoyStick());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
    }

    private int limitDashboardUpdates = 0;

    @Override
    public void periodic() {
        PigeonIMU.GeneralStatus genStatus = new PigeonIMU.GeneralStatus();
        PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();
        double[] xyz_dps = new double[3];
        /* grab some input data from Pigeon and gamepad */
        pigeon.getGeneralStatus(genStatus);
        pigeon.getRawGyro(xyz_dps);
        pigeon.getFusedHeading(fusionStatus);
        double[] ypr = new double[3];
        pigeon.getYawPitchRoll(ypr);

        if (Constants.kDebug_DT) {
            if (++limitDashboardUpdates % 10 == 0) {
                SmartDashboard.putNumber(Constants.kAmpLimitName_DT, Constants.kAmpLimit_DT);
                SmartDashboard.putNumber(Constants.kAmpPeakLimitName_DT, Constants.kAmpPeak_DT);
                SmartDashboard.putNumber("test/drive/RMaster Amps", rightMaster.getSupplyCurrent());
                SmartDashboard.putNumber("test/drive/RFollow Amps", rightFollower.getSupplyCurrent());
                SmartDashboard.putNumber("test/drive/LMaster Amps", leftMaster.getSupplyCurrent());
                SmartDashboard.putNumber("test/drive/LFollow Amps", leftFollower.getSupplyCurrent());
                SmartDashboard.putNumber("test/drive/RMStator Stator", rightMaster.getStatorCurrent());
                SmartDashboard.putNumber("test/drive/RFStator Stator", rightFollower.getStatorCurrent());
                SmartDashboard.putNumber("test/drive/LMStator Stator", leftMaster.getStatorCurrent());
                SmartDashboard.putNumber("test/drive/LFStator Stator", leftFollower.getStatorCurrent());
                SmartDashboard.putNumber("drive/RMaster Temp", rightMaster.getTemperature());
                SmartDashboard.putNumber("drive/RFollow Temp", rightFollower.getTemperature());
                SmartDashboard.putNumber("drive/LMaster Temp", leftMaster.getTemperature());
                SmartDashboard.putNumber("drive/LFollow Temp", leftFollower.getTemperature());

                SmartDashboard.putNumber("test/drive/Left encoder",
                        leftMaster.getSensorCollection().getIntegratedSensorPosition());
                SmartDashboard.putNumber("test/drive/Left velocity",
                        leftMaster.getSensorCollection().getIntegratedSensorVelocity());
                SmartDashboard.putNumber("test/drive/Right encoder",
                        rightMaster.getSensorCollection().getIntegratedSensorPosition());
                SmartDashboard.putNumber("test/drive/Right velocity",
                        rightMaster.getSensorCollection().getIntegratedSensorVelocity());
            }
        }
        if (Constants.kDebug_DT) {
            SmartDashboard.putBoolean("test/drive/Pigeon Ready?",
                    (pigeon.getState() == PigeonIMU.PigeonState.Ready) ? true : false);
            SmartDashboard.putNumber("test/drive/Pigeon Angle", fusionStatus.heading);
            SmartDashboard.putNumber("test/drive/Pigeon Yaw", ypr[0]);
        }

        // if (timer >= 2000) {
        // coolMotor();
        // timer = 0;
        // }

        // else {
        // timer = timer + 20;
        // }
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS
    public void driveWithJoystick(Joystick joystickP0) {
        y = joystickP0.getY();
        twist = joystickP0.getTwist();
        y = Deadband(y);
        // limit the turn to the square of the number. Keep signs straight use ? :
        // syntax

        twist = (twist < 0) ? -Math.pow(Deadband(twist), 2) : Math.pow(Deadband(twist), 2);

        if (Robot.shifter.highGear == true) {
            double max = .7;
            leftMaster.configPeakOutputForward(max, Constants.kTimeoutMs);
            leftMaster.configPeakOutputReverse(-max, Constants.kTimeoutMs);
            rightMaster.configPeakOutputForward(max, Constants.kTimeoutMs);
            rightMaster.configPeakOutputReverse(-max, Constants.kTimeoutMs);
      //      y = ThrottleLookup.calcJoystickCorrection("HighGearRamp", y);
            // twist = ThrottleLookup.calcJoystickCorrection("HighGearTurn", twist);
       //     twist = (twist < 0) ? -Math.pow(Deadband(twist), 2) : Math.pow(Deadband(twist), 2);
            tankDrive.arcadeDrive(y, -twist);
        } else {
            double max = .4;
            double maxxx = 1.0;
            leftMaster.configPeakOutputForward(max, Constants.kTimeoutMs);
            leftMaster.configPeakOutputReverse(-maxxx, Constants.kTimeoutMs);
            rightMaster.configPeakOutputForward(max, Constants.kTimeoutMs);
            rightMaster.configPeakOutputReverse(-maxxx, Constants.kTimeoutMs);
        //    y = ThrottleLookup.calcJoystickCorrection("LowGearRamp", y);
        //    twist = ThrottleLookup.calcJoystickCorrection("LowGearTurn", twist);
            tankDrive.arcadeDrive(y, -twist);
        }

    }

    public void motorConfig() {
        motorConfigFalcon();
    }

    public void driveForward() {
        rightMaster.set(.3);
        leftMaster.set(.3);
    }

    public void closedLoopTurn(double angle) {
        zeroSensors();
        rightMaster.set(ControlMode.MotionMagic, 0, DemandType.AuxPID, angle * 10);
        leftMaster.follow(rightMaster, FollowerType.AuxOutput1);
        // rightFollower.follow(rightMaster);
        // leftFollower.follow(leftMaster);
    }

    public boolean closedLoopTurnComplete(double target) {
        double[] ypr = new double[3];
        pigeon.getYawPitchRoll(ypr);

        if (Constants.kDebug_DT)
            SmartDashboard.putNumber("test/shifter/Turn Distance Remaining", Math.abs(ypr[0] - target));
        return Math.abs(ypr[0] - target) < (360 * .005);
    }

    public void stop() {
        rightMaster.set(0);
        leftMaster.set(0);
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public void motorConfigFalcon() {
        // Factory default all motors initially
        rightMaster.configFactoryDefault();
        rightFollower.configFactoryDefault();
        leftMaster.configFactoryDefault();
        leftFollower.configFactoryDefault();

        // Set Neutral Mode
        leftMaster.setNeutralMode(NeutralMode.Brake);
        leftFollower.setNeutralMode(NeutralMode.Brake);
        rightMaster.setNeutralMode(NeutralMode.Brake);
        rightFollower.setNeutralMode(NeutralMode.Brake);

        // Configure output and sensor direction
        leftMaster.setInverted(_leftInvert);
        leftFollower.setInverted(_leftInvert);
        rightMaster.setInverted(_rightInvert);
        rightFollower.setInverted(_rightInvert);

        // configure the max current for motor. Thought is that
        // other motors will follow.
        double ampLimit = SmartDashboard.getNumber(Constants.kAmpLimitName_DT, Constants.kAmpLimit_DT);
        double ampPeakLimit = SmartDashboard.getNumber(Constants.kAmpPeakLimitName_DT, Constants.kAmpPeak_DT);

        SupplyCurrentLimitConfiguration currentLimitingFalcons = new SupplyCurrentLimitConfiguration(
                Constants.kEnableCurrentLimiting_DT, ampLimit, ampPeakLimit, Constants.kthreshholdTime);

        rightMaster.configSupplyCurrentLimit(currentLimitingFalcons);

        // Reset Pigeon Configs
        pigeon.configFactoryDefault();

        _leftConfig.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor; // Local Feedback Source

        // * Configure the Remote (Left) Talon's selected sensor as a remote sensor for
        // * the right Talon

        _rightConfig.remoteFilter0.remoteSensorDeviceID = leftMaster.getDeviceID(); // Device ID of Remote Source
        _rightConfig.remoteFilter0.remoteSensorSource = RemoteSensorSource.TalonSRX_SelectedSensor; // Remote Source
                                                                                                    // Type

        // * Now that the Left sensor can be used by the master Talon, set up the Left
        // * (Aux) and Right (Master) distance into a single Robot distance as the
        // * Master's Selected Sensor 0.

        setRobotDistanceConfigs(_rightInvert, _rightConfig);
        // FPID for Distance
        _rightConfig.slot0.kF = Constants.kGains_Distanc.kF;
        _rightConfig.slot0.kP = Constants.kGains_Distanc.kP;
        _rightConfig.slot0.kI = Constants.kGains_Distanc.kI;
        _rightConfig.slot0.kD = Constants.kGains_Distanc.kD;
        _rightConfig.slot0.integralZone = Constants.kGains_Distanc.kIzone;
        _rightConfig.slot0.closedLoopPeakOutput = Constants.kGains_Distanc.kPeakOutput;

        // * Heading Configs
        _rightConfig.remoteFilter1.remoteSensorDeviceID = pigeon.getDeviceID(); // Pigeon Device ID
        _rightConfig.remoteFilter1.remoteSensorSource = RemoteSensorSource.Pigeon_Yaw; // This is for a Pigeon over CAN
        _rightConfig.auxiliaryPID.selectedFeedbackSensor = FeedbackDevice.RemoteSensor1; // Set as the Aux Sensor
        _rightConfig.auxiliaryPID.selectedFeedbackCoefficient = 3600.0 / Constants.kPigeonUnitsPerRotation; // Convert
                                                                                                            // Yaw to
                                                                                                            // tenths of
                                                                                                            // a degree

        // * false means talon's local output is PID0 + PID1, and other side Talon is
        // PID0
        // * - PID1 This is typical when the master is the right Talon FX and using
        // Pigeon
        // *
        // * true means talon's local output is PID0 - PID1, and other side Talon is
        // PID0
        // * + PID1 This is typical when the master is the left Talon FX and using
        // Pigeon

        _rightConfig.auxPIDPolarity = false;
        // FPID for Heading
        _rightConfig.slot1.kF = Constants.kGains_Turning.kF;
        _rightConfig.slot1.kP = Constants.kGains_Turning.kP;
        _rightConfig.slot1.kI = Constants.kGains_Turning.kI;
        _rightConfig.slot1.kD = Constants.kGains_Turning.kD;
        _rightConfig.slot1.integralZone = Constants.kGains_Turning.kIzone;
        _rightConfig.slot1.closedLoopPeakOutput = Constants.kGains_Turning.kPeakOutput;

        // Config the neutral deadband.
        _leftConfig.neutralDeadband = Constants.kNeutralDeadband;
        _rightConfig.neutralDeadband = Constants.kNeutralDeadband;

        // *
        // * 1ms per loop. PID loop can be slowed down if need be. For example, - if
        // * sensor updates are too slow - sensor deltas are very small per update, so
        // * derivative error never gets large enough to be useful. - sensor movement is
        // * very slow causing the derivative error to be near zero.

        int closedLoopTimeMs = 1;
        rightMaster.configClosedLoopPeriod(0, closedLoopTimeMs, Constants.kTimeoutMs);
        rightMaster.configClosedLoopPeriod(1, closedLoopTimeMs, Constants.kTimeoutMs);

        // Motion Magic Configs
        _rightConfig.motionAcceleration = 9500; // (distance units per 100 ms) per second
        _rightConfig.motionCruiseVelocity = 17000; // distance units per 100 ms

        // APPLY the config settings
        leftMaster.configAllSettings(_leftConfig);
        leftFollower.configAllSettings(_leftConfig);
        rightMaster.configAllSettings(_rightConfig);
        rightFollower.configAllSettings(_rightConfig);

        // Set status frame periods to ensure we don't have stale data

        // * These aren't configs (they're not persistant) so we can set these after the
        // * configs.

        rightMaster.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20, Constants.kTimeoutMs);
        rightMaster.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 20, Constants.kTimeoutMs);
        rightMaster.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 20, Constants.kTimeoutMs);
        rightMaster.setStatusFramePeriod(StatusFrame.Status_10_Targets, 10, Constants.kTimeoutMs);
        leftMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, Constants.kTimeoutMs);
        pigeon.setStatusFramePeriod(PigeonIMU_StatusFrame.CondStatus_9_SixDeg_YPR, 5, Constants.kTimeoutMs);

        rightFollower.follow(rightMaster);
        leftFollower.follow(leftMaster);
        // WPI drivetrain classes assume by default left & right are opposite
        // - call this to apply + to both sides when moving forward
        tankDrive.setRightSideInverted(false);

        // set on call from autonomous
        rightMaster.selectProfileSlot(Constants.kSlot_Distanc, Constants.PID_PRIMARY);
        rightMaster.selectProfileSlot(Constants.kSlot_Turning, Constants.PID_TURN);
        // rightFollower.follow(rightMaster);
        // leftFollower.follow(leftMaster);

        // rightMaster.configStatorCurrentLimit(currLimitCfg)
        // (Constants.kPeakCurrentAmps, Constants.kTimeoutMs);
        // rightMaster.configPeakCurrentDuration(Constants.kPeakTimeMs,
        // Constants.kTimeoutMs);
        // rightMaster.configContinuousCurrentLimit(Constants.kContinCurrentAmps,
        // Constants.kTimeoutMs);
        // rightMaster.enableCurrentLimit(_currentLimEn); // Honor initial setting
        // Set Max output of motors
        // double max = 0.25;
        // leftMaster.configPeakOutputForward(max, Constants.kTimeoutMs);
        // leftMaster.configPeakOutputReverse(-max, Constants.kTimeoutMs);
        // rightMaster.configPeakOutputForward(max, Constants.kTimeoutMs);
        // rightMaster.configPeakOutputReverse(-max, Constants.kTimeoutMs);

    }

    /** Zero all sensors, both Talons and Pigeon */
    public void zeroSensors() {
        leftMaster.getSensorCollection().setIntegratedSensorPosition(0, Constants.kTimeoutMs);
        rightMaster.getSensorCollection().setIntegratedSensorPosition(0, Constants.kTimeoutMs);
        pigeon.setYaw(0, Constants.kTimeoutMs);
        pigeon.setAccumZAngle(0, Constants.kTimeoutMs);
        System.out.println("Integrated Encoders + Pigeon] All sensors are zeroed.\n");
    }

    void setRobotDistanceConfigs(TalonFXInvertType masterInvertType, TalonFXConfiguration masterConfig) {
        /**
         * Determine if we need a Sum or Difference.
         * 
         * The auxiliary Talon FX will always be positive in the forward direction
         * because it's a selected sensor over the CAN bus.
         * 
         * The master's native integrated sensor may not always be positive when forward
         * because sensor phase is only applied to *Selected Sensors*, not native sensor
         * sources. And we need the native to be combined with the aux (other side's)
         * distance into a single robot distance.
         */

        /*
         * THIS FUNCTION should not need to be modified. This setup will work regardless
         * of whether the master is on the Right or Left side since it only deals with
         * distance magnitude.
         */

        /* Check if we're inverted */
        if (masterInvertType == TalonFXInvertType.Clockwise) {
            /*
             * If master is inverted, that means the integrated sensor will be negative in
             * the forward direction.
             * 
             * If master is inverted, the final sum/diff result will also be inverted. This
             * is how Talon FX corrects the sensor phase when inverting the motor direction.
             * This inversion applies to the *Selected Sensor*, not the native value.
             * 
             * Will a sensor sum or difference give us a positive total magnitude?
             * 
             * Remember the Master is one side of your drivetrain distance and Auxiliary is
             * the other side's distance.
             * 
             * Phase | Term 0 | Term 1 | Result Sum: -1 *((-)Master + (+)Aux )| NOT OK, will
             * cancel each other out Diff: -1 *((-)Master - (+)Aux )| OK - This is what we
             * want, magnitude will be correct and positive. Diff: -1 *((+)Aux - (-)Master)|
             * NOT OK, magnitude will be correct but negative
             */

            masterConfig.diff0Term = FeedbackDevice.IntegratedSensor; // Local Integrated Sensor
            masterConfig.diff1Term = FeedbackDevice.RemoteSensor0; // Aux Selected Sensor
            masterConfig.primaryPID.selectedFeedbackSensor = FeedbackDevice.SensorDifference; // Diff0 - Diff1
        } else {
            /* Master is not inverted, both sides are positive so we can sum them. */
            masterConfig.sum0Term = FeedbackDevice.RemoteSensor0; // Aux Selected Sensor
            masterConfig.sum1Term = FeedbackDevice.IntegratedSensor; // Local IntegratedSensor
            masterConfig.primaryPID.selectedFeedbackSensor = FeedbackDevice.SensorSum; // Sum0 + Sum1
        }

        /*
         * Since the Distance is the sum of the two sides, divide by 2 so the total
         * isn't double the real-world value
         */
        masterConfig.primaryPID.selectedFeedbackCoefficient = 0.5;
    }

    public void driveToPowerCell(int xPos, int height) {
        if (height != 999) {
            twist = (double) xPos / 500;
            if (height < 50)
                y = .30;
            else
                y = 0;
        } else {
            twist = 0;
            y = 0;
        }
        System.out.println(y);
        tankDrive.arcadeDrive(y, twist);
    }

    /** Deadband 5 percent, used on the gamepad --NEED TO USE */
    private double Deadband(double value) {
        /* Upper deadband */
        if (value >= +0.05)
            return value;

        /* Lower deadband */
        if (value <= -0.05)
            return value;

        /* Outside deadband */
        return 0;
    }

    public void driveToEncoderUnits(double target_sensorUnits) {
        // double target_turn = rightMaster.getSensorCollection.get(1); // no turn
        double target_turn = 0;
        System.out.println("***********");
        double angleInput = SmartDashboard.getNumber("TargetAngle", 0);
        if (angleInput != 0) {
            target_turn = angleInput;
        } // Angle specified on screen

        System.out.println("Target sensor units:" + target_sensorUnits);
        System.out.println("Target turn:" + target_turn);

        /*
         * Configured for MotionMagic on Integrated Encoders' Sum and Auxiliary PID on
         * Pigeon
         */
        rightMaster.set(ControlMode.MotionMagic, target_sensorUnits, DemandType.AuxPID, target_turn);
        leftMaster.follow(rightMaster, FollowerType.AuxOutput1);
        rightFollower.follow(rightMaster);
        leftFollower.follow(leftMaster);

    }

    public boolean atTarget(double encoderUnits) {
        // fix it
        // double leftCurrentEncoderUnits =
        // leftMaster.getSelectedSensorPosition(Constants.kPIDLoopIdx);
        // leftCurrentEncoderUnits =
        // leftMaster.getSensorCollection().getIntegratedSensorPosition();
        double rightCurrentEncoderUnits = rightMaster.getSelectedSensorPosition(Constants.kPIDLoopIdx);
        rightCurrentEncoderUnits = rightMaster.getSensorCollection().getIntegratedSensorPosition();
        // double remainingLeft = Math.abs(leftCurrentEncoderUnits - encoderUnits);
        double remainingRight = Math.abs(rightCurrentEncoderUnits - encoderUnits);
        // if ((remainingRight < 1000) && (remainingLeft < 1000)) {
        if (remainingRight < 1000) {
            System.out.println("true");
            return true;
        }
        return false;
    }

    // public void coolMotor() {
    // if (rightMaster.getTemperature() > 50) {
    // coolingSolenoidMotors.set(true);
    // coolingOn = true;
    // } else if (coolingOn) {
    // coolingSolenoidMotors.set(false);
    // coolingOn = false;
    // }
    // }

    public void reinitializeDriveTrain() {
        // coolingSolenoidMotors.set(false);
        // timer = 0;
    }

    public void autonomousLimiting() {
        double max = 0.70;
        leftMaster.configPeakOutputForward(max, Constants.kTimeoutMs);
        leftMaster.configPeakOutputReverse(-max, Constants.kTimeoutMs);
        rightMaster.configPeakOutputForward(max, Constants.kTimeoutMs);
        rightMaster.configPeakOutputReverse(-max, Constants.kTimeoutMs);
    }

    public void teleopLimiting() {
        double max = 1.0;
        leftMaster.configPeakOutputForward(max, Constants.kTimeoutMs);
        leftMaster.configPeakOutputReverse(-max, Constants.kTimeoutMs);
        rightMaster.configPeakOutputForward(max, Constants.kTimeoutMs);
        rightMaster.configPeakOutputReverse(-max, Constants.kTimeoutMs);
        leftMaster.configOpenloopRamp(1.0,Constants.kTimeoutMs);
        rightMaster.configOpenloopRamp(1.0,Constants.kTimeoutMs);
    //    leftMaster.configOpenloopRamp(1.5,Constants.kTimeoutMs);
    //    leftMaster.configOpenloopRamp(1.5,Constants.kTimeoutMs);
    }

    public TalonFXConfiguration getRightMotor() {
        return _rightConfig;
    }
}
