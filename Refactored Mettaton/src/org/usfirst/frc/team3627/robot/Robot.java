package org.usfirst.frc.team3627.robot;

import java.util.Vector;

import edu.wpi.first.wpilibj.*;

/* 
 * This is a robust object-oriented implementation of the code
 * that drives Mettaton 2016. The classes here are designed so
 * that they can be easily reused for years to come; hopefully,
 * this will save anybody reading this a good amount of time.
 * 
 * However, this code does assume knowledge of some advanced
 * object-oriented concepts. Though I certainly recommend coding
 * this way if you have the knowledge, you should only use this
 * as a reference if you do not.
 * 
 * However, I do recommend that you reuse at least a few of the
 * classes. The Dampener class is a very general class that can
 * be used in a huge number of applications. In this code,
 * they are used to aid with motor acceleration so that the bot
 * does not jerk around when the driver decides to suddenly apply
 * input.
 * 
 * The RoboticsController class has source code that might look 
 * messy, but using it makes it easier to read controller input
 * that you know is going to be from an Xbox controller.
 * 
 * Both of these classes implement the NeedsUpdating interface.
 * Dampeners need to be updated at constant intervals so that their
 * values actually approach their target values. RoboticsControllers
 * need to constantly read and save their input states so that
 * edge detection works properly, which is used with button toggling.
 * As a result, you would need to reuse this interface as well, 
 * which defines the method that contains the code that updates.
 * 
 * The interface makes it easy to store any objects that need
 * updating in a single Vector. You can update everything at once
 * by simply iterating through this Vector and calling the
 * update() method in all periodic robot functions. You
 * can see an example of this in the code below.
 * 
 * This makes updating objects incredibly easy. Similarly, you can
 * cut down on mapping controls to different actions on the bot
 * by implementing the ControllerDrivable interface in your robot
 * component objects, defining the driveByController() method to
 * perform actions based on controller input within your class, 
 * and using a Vector as before.
 * 
 * The other classes are mostly specific to Mettaton. The only 
 * exception might be the LimitSwitch class, which is a lightweight
 * class that clears up the ambiguous boolean output of digital
 * inputs.
 * 
 * Visual recognition is not included in this code, because visual
 * recognition kinda failed with Mettaton. We didn't have an Axis
 * Camera, which allows configuration of the image feed via the
 * roboRIO web interface. As a result, the camera picked up on
 * the overhead lighting and other objects, which messed with the
 * auto aiming and visual recognition. If you want to attempt it
 * in the future, make sure to use an Axis Camera and reduce its
 * brightness. Putting a light ring around the camera will then
 * cause the target to glow (assuming that the target is made 
 * from retro-reflective tape like it was for 2016). With the
 * customized Axis Camera settings, the target should be the
 * only bright object within the image, which should work. Use
 * the sample visual recognition code projects to learn how
 * targets are recognized. Once the targets are recognized,
 * the bot can be rotated toward it by computing the center
 * of the target in pixels, the offset of the target center
 * from the center of the camera, and converting that pixel
 * offset into an angle offset based off of the FOV and pixel
 * density of the camera. A gyro can then be used to gradually
 * reduce this discrepancy.
 * 
 *  
 * Good luck to any future programmers!!! You are the lifeblood
 * of the team, yet for some reason nobody will appreciate
 * you. When the electrical or mechanical team messes up, no
 * one will bat an eye even though their mistakes can cause
 * hours of time to be lost. Yet if you acknowledge the
 * presence of a single one-line bug in your code that you can
 * fix in 15 seconds, everybody loses their damn minds. Only
 * programmers can understand the problems that programmers face.
 * I'm no exception, and I truly empathize with your struggles. 
 * 
 * That being said, just remember that the robot wouldn't be a robot
 * without you-- it would just be a giant hunk of expensive scrap metal.
 * 
 * GLHF
 *  
 *
 *
 * --Ryan Longood, Programming Captain 2016
 *  
 *  
 * Feel free to contact me for any help you may need:
 * longoodr@gmail.com
 * (804) 840-7372
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
    	for (NeedsUpdating component : needsUpdating) {
    		component.update();
    	}
    }
    
    /**
     * Drives each component that is driven by a
     * controller.
     */
    public void driveComponents() {
    	for (ControllerDrivable component : controllerDriven) {
    		component.driveByController(controller);
    	}
    }
    
    public void winGame() {
    	//not yet implemented
    }
}