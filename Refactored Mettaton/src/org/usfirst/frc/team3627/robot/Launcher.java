package org.usfirst.frc.team3627.robot;

/**
 * Encapsulates a Mettaton-style launcher. This launcher
 * has a winch to adjust the vertical angle, 2 flywheels
 * for intaking and shooting, and a pusher to push the ball
 * into the flywheels for shooting.
 * 
 * The controls are as follows:
 * Start toggles between flywheel shooting and off
 * Back toggles flywheel intaking and off
 * Y raises the launcher
 * A lowers the launcher
 * X extends the pusher for the duration of the press
 * 
 * 
 * @author Ryan Longood
 * @version March 2016
 */
public class Launcher extends Winch implements NeedsUpdating, ControllerDrivable {
	
	//Launcher inherits from winch, so there is no field for one.
	
	//flywheels
	private Flywheel leftFly;
	private Flywheel rightFly;
	
	//pusher
	private Pusher ballPusher;
	
	public Flywheel.FlywheelState getFlywheelState() {
		return leftFly.getState();
	}
	
	/**
	 * Creates a Launcher as used with Mettaton 2016.
	 * 
	 * @param winch the winch to use
	 * @param ballPusher the pusher to use
	 * @param leftFlyChannel the PWM channel of the left flywheel
	 * @param rightFlyChannel the PWM channel of the right flywheel
	 * @param shootSpeed the speed at which the flywheels will spin to shoot
	 * @param intakeSpeed the speed at which the flywheels will spin to intake
	 */
	public Launcher(Winch winch, Pusher ballPusher, int leftFlyChannel, int rightFlyChannel, double shootSpeed, double intakeSpeed) {
		
		//call the base's constructor
		super(winch);
		
		//now do launcher-specific things:
		this.ballPusher = ballPusher;
		
		leftFly = new Flywheel(leftFlyChannel, shootSpeed, intakeSpeed, false);
		rightFly = new Flywheel(rightFlyChannel, shootSpeed, intakeSpeed, true);
		setFlywheelState(Flywheel.FlywheelState.Off);
	}
	
	/**
	 * Manipulates the launcher based on the given controller's input.
	 * 
	 * The controls are as follows:
	 * Start toggles between flywheel shooting and off
	 * Back toggles flywheel intaking and off
	 * Y raises the launcher
	 * A lowers the launcher
	 * X extends the pusher for the duration of the press
	 * 
	 * @param controller the controller to use
	 */
	public void driveByController(RoboticsController controller) {
		
		//toggle flywheel state
		if (controller.getButtonEdgeStart()) {
			
			
			//toggle between shooting and off
			if (leftFly.getState() == Flywheel.FlywheelState.Off) {
				setFlywheelState(Flywheel.FlywheelState.Shooting);
			} else {
				setFlywheelState(Flywheel.FlywheelState.Off);
			}
		} else if (controller.getButtonEdgeBack()) {
			
			
			//toggle between intaking and off
			if (leftFly.getState() == Flywheel.FlywheelState.Off) {
				setFlywheelState(Flywheel.FlywheelState.Intaking);
			} else {
				setFlywheelState(Flywheel.FlywheelState.Off);
			}
		}
		
		//move winch
		if (controller.getButtonY()) {
			raise();
		} else if (controller.getButtonA()) {
			lower(0.5);
		} else {
			stop();
		}
		
		//move pusher
		if (controller.getButtonX()) {
			ballPusher.extend();
		} else {
			ballPusher.retract();
		}
	}
	
	/**
	 * Sets the state of the launcher flywheels to the 
	 * specified state.
	 * 
	 * @param newState the new state of the flywheels
	 */
	public void setFlywheelState(Flywheel.FlywheelState newState) {
		leftFly.setState(newState);
		rightFly.setState(newState);
	}
	
	/**
	 * Extends the pusher to fire the ball.
	 */
	public void extendPusher() {
		ballPusher.extend();
	}
	
	/**
	 * Retracts the pusher.
	 */
	public void retractPusher() {
		ballPusher.retract();
	}
}
