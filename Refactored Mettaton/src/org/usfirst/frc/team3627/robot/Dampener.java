package org.usfirst.frc.team3627.robot;

/**
 * Encapsulates a value that can be gradually stepped up to a target 
 * value. This is useful for controlling anything that must be spun 
 * up or accelerated gradually instead of instantly. 
 * 
 * As an example, a motor governed by a Dampener value allows for smooth
 * acceleration, eliminating jerking that might harm robot components.
 * 
 * Remember that a class that encapsulates a Dampener to use in this way 
 * needs to implement the interface NeedsUpdating and added to the
 * Vector of NeedsUpdating objects in the Robot class so that the 
 * dampener values are periodically updated.
 * 
 * @author Ryan Longood
 * @version March 2016
 */
public class Dampener implements NeedsUpdating {
	
	//boundaries for the value
	private double minVal;
	private double maxVal;
	
	//current value of the dampener
	private double currentVal;
	
	//value the dampener will attempt to reach
	private double targetVal;
	
	//maximum change in value per step
	private double maxAcceleration;
	
	public double getCurrentVal() {
		return currentVal;
	}
	
	public double getTargetVal() {
		return targetVal;
	}
	
	public void setTargetVal(double target) {
		
		//clamp between min and max
		if (target < minVal) {
			targetVal = minVal;
		} else if (target > maxVal) {
			targetVal = maxVal;
		} else {
			
			//set normally
			targetVal = target;
		}
	}
	
	public double getMaxAcceleration() {
		return maxAcceleration;
	}
	
	public void setMaxAcceleration(double acceleration) {
		maxAcceleration = acceleration;
	}
	
	/**
	 * Creates a Dampener with the specified attributes
	 * 
	 * @param minVal the minimum value of the dampener
	 * @param maxVal the maximum value of the dampener
	 * @param maxAcceleration the maximum change in value per update
	 */
	public Dampener(double minVal, double maxVal, double maxAcceleration) {
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.maxAcceleration = maxAcceleration;
		
		//set currentVal to be the midpoint of bounds
		currentVal = (minVal + maxVal) / 2;
	}
	
	/**
	 * Causes the current value of the dampener to approach the target value
	 */
	public void update() {
		
		//simply set the value if close enough
		if (Math.abs(currentVal - targetVal) <= maxAcceleration) {
			currentVal = targetVal;
		}
		
		//otherwise, step normally
		
		//determine direction
		double direction = Math.signum(targetVal - currentVal);
		
		//step by maxAcceleration in determined direction
		currentVal += maxAcceleration * direction;
	}
}
