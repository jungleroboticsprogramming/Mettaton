package org.usfirst.frc.team3627.robot;

/**
 * Some components have the ability to stop. This 
 * interface unites those components as a single type.
 * 
 * Though the Stoppable interfaces are not directly used in
 * the code for Mettaton, they allow for coding easy emergency
 * stopping functions if future specs require them. They don't
 * really add any complexity to the code either, unless you aren't
 * familiar with interfaces. I partially included these 
 * interfaces so  that you can get exposure to them in the 
 * case that you aren't familiar with them.
 * 
 * @author Ryan Longood
 * @version March 2016
 */
public interface Stoppable {

	public void stop();
}
