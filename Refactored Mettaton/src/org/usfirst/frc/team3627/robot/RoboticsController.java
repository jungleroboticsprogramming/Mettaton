package org.usfirst.frc.team3627.robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Encapsulates a joystick with methods 
 * to allow for access to Xbox-style input.
 * 
 * @author Ryan Longood
 * @version March 2016
 */
public class RoboticsController implements NeedsUpdating {
	
	//the joystick itself
	private Joystick controller;
	
	//constants mapped to axis values
	private final static int AX_LX = 0;
	private final static int AX_LY = 1;
	private final static int AX_L_TRIG = 2;
	private final static int AX_R_TRIG = 3;
	private final static int AX_RX = 4;
	private final static int AX_RY = 5;
	
	//constants mapped to button values
	private final static int BTN_A = 1;
	private final static int BTN_B = 2;
	private final static int BTN_X = 3;
	private final static int BTN_Y = 4;
	private final static int BTN_LB = 5;
	private final static int BTN_RB = 6;
	private final static int BTN_BACK = 7;
	private final static int BTN_START = 8;
	private final static int BTN_LS = 9;
	private final static int BTN_RS = 10;
	
	//constants mapped to direction indices
	private final static int DIR_N = 0;
	private final static int DIR_NE = 1;
	private final static int DIR_E = 2;
	private final static int DIR_SE = 3;
	private final static int DIR_S = 4;
	private final static int DIR_SW = 5;
	private final static int DIR_W = 6;
	private final static int DIR_NW = 7;
	
	//used for edge detection
	private boolean[] lastButtons = new boolean[BTN_RS + 1];
	private boolean[] lastDirections = new boolean[8];
	
	
	/**
	 * Creates a RoboticsController from the specified joystick port
	 * 
	 * @param port the port number of the joystick to use; must be an Xbox controller
	 * @throws IllegalArgumentException when the joystick is not an Xbox controller
	 */
	public RoboticsController(int port) throws IllegalArgumentException {
		Joystick newController = new Joystick(port);
		
		//only allow Xbox controllers
		if (!newController.getIsXbox()) {
			throw new IllegalArgumentException("the specified joystick must be an Xbox controller");
		} else {
			
			controller = newController;
		}
	}
	
	//returns if the given button is pressed
	
	public boolean getButtonA() {
		return controller.getRawButton(BTN_A);
	}
	
	public boolean getButtonB() {
		return controller.getRawButton(BTN_B);
	}
	
	public boolean getButtonX() {
		return controller.getRawButton(BTN_X);
	}
	
	public boolean getButtonY() {
		return controller.getRawButton(BTN_Y);
	}
	
	public boolean getButtonLB() {
		return controller.getRawButton(BTN_LB);
	}
	
	public boolean getButtonRB() {
		return controller.getRawButton(BTN_RB);
	}
	
	public boolean getButtonBack() {
		return controller.getRawButton(BTN_BACK);
	}
	
	public boolean getButtonStart() {
		return controller.getRawButton(BTN_START);
	}
	
	public boolean getButtonLS() {
		return controller.getRawButton(BTN_LS);
	}
	
	public boolean getButtonRS() {
		return controller.getRawButton(BTN_RS);
	}
	
	
	//returns true if an edge for the given button was detected
	
	public boolean getButtonEdgeA() {
		return (!lastButtons[BTN_A] && getButtonA());
	}

	public boolean getButtonEdgeB() {
		return (!lastButtons[BTN_B] && getButtonB());
	}
	
	public boolean getButtonEdgeX() {
		return (!lastButtons[BTN_X] && getButtonX());
	}
	
	public boolean getButtonEdgeY() {
		return (!lastButtons[BTN_Y] && getButtonY());
	}
	
	public boolean getButtonEdgeLB() {
		return (!lastButtons[BTN_LB] && getButtonLB());
	}
	
	public boolean getButtonEdgeRB() {
		return (!lastButtons[BTN_RB] && getButtonRB());
	}
	
	public boolean getButtonEdgeBack() {
		return (!lastButtons[BTN_BACK] && getButtonBack());
	}
	
	public boolean getButtonEdgeStart() {
		return (!lastButtons[BTN_START] && getButtonStart());
	}
	
	public boolean getButtonEdgeLS() {
		return (!lastButtons[BTN_LS] && getButtonLS());
	}
	
	public boolean getButtonEdgeRS() {
		return (!lastButtons[BTN_RS] && getButtonRS());
	}
	
	public boolean getDPadEdgeNorth() {
		return (!lastDirections[DIR_N] && isDPadNorth());
	}
	
	public boolean getDPadEdgeNortheast() {
		return (!lastDirections[DIR_NE] && isDPadNortheast());
	}
	
	public boolean getDPadEdgeEast() {
		return (!lastDirections[DIR_E] && isDPadEast());
	}
	
	public boolean getDPadEdgeSoutheast() {
		return (!lastDirections[DIR_SE] && isDPadSoutheast());
	}
	
	public boolean getDPadEdgeSouth() {
		return (!lastDirections[DIR_S] && isDPadSouth());
	}
	
	public boolean getDPadEdgeSouthwest() {
		return (!lastDirections[DIR_SW] && isDPadSouthwest());
	}
	
	public boolean getDPadEdgeWest() {
		return (!lastDirections[DIR_W] && isDPadWest());
	}
	
	public boolean getDPadEdgeNorthwest() {
		return (!lastDirections[DIR_NW] && isDPadNorthwest());
	}
	
	//returns the axis value of the given axis
	
	public double getAxisLX() {
		return controller.getRawAxis(AX_LX);
	}
	
	/**
	 * Returns the value of the Y axis of the left stick.
	 * Positive values are up, and negative values are down.
	 * 
	 * @return the value of the Y axis of the left stick
	 */
	public double getAxisLY() {
		return -controller.getRawAxis(AX_LY);
	}
	
	public double getAxisLTrig() {
		return controller.getRawAxis(AX_L_TRIG);
	}
	
	public double getAxisRTrig() {
		return controller.getRawAxis(AX_R_TRIG);
	}
	
	public double getAxisRX() {
		return controller.getRawAxis(AX_RX);
	}
	
	/**
	 * Returns the value of the Y axis of the right stick.
	 * Positive values are up, and negative values are down.
	 * 
	 * @return the value of the Y axis of the right stick
	 */
	public double getAxisRY() {
		return -controller.getRawAxis(AX_RY);
	}
	
	/**
	 * Returns the angle of the D-Pad in degrees. -1 indicates that no button is being selected. 
	 * Otherwise, the angle starts at 0 for vertical and increases clockwise.
	 * 
	 * @return the angle of the D-Pad
	 */
	public int getDPadAngle() {
		return controller.getPOV();
	}
	
	public boolean isDPadNorth() {
		return (getDPadAngle() == 0);
	}
	
	public boolean isDPadNortheast() {
		return (getDPadAngle() == 45);
	}

	public boolean isDPadEast() {
		return (getDPadAngle() == 90);
	}
	
	public boolean isDPadSoutheast() {
		return (getDPadAngle() == 135);
	}
	
	public boolean isDPadSouth() {
		return (getDPadAngle() == 180);
	}
	
	public boolean isDPadSouthwest() {
		return (getDPadAngle() == 225);
	}
	
	public boolean isDPadWest() {
		return (getDPadAngle() == 270);
	}
	
	public boolean isDPadNorthwest() {
		return (getDPadAngle() == 315);
	}
	
	public boolean isDPadPressed() {
		return (getDPadAngle() != -1);
	}
	
	/**
	 * Updates the button states for edge detection. If a button value does not equal 
	 * what it equaled when this method was last called, it is considered an edge and 
	 * returns true. This is useful for when you want an action to happen only when a 
	 * button is first pressed and not continuously while the button is held down.
	 * 
	 * Edges only occur when a button is pressed, not when released.
	 */
	public void update() {
		
		//buttons start at 1, ergo lastButtons[0] is unused
		for (int i = 1; i < lastButtons.length; i++) {
			lastButtons[i] = controller.getRawButton(i);
		}
		
		lastDirections[DIR_N] = isDPadNorth();
		lastDirections[DIR_NE] = isDPadNortheast();
		lastDirections[DIR_E] = isDPadEast();
		lastDirections[DIR_SE] = isDPadSoutheast();
		lastDirections[DIR_S] = isDPadSouth();
		lastDirections[DIR_SW] = isDPadSouthwest();
		lastDirections[DIR_W] = isDPadWest();
		lastDirections[DIR_NW] = isDPadNorthwest();
		
		
	}
	
	/**
	 * ayy lmao
	 * 
	 * @return ayy lmao
	 */
	public boolean getIsXbox() {
		return true;
	}
}
