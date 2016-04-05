package org.usfirst.frc.team3627.robot;

/**
 * Classes that implement this interface can be driven by
 * a RoboticsController.
 * 
 * By implementing this interface, all classes driven by
 * a controller can be put into a single Vector of the type
 * ControllerDriven. Then, all components can easily be driven
 * by iterating through the vector and calling the method.
 * 
 * @author Ryan Longood
 * @version March 2016
 *
 */

public interface ControllerDrivable {
	public void driveByController(RoboticsController controller);
}
