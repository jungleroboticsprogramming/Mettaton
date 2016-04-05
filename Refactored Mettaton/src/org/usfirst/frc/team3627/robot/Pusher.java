package org.usfirst.frc.team3627.robot;

import edu.wpi.first.wpilibj.Servo;

/**
 * Encapsulates the implementation of a Mettaton-style pusher.
 * This pusher is a servo with only 2 states, extended and 
 * retracted. The specific positions of these states are
 * determined by retractedVal and extendedVal.
 * 
 * @author Ryan Longood
 * @version March 2016
 *
 */
public class Pusher {
	
	//the servo itself
	private Servo servo;
	
	//the value of the servo when retracted
	private double retractedVal;
	
	//the value of the servo when extended
	private double extendedVal;
	
	public double getRetractedVal() {
		return retractedVal;
	}
	
	public double getExtendedVal() {
		return extendedVal;
	}
	
	public void setRetractedVal(double value) {
		retractedVal = value;
	}
	
	public void setExtendedVal(double value) {
		extendedVal = value;
	}
	
	/**
	 * Creates a Pusher with the specified attributes
	 * 
	 * @param servoPort the port of the pusher servo
	 * @param retractedVal the value of the servo when retracted
	 * @param extendedVal the value of the servo when extended
	 * @throws IllegalArgumentException when retractedVal or extendedVal is not between 0 and 1
	 */
	public Pusher(int servoPort, double retractedVal, double extendedVal) throws IllegalArgumentException {
		
		if ((0 <= retractedVal && retractedVal <= 1) && (0 <= extendedVal && extendedVal <= 1)) {
			servo = new Servo(servoPort);
			this.retractedVal = retractedVal;
			this.extendedVal = extendedVal;
			retract();
		} else {
			throw new IllegalArgumentException(
					"retractedVal (" + retractedVal + ") and extendedVal (" + extendedVal + ") must both be between 0 and 1"
					);
		}
	}
	
	/**
	 * Extends the pusher to its extended position
	 */
	public void extend() {
		servo.set(extendedVal);
	}
	
	/**
	 * Retracts the pusher to its retracted position
	 */
	public void retract() {
		servo.set(retractedVal);
	}
}
