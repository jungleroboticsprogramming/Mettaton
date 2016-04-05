package org.usfirst.frc.team3627.robot;

import edu.wpi.first.wpilibj.Talon;

/**
 * Encapsulates the implementation of a Mettaton-style winch.
 * This kind of winch has a single motor, along with 2 limit switches
 * that set the boundaries of the winch.
 * 
 * @author Ryan Longood
 * @version March 2016
 *
 */
public class Winch implements NeedsUpdating, DampenedStoppable {
	
	//used to prevent jerking of the winch movement
	private Dampener motorDampener;
	
	private Talon motor;
	private LimitSwitch lowerLimit;
	private LimitSwitch upperLimit;
	private double speed;
	
	/**
	 * Creates a Winch as used with Mettaton for 2016.
	 * 
	 * @param motorPort the port of the winch motor
	 * @param lowerLimitPort the port of the lower boundary limit switch
	 * @param upperLimitPort the port of the upper boundary limit switch
	 * @param speed the speed at which the winch moves
	 * @param maxAcceleration the maximum acceleration of the winch
	 */
	public Winch(int motorPort, int lowerLimitPort, int upperLimitPort, double speed, double maxAcceleration) {
		motor = new Talon(motorPort);
		lowerLimit = new LimitSwitch(lowerLimitPort);
		upperLimit = new LimitSwitch(upperLimitPort);
		this.speed = speed;
		motorDampener = new Dampener(-1, 1, maxAcceleration);
	}
	
	protected Winch(Winch other) {
		motor = other.motor;
		lowerLimit = other.lowerLimit;
		upperLimit = other.upperLimit;
		speed = other.speed;
		motorDampener = other.motorDampener;
	}
	/**
	 * Since winches can move faster downward than upward,
	 * this method allows you to specify a multiplier for
	 * the speed to compensate.
	 * 
	 * @param multiplier the multiplier for the speed
	 */
	public void lower(double multiplier) {
		if (lowerLimit.isPressed()) {
			
			//physical limit of the limit switch reached
			immediateStop();
		} else {
			motorDampener.setTargetVal(-speed * multiplier);
			applyDampenerValues();
		}
	}
	
	/**
	 * Lowers the winch, ya dingus
	 */
	public void lower() {
		
		//remember to not repeat yourself
		lower(1);
	}
	
	/**
	 * Stops the winch, ya dingus
	 */
	public void stop() {
		motorDampener.setTargetVal(0);
		applyDampenerValues();
	}
	
	/**
	 * Immediately stops the winch without decelerating. Not recommended
	 * unless it is needed to prevent something from breaking.
	 */
	public void immediateStop() {
		motor.set(0);
		motorDampener.setTargetVal(0);
	}
	
	/**
	 * Since winches can move faster upward than downward,
	 * this method allows you to specify a multiplier for
	 * the speed to compensate.
	 * 
	 * @param multiplier the multiplier for the speed
	 */
	public void raise(double multiplier) {
		if (upperLimit.isPressed()) {
			
			//physical limit of the limit switch reached
			immediateStop();
		} else {
			motorDampener.setTargetVal(speed * multiplier);
			applyDampenerValues();
		}
	}
	
	/**
	 * Raises the winch, ya dingus
	 */
	public void raise() {
		
		//remember to not repeat yourself
		raise(1);
	}
	
	/**
	 * Returns whether the winch is fully up.
	 * 
	 * @return true if the top limit switch is pressed
	 */
	public boolean isFullyUp() {
		return upperLimit.isPressed();
	}
	
	/**
	 * Returns whether the winch is fully down.
	 *
	 * @return true if the bottom limit switch is pressed
	 */
	public boolean isFullyDown() {
		return lowerLimit.isPressed();
	}
	
	/**
	 * Steps the current speed of the winch closer to the target speed
	 */
	public void update() {
		motorDampener.update();
		applyDampenerValues();
	}
	
	/**
	 * Actually applies the dampener values to the motor talon
	 */
	private void applyDampenerValues() {
		motor.set(motorDampener.getCurrentVal());
	}
}
