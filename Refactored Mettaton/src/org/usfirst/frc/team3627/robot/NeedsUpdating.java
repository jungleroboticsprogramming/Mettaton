package org.usfirst.frc.team3627.robot;

/**
 * Classes that implement this interface need
 * some sort of consistent refreshing during
 * periodic robot modes to function properly.
 * 
 * These update events may be stepping an internal
 * Dampener (as is the case with the Winch class)
 * or updating edge values (as with the 
 * RoboticsController class).
 * 
 * By implementing this interface, all classes that need 
 * updating can be put into a single Vector of the type
 * NeedsUpdating. Then, all components can easily be updated
 * by iterating through the vector and calling the method.
 * 
 * @author Ryan Longood
 * @version March 2016
 */
public interface NeedsUpdating {

	public void update();
}
