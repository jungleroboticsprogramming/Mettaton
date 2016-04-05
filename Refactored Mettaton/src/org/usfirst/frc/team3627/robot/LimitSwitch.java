package org.usfirst.frc.team3627.robot;

import edu.wpi.first.wpilibj.*;

/**
 * DigitalInputs are confusing. This class doesn't
 * really add any functionality other than clearing
 * up a major ambiguity of DigitalInputs: what the
 * hell does the boolean value even represent???
 * 
 * The only thing this class adds to the base class
 * of DigitalInput is a method that returns true if
 * the limit switch is toggled.
 * 
 * @author Ryan Longood
 * @version March 2016
 */
public class LimitSwitch extends DigitalInput {

	/**
	 * Constructs a LimitSwitch with the specified channel.
	 * 
	 * @param channel the DIO channel of the limit switch
	 */
	public LimitSwitch(int channel) {
		
		//call the constructor of the base class (DigitalInput)
		super(channel);
	}
	
	/**
	 * Returns whether the limit switch is being pressed.
	 * 
	 * @return true if the limit switch is pressed
	 */
	public boolean isPressed() {
		return !this.get();
	}
}
