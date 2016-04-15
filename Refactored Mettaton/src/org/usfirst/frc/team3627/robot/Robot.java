package org.usfirst.frc.team3627.robot;

import java.util.Vector;

import edu.wpi.first.wpilibj.*;

/**
 * This class is the entry point for the robot code. All of the magic happens here.
 *
 * @author Ryan Longood
 * @version March 2016
 */

public class Robot extends IterativeRobot {
	
	//this constant holds the number of times 
	//the periodic functions execute per second
	static final double TICKS_PER_SEC = 50;
	
	//channels
	final int CHAN_CONTROLLER = 0;
	
	final int CHAN_FRONT_LEFT = 0;
	final int CHAN_BACK_LEFT = 1;
	final int CHAN_FRONT_RIGHT = 2;
	final int CHAN_BACK_RIGHT = 3;
	
	final int CHAN_PUSHER = 4;
	final int CHAN_WINCH = 5;
	final int CHAN_LEFT_FLY = 6;
	final int CHAN_RIGHT_FLY = 7;
	
	final int CHAN_LOWER_LS = 1;
	final int CHAN_UPPER_LS = 0;
	
	
	
	//robot attributes
	final double DRIVE_ACC = 4.5 / TICKS_PER_SEC;
	
	final double WINCH_ACC = 10 / TICKS_PER_SEC;
	final double WINCH_SPD = 0.8;
	
	final double PUSH_RETRACTED = 0.15;
	final double PUSH_EXTENDED = 0.55;
	
	final double FLY_SHOOT_SPD = 1;
	final double FLY_INTAKE_SPD = 0.4;
	
	
	//fields
	boolean winchPositioned;
	int autonCounter;		//keeps time in autonomous
	int teleCounter;
	
	RoboticsController controller;
	
	DriveTrain driveTrain;
	Launcher launcher;
	CameraServer server;
	
	//Vectors
	Vector<NeedsUpdating> needsUpdating;
	Vector<ControllerDrivable> controllerDriven;
	
	
	
	/**
	 * This function is called when the code is first executed.
	 * 
	 * Use it to construct objects and configure settings for the bot.
	 */
    public void robotInit() {
    	
    	//commence death by glamour
	    System.out.println("Ohhh yessssss...");
	    
    	//initialize components
    	controller = new RoboticsController(CHAN_CONTROLLER);
    	
    	//configure launcher components
    	Winch winch = new Winch(CHAN_WINCH, CHAN_LOWER_LS, CHAN_UPPER_LS,
    			WINCH_SPD, WINCH_ACC);
    	
    	Pusher pusher = new Pusher(CHAN_PUSHER, 
    			PUSH_RETRACTED, PUSH_EXTENDED);
    	
    	//configure launcher
    	launcher = new Launcher(winch, pusher, 
    			CHAN_LEFT_FLY, CHAN_RIGHT_FLY,
    			FLY_SHOOT_SPD, FLY_INTAKE_SPD);
    	
    	//configure driveTrain
    	driveTrain = new DriveTrain(
    			CHAN_FRONT_LEFT, CHAN_FRONT_RIGHT, CHAN_BACK_LEFT, CHAN_BACK_RIGHT,
    			DRIVE_ACC);
    	
    	//configure camera feed (name obtained from roboRIO web interface)
    	server = CameraServer.getInstance();
    	server.setQuality(30);
    	server.startAutomaticCapture("cam0");
    	
    	
    	
    	//Add any NeedsUpdating components to the Vector here.
    	//Each will automatically be updated
    	needsUpdating = new Vector<NeedsUpdating>();
    	needsUpdating.add(driveTrain);
    	needsUpdating.add(launcher);
    	needsUpdating.add(controller);
    	
    	//Add any ControllerDrivable components to the Vector here.
    	//Each will automatically be driven by the controller
    	controllerDriven = new Vector<ControllerDrivable>();
    	controllerDriven.add(driveTrain);
    	controllerDriven.add(launcher);
    }
    
    /**
     * This function is called when autonomous is first entered
     */
    public void autonomousInit() {
    	autonCounter = 0;
    	winchPositioned = false;
    }
    
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	
    	//position winch
    	if (!winchPositioned) {
    		if (launcher.isFullyDown()) {
    			
        		//winch is positioned
    			winchPositioned = true;
    			launcher.stop();
    		} else {
    			
    			//winch not yet positioned
    			launcher.lower();
    		}
    		
		//once positioned, move forward
    	} else {
	    	if (0 <= autonCounter && autonCounter <= 5 * TICKS_PER_SEC) {
	    		driveTrain.driveStraight(0.5);
	    	} else {
	    		driveTrain.stop();
	    	}
	    	
	    	autonCounter++;
	    }

    	updateComponents();
    }
    
    /**
     * This function is called periodically during tele-operated
     */
    public void teleopPeriodic() {
    	
    	//drive each component
    	driveComponents();
    	
    	//update state of each component
    	updateComponents();
    }
    
    /**
     * Updates each component that needs to be updated.
     * This should be called in every periodic function.
     * Otherwise the values of the components will not
     * be updated.
     */
    public void updateComponents() {
    	
    	//for each component of type NeedsUpdating in needsUpdating vector:
    	for (NeedsUpdating component : needsUpdating) {
    		component.update();
    	}
    }
    
    /**
     * Drives each component that is driven by a
     * controller.
     */
    public void driveComponents() {
    	
    	//for each component of type ControllerDrivable in controllerDriven vector:
    	for (ControllerDrivable component : controllerDriven) {
    		component.driveByController(controller);
    	}
    }
    
    public void winGame() {
    	//not yet implemented
    }
}
