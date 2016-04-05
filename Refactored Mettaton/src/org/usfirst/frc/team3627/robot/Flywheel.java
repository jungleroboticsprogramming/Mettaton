package org.usfirst.frc.team3627.robot;

import edu.wpi.first.wpilibj.Talon;

/**
 * Encapsulates the implementation of a Mettaton-style flywheel.
 * This kind of flywheel is a motor with 3 states: shooting,
 * intaking, and off. Each mode has a speed that can be specified
 * during construction.
 * 
 * @author Ryan Longood
 * @version March 2016
 */

public class Flywheel implements Stoppable {
	
	//this enum represents the possible states 
	//that a flywheel can be in at any given time
	public enum FlywheelState {
		Off,
		Shooting,
		Intaking
	}
	
	//talon
	private Talon flywheelMotor;
	
	//attributes of this flywheel
	private FlywheelState state;
	private double shootSpeed;
	private double intakeSpeed;
	private int direction;
	
	public double getShootSpeed() {
		return shootSpeed;
	}
	
	public double getIntakeSpeed() {
		return intakeSpeed;
	}
	
	public FlywheelState getState() {
		return state;
	}
	
	/**
	 * Sets the state of the flywheel to the specified state and
	 * adjusts its speed to compensate.
	 * 
	 * @param newState the new state of the flywheel	 
	 */
	public void setState(FlywheelState newState) {
		state = newState;
		
			switch (state) {
			case Shooting:
				flywheelMotor.set(-shootSpeed * direction);
				break;
				
			case Intaking:
				flywheelMotor.set(intakeSpeed * direction);
				break;
				
			default:
				flywheelMotor.set(0);
				break;
		}
	}
	
	/**
	 * Creates a new flywheel with the specified attributes
	 * 
	 * @param motorPort the port of the flywheel talon
	 * @param shootSpeed the speed at which the flywheel will shoot; should be positive
	 * @param intakeSpeed the speed at which the flywheel will intake; should be positive
	 * @param counterclockwise if true, flips the direction in which this flywheel spins
	 * @throws IllegalArgumentException when shootSpeed or intakeSpeed is not between 0 and 1
	 */
	public Flywheel(int motorPort, double shootSpeed, double intakeSpeed, boolean counterclockwise) throws IllegalArgumentException {
		
		//ensure speeds are between 0 and 1
		if ((0 <= shootSpeed && shootSpeed <= 1) && (0 <= intakeSpeed && intakeSpeed <= 1)) {
			flywheelMotor = new Talon(motorPort);
			this.shootSpeed = shootSpeed;
			this.intakeSpeed = intakeSpeed;
			
			//multiply values by -1 if counterclockwise
			direction = (counterclockwise ? -1 : 1);
			setState(FlywheelState.Off);
			
			
			//speeds must be between 0 and 1
		} else {
			throw new IllegalArgumentException(
					"shootSpeed (" + shootSpeed + ") and intakeSpeed (" + intakeSpeed +
					") must both be between 0 and 1");
		}
	}
	
	/**
	 * Creates a new flywheel with the specified attributes that rotates clockwise
	 * 
	 * @param motorPort the port of the flywheel talon
	 * @param shootSpeed the speed at which the flywheel will shoot; should be positive
	 * @param intakeSpeed the speed at which the flywheel will intake; should be positive
	 * @throws IllegalArgumentException when shootSpeed or intakeSpeed is not between 0 and 1
	 */
	public Flywheel(int motorPort, double shootSpeed, double intakeSpeed) throws IllegalArgumentException {
		
		//when no direction is specified, assume clockwise
		this(motorPort, shootSpeed, intakeSpeed, false);
	}
	
	/**
	 * Stops the flywheel from spinning.
	 */
	public void stop() {
		setState(FlywheelState.Off);
	}
}
