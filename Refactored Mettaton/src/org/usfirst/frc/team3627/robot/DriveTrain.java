package org.usfirst.frc.team3627.robot;

import edu.wpi.first.wpilibj.Talon;

/**
 * Encapsulates the implementation of the Mettaton drive 
 * train. This is a tank drive-style drive train with
 * additional functionality for fine control and acceleration
 * dampening.
 * 
 * The controls are as follows:
 * Right trigger drives straight forward
 * Left trigger drives straight backward
 * Bumpers rotate slowly
 * Left stick controls left motors
 * Right stick controls right motors
 * 
 * @author Ryan Longood
 * @version March 2016
 */

public class DriveTrain implements NeedsUpdating, ControllerDrivable, DampenedStoppable {
	
	//attributes of this DriveTrain
	private double bumperRotSpeed = 0.2;
	private double straighteningThreshold = 0.2;
	private double inputScalar = 2;
	
	public double getBumperRotSpeed() {
		return bumperRotSpeed;
	}
	
	public double getStraighteningThreshold() {
		return straighteningThreshold;
	}
	
	public double getInputScalar() {
		return inputScalar;
	}
	
	public double getMaxAcceleration() {
		return leftDampener.getMaxAcceleration();
	}
	
	
	public void setBumperRotSpeed(double value) {
		bumperRotSpeed = value;
	}

	public void setStraighteningThreshold(double value) {
		straighteningThreshold = value;
	}
	
	public void setInputScalar(double value) {
		inputScalar = value;
	}
	
	public void setMaxAcceleration(double value) {
		leftDampener.setMaxAcceleration(value);
		rightDampener.setMaxAcceleration(value);
	}
	
	
	//talons
	private Talon frontLeft;
	private Talon frontRight;
	private Talon backLeft;
	private Talon backRight;
	
	//values of the sides of the drive train
	private Dampener leftDampener;
	private Dampener rightDampener;
	
	/**
	 * Creates a DriveTrain as used with Mettaton for 2016.
	 * 
	 * @param frontLeftPort the port of the front left talon
	 * @param frontRightPort the port of the front right talon
	 * @param backLeftPort the port of the back left talon
	 * @param backRightPort the port of the back right talon
	 * @param maxAcceleration the acceleration of the motors
	 */
	public DriveTrain(int frontLeftPort, int frontRightPort, int backLeftPort, int backRightPort, double maxAcceleration) {
		frontLeft = new Talon(frontLeftPort);
		frontRight = new Talon(frontRightPort);
		backLeft = new Talon(backLeftPort);
		backRight = new Talon(backRightPort);
		
		leftDampener = new Dampener(-1, 1, maxAcceleration);
		rightDampener = new Dampener(-1, 1, maxAcceleration);
	}
	
	/**
	 * Sets the values of the motors based on the given controller's input
	 * 
	 * @param controller the controller to use
	 */
	public void driveByController(RoboticsController controller) {
		
		double triggers = controller.getAxisRTrig() - controller.getAxisLTrig();
		
		//there is net bumper input only when 1 is pressed
		boolean netBumper = (controller.getButtonRB() != controller.getButtonLB());
		
		//drive with triggers if there is trigger input
		if (Math.abs(triggers) > 0.05) {
			
			//drive by triggers: drive straight
			double scaledTriggers = getScaledValue(triggers);
			
	    		leftDampener.setTargetVal(scaledTriggers);
			rightDampener.setTargetVal(scaledTriggers);
			
			
			
			//drive with bumpers if there is (net) bumper input
		} else if (netBumper) {
			
			//drive by bumpers: rotate slowly
			double direction = bumperRotSpeed;
			
			//determine direction (- is right, + is left)
			if (controller.getButtonLB()) {
				direction *= -1;
			}
			
			//apply the rotation values
			leftDampener.setTargetVal(direction);
			rightDampener.setTargetVal(-direction);
			
			
			
			//drive with sticks
		} else {
			double leftVal = controller.getAxisLY();
	    	double rightVal = controller.getAxisRY();
	    	
	    	//average the 2 values if within threshold of each other
	    	if (Math.abs(leftVal - rightVal) <= straighteningThreshold) {
		    	double average = (leftVal + rightVal) / 2;
		    	leftVal = average;
		    	rightVal = average;
	    	}
	
	    	leftDampener.setTargetVal(getScaledValue(leftVal));
	    	rightDampener.setTargetVal(getScaledValue(rightVal));
    	}
		
		applyDampenerValues();
	}
	
	/**
	 * Applies a given speed to the motors in order to drive straight
	 * 
	 * @param speed the speed at which to drive
	 */
	public void driveStraight(double speed) {
		leftDampener.setTargetVal(speed);
		rightDampener.setTargetVal(speed);
		applyDampenerValues();
	}
	
	/**
	 * Applies a given speed to the motors in order to rotate
	 * 
	 * @param rate The speed at which to rotate. Negative values
	 * rotate to the left, and positive values rotate to the right.
	 */
	public void driveRotate (double rate) {
		leftDampener.setTargetVal(rate);
		rightDampener.setTargetVal(-rate);
		applyDampenerValues();
	}
	
	/**
	 * Actually applies the dampener values to the talons
	 */
	private void applyDampenerValues() {
		frontLeft.set(leftDampener.getCurrentVal());
		backLeft.set(leftDampener.getCurrentVal());
		
		//the right side is negated to compensate 
		//for the stupidity of the electrical team
		frontRight.set(-rightDampener.getCurrentVal());
		backRight.set(-rightDampener.getCurrentVal());
	}
	
	/**
	 * Scales the given value through exponentiation to provide finer control
	 * 
	 * @param value the value to be scaled
	 * @return the scaled value
	 */
	protected double getScaledValue(double value) {
		double scaled = Math.pow(value, inputScalar);
    	
    	//if the scaled value is positive but the 
    	//input is negative (from an even exponent), 
    	//make negative
    	if (scaled > 0 && value < 0) {
    		scaled *= -1;
    	}
    	
    	return scaled;
	}
	
	/**
	 * Steps the motor speeds closer to the target speeds
	 */
	public void update() {
		leftDampener.update();
		rightDampener.update();
		applyDampenerValues();
	}
	
	/**
	 * Stops the motors gradually.
	 */
	public void stop() {
		driveStraight(0);
	}
	
	/**
	 * Immediately stops the motors without decelerating. Not recommended
	 * unless it is needed to prevent something from breaking.
	 */
	public void immediateStop() {
		frontLeft.set(0);
		frontRight.set(0);
		backLeft.set(0);
		backRight.set(0);
		
		leftDampener.setTargetVal(0);
		rightDampener.setTargetVal(0);
		
	}
}
