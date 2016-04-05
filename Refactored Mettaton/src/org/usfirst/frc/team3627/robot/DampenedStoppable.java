package org.usfirst.frc.team3627.robot;

/**
 * Stoppable components that use Dampeners run the risk of breaking
 * the bot if they do not stop immediately, because they will
 * gradually decelerate instead of stopping fully which
 * can cause them to go past their physical limits. This
 * interface provides an immediate stopping function to bypass
 * the gradual deceleration that Dampeners provide.
 * However, because this class inherits from Stoppable,
 * the classes implementing this interface retain their
 * ability to stop gradually as normal.
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
 *
 */
public interface DampenedStoppable extends Stoppable {
	
	//DampenedStoppable inherits stop() from Stoppable
	
	//in addition to stop(), DampenedStoppable allows for 
	//stopping without decelerating via immediateStop()
	public void immediateStop();
}
