package org.firstinspires.ftc.teamcode.AutoCode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.MovingStatistics;

import org.firstinspires.ftc.teamcode.AutoCode.Control.AcceleratedGain;
import org.firstinspires.ftc.teamcode.AutoCode.Trajectory.Base.StateMMovmentPerformer;
import org.firstinspires.ftc.teamcode.AutoCode.Trajectory.Base.StateMPointApproach;
import org.firstinspires.ftc.teamcode.AutoCode.Trajectory.Base.StateMTrajectory;
import org.firstinspires.ftc.teamcode.AutoCode.Trajectory.SpikeFinish;
import org.firstinspires.ftc.teamcode.Globals;
import org.firstinspires.ftc.teamcode.TrackingWheelIntegrator;
import org.firstinspires.ftc.teamcode.drivebase.CenterStageDriveBase;

import static org.firstinspires.ftc.teamcode.Globals.SpikeIsFinished;
import static org.firstinspires.ftc.teamcode.Globals.X;
import static org.firstinspires.ftc.teamcode.Globals.Y;
import static org.firstinspires.ftc.teamcode.Globals.trackingWheelIntegrator;
import static org.firstinspires.ftc.teamcode.Globals.wheelH;


@Autonomous(preselectTeleOp = "MecDrive")
public class BackStagePRIMARYAuto extends LinearOpMode {

//    TrackingWheelIntegrator trackingWheelIntegrator = new TrackingWheelIntegrator();

    CenterStageDriveBase centerStageDriveBase;

    private DcMotorEx FL;
    private DcMotorEx FR;
    private DcMotorEx RL;
    private DcMotorEx RR;

    private DcMotorEx rightHook;
    private DcMotorEx leftHook;
    private DcMotorEx intake;
    private DcMotorEx Lift;
    private DcMotorEx RHang;
    private DcMotorEx LHang;
    private Servo RHook;
    private Servo LHook;
    private Servo intakeLift;
    private Servo airplane;
    private Servo Door;
    private Servo RLock;
    private Servo LLock;
    private Servo Pivot;
    private Servo SLift;

    private float Rservopos;
    private float Lservopos;

    private double leftStickX;
    private double leftStickY;
    private double rightStickX;
    private int LiftCounts;
    private float speedFactor;


    private static DcMotorEx LeftTW;
    private static DcMotorEx RightTW;
    private static DcMotorEx BackTW;
//    public static double Y;
//    public static double X;
//    public static double wheelH;



//      INT STATEM
        StateMTrajectory CenterSpike;
        StateMTrajectory LeftSpike;
        StateMTrajectory RightSpike;
        StateMTrajectory CenterPlace;
        StateMTrajectory LeftPlace;
        StateMTrajectory RightPlace;
//        StateMTrajectory FirstMovement;
//        StateMTrajectory FirstMovement;
//


//      INT Movment Trajectory



    //Basics For Tracking
        MovingStatistics movingStatistics = new MovingStatistics(300);
//        static LynxDcMotorController ctrl;
//        LynxModule module;
        CenterStageDriveBase PowerPlayDrivebase;
        StateMTrajectory trajectory;




        @Override
        public void runOpMode() throws InterruptedException
        {

            trackingWheelIntegrator = new TrackingWheelIntegrator();

            FL= (DcMotorEx) hardwareMap.get(DcMotorEx.class, "FL");
            FR= (DcMotorEx) hardwareMap.get(DcMotorEx.class, "FR");
            RL= (DcMotorEx) hardwareMap.get(DcMotorEx.class, "RL");
            RR= (DcMotorEx) hardwareMap.get(DcMotorEx.class, "RR");

            intake= (DcMotorEx) hardwareMap.get(DcMotorEx.class, "intake");
            Lift = (DcMotorEx) hardwareMap.get(DcMotorEx.class, "Lift");
            LHang = (DcMotorEx) hardwareMap.get(DcMotorEx.class, "LHang");
            RHang = (DcMotorEx) hardwareMap.get(DcMotorEx.class, "RHang");


            intakeLift=(Servo) hardwareMap.get(Servo.class, "intakeLift");
            airplane=(Servo)  hardwareMap.get(Servo.class, "airplane");
            Door=(Servo)  hardwareMap.get(Servo.class, "Door");
            RLock=(Servo)  hardwareMap.get(Servo.class, "RLock");
            LLock=(Servo)  hardwareMap.get(Servo.class, "LLock");
            Pivot=(Servo)  hardwareMap.get(Servo.class, "Pivot");
            SLift=(Servo)  hardwareMap.get(Servo.class, "SLift");
            RHook=(Servo)  hardwareMap.get(Servo.class, "RHook");
            LHook=(Servo)  hardwareMap.get(Servo.class, "LHook");

            LeftTW = hardwareMap.get(DcMotorEx.class, "LHang");
            RightTW = hardwareMap.get(DcMotorEx.class, "RHang");
            BackTW = hardwareMap.get(DcMotorEx.class, "intake");


            centerStageDriveBase = new CenterStageDriveBase();
            centerStageDriveBase.init(hardwareMap);
            centerStageDriveBase.resetEncoders();
            centerStageDriveBase.enableBrake(true);
            centerStageDriveBase.enablePID();
            Globals.robot=centerStageDriveBase;
            Globals.driveBase=centerStageDriveBase;
            Globals.opMode = this;
            trackingWheelIntegrator = trackingWheelIntegrator;

            telemetry.setMsTransmissionInterval(20);

            telemetry.setMsTransmissionInterval(20);

            //Setting Start Location in Auto.
            trackingWheelIntegrator.setFirstTrackingVal(-96,-1);

            clearEnc();

            while (!isStarted() && !isStopRequested())
            {
                //Vision During INT
            }

    buildTrajectory(); //Command for State Mechine.

            while (opModeIsActive()) {

                int left = -LeftTW.getCurrentPosition();
                int right = -RightTW.getCurrentPosition();
                int aux = -BackTW.getCurrentPosition();
                trackingWheelIntegrator.update(left, right, aux);

                Y = trackingWheelIntegrator.getY();
                X = trackingWheelIntegrator.getX();
                wheelH = trackingWheelIntegrator.getHeading();

                telemetry.addData("X", X);
                //opMode.telemetry.addData("FrontInch", inch);
                telemetry.addData("Y", Y);
                telemetry.addData("wheelH", wheelH);
                telemetry.update();

                if (!SpikeIsFinished) {
                    CenterSpike.followInteration();
                }
                if (SpikeIsFinished) {
                    CenterPlace.followInteration();
                }


            }

        }

    public static void clearEnc()        {
            LeftTW.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            RightTW.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            BackTW.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            LeftTW.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            RightTW.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            BackTW.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }


    public void buildTrajectory() {



        CenterSpike = new StateMTrajectory.Builder()

                .addMovement(new StateMPointApproach.Builder()
                        .setTargetPosition(92,-28)
                        .setMaxPower(.35) // .4 - 6
                        .setXyGain(.04)
                        .setTargetHeading(0)
                        .setHeadingDynamicGain(new AcceleratedGain(.012, -0.0004))
                        .setMaxTurnPower(.2)
                        .setMovementThresh(1)
                        .setHeadingThreshold(2)
                        .stopMotorsOnDone(true)
                        .build())

                .addMovement(new StateMPointApproach.Builder() //First movment.
                        .setTargetPosition(92,-24)
                        .setMaxPower(.4) // .4 - 6
                        .setXyGain(.04)
                        .setTargetHeading(0)
                        .setHeadingDynamicGain(new AcceleratedGain(.012, -0.0004))
                        .setMaxTurnPower(.2)
                        .setMovementThresh(2)
                        .setHeadingThreshold(2)
                        .stopMotorsOnDone(true)
                        .build())
                .addMovement((StateMMovmentPerformer) new SpikeFinish())
                .build();

        LeftSpike = new StateMTrajectory.Builder()

                .addMovement(new StateMPointApproach.Builder()
                        .setTargetPosition(101,-24)
                        .setMaxPower(.35) // .4 - 6
                        .setXyGain(.04)
                        .setTargetHeading(0)
                        .setHeadingDynamicGain(new AcceleratedGain(.012, -0.0004))
                        .setMaxTurnPower(.2)
                        .setMovementThresh(1)
                        .setHeadingThreshold(2)
                        .stopMotorsOnDone(true)
                        .build())

                .addMovement(new StateMPointApproach.Builder() //First movment.
                        .setTargetPosition(92,-20)
                        .setMaxPower(.4) // .4 - 6
                        .setXyGain(.04)
                        .setTargetHeading(0)
                        .setHeadingDynamicGain(new AcceleratedGain(.012, -0.0004))
                        .setMaxTurnPower(.2)
                        .setMovementThresh(2)
                        .setHeadingThreshold(2)
                        .stopMotorsOnDone(true)
                        .build())
                .build();

        RightSpike = new StateMTrajectory.Builder()

                .addMovement(new StateMPointApproach.Builder()
                        .setTargetPosition(88,-26)
                        .setMaxPower(.35) // .4 - 6
                        .setXyGain(.04)
                        .setTargetHeading(17)
                        .setHeadingDynamicGain(new AcceleratedGain(.012, -0.0004))
                        .setMaxTurnPower(.3)
                        .setMovementThresh(1)
                        .setHeadingThreshold(2)
                        .stopMotorsOnDone(true)
                        .build())

                .addMovement(new StateMPointApproach.Builder() //First movment.
                        .setTargetPosition(90,-22)
                        .setMaxPower(.4) // .4 - 6
                        .setXyGain(.04)
                        .setTargetHeading(17)
                        .setHeadingDynamicGain(new AcceleratedGain(.012, -0.0004))
                        .setMaxTurnPower(.2)
                        .setMovementThresh(2)
                        .setHeadingThreshold(2)
                        .stopMotorsOnDone(true)
                        .build())
                .build();

        CenterPlace = new StateMTrajectory.Builder()

                .addMovement(new StateMPointApproach.Builder()
                        .setTargetPosition(92,-24)
                        .setMaxPower(.35) // .4 - 6
                        .setXyGain(.04)
                        .setTargetHeading(-90)
                        .setHeadingDynamicGain(new AcceleratedGain(.012, -0.0004))
                        .setMaxTurnPower(.2)
                        .setMovementThresh(1)
                        .setHeadingThreshold(2)
                        .stopMotorsOnDone(true)
                        .build())

                .addMovement(new StateMPointApproach.Builder() //First movment.
                        .setTargetPosition(131,-24)
                        .setMaxPower(.4) // .4 - 6
                        .setXyGain(.04)
                        .setTargetHeading(0)
                        .setHeadingDynamicGain(new AcceleratedGain(.012, -0.0004))
                        .setMaxTurnPower(.2)
                        .setMovementThresh(2)
                        .setHeadingThreshold(2)
                        .stopMotorsOnDone(true)
                        .build())
                .build();

        LeftPlace = new StateMTrajectory.Builder()

                .addMovement(new StateMPointApproach.Builder()
                        .setTargetPosition(92,-30)
                        .setMaxPower(.35) // .4 - 6
                        .setXyGain(.04)
                        .setTargetHeading(0)
                        .setHeadingDynamicGain(new AcceleratedGain(.012, -0.0004))
                        .setMaxTurnPower(.2)
                        .setMovementThresh(1)
                        .setHeadingThreshold(2)
                        .stopMotorsOnDone(true)
                        .build())

                .addMovement(new StateMPointApproach.Builder() //First movment.
                        .setTargetPosition(92,-24)
                        .setMaxPower(.4) // .4 - 6
                        .setXyGain(.04)
                        .setTargetHeading(0)
                        .setHeadingDynamicGain(new AcceleratedGain(.012, -0.0004))
                        .setMaxTurnPower(.2)
                        .setMovementThresh(2)
                        .setHeadingThreshold(2)
                        .stopMotorsOnDone(true)
                        .build())
                .build();

        RightPlace = new StateMTrajectory.Builder()

                .addMovement(new StateMPointApproach.Builder()
                        .setTargetPosition(92,-30)
                        .setMaxPower(.35) // .4 - 6
                        .setXyGain(.04)
                        .setTargetHeading(0)
                        .setHeadingDynamicGain(new AcceleratedGain(.012, -0.0004))
                        .setMaxTurnPower(.2)
                        .setMovementThresh(1)
                        .setHeadingThreshold(2)
                        .stopMotorsOnDone(true)
                        .build())

                .addMovement(new StateMPointApproach.Builder() //First movment.
                        .setTargetPosition(92,-24)
                        .setMaxPower(.4) // .4 - 6
                        .setXyGain(.04)
                        .setTargetHeading(0)
                        .setHeadingDynamicGain(new AcceleratedGain(.012, -0.0004))
                        .setMaxTurnPower(.2)
                        .setMovementThresh(2)
                        .setHeadingThreshold(2)
                        .stopMotorsOnDone(true)
                        .build())
                .build();

    }
}
