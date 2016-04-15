(The actual code is contained in Refactored Mettaton/src/org/usfirst/frc/team3627/robot)

This is a robust object-oriented implementation of the code that drives Mettaton 2016. The classes here are designed so that they can be easily reused for years to come; hopefully, this will save anybody reading this a good amount of time.

However, this code does assume knowledge of some advanced object-oriented concepts. Though I certainly recommend coding this way if you have the knowledge, you should only use this as a reference if you do not.

I'm going to assume you have at least a basic working knowledge of how to use objects though; because of this, I do recommend that you reuse at least a few of the classes. The Dampener class is a very general class that can be used in a huge number of applications. In this code, they are used to aid with motor acceleration so that the bot does not jerk around when the driver decides to suddenly apply input. I'm sure you can think of other uses.

The RoboticsController class has source code that might look messy, but using it makes it easier to read controller input that you know is going to be from an Xbox controller.

Both of these classes implement the NeedsUpdating interface. Dampeners need to be updated at constant intervals so that their values actually approach their target values. RoboticsControllers need to constantly read and save their input states so that edge detection works properly, which is used with button toggling. As a result, you would need to reuse this interface as well, which defines the method that contains the code that updates.

The interface makes it easy to store any objects that need updating in a single Vector. You can update everything at once by simply iterating through this Vector and calling the update() method in all periodic robot functions. You can see an example of this in the code below.

This makes updating objects incredibly easy. Similarly, you can cut down on mapping controls to different actions on the bot by implementing the ControllerDrivable interface in your robot component objects, defining the driveByController() method to perform actions based on controller input within your class, and using a Vector as before.

The other classes are mostly specific to Mettaton. The only exception might be the LimitSwitch class, which is a lightweight class that clears up the ambiguous boolean output of digital inputs. Now that I'm thinking about it, you should reuse this class as well. It has very little overhead.

Visual recognition is not included in this code, because visual recognition kinda failed with Mettaton. We didn't have an Axis Camera, which allows configuration of the image feed via the roboRIO web interface. As a result, the camera picked up on the overhead lighting and other objects, which messed with the auto aiming and visual recognition. If you want to attempt it in the future, make sure to use an Axis Camera and reduce its brightness. Putting a light ring around the camera will then cause the target to glow (assuming that the target is made from retro-reflective tape like it was for 2016). With the customized Axis Camera settings, the target should be the only bright object within the image, which should work. Use the sample visual recognition code projects to learn how targets are recognized. Once the targets are recognized, the bot can be rotated toward it by computing the center of the target in pixels, the offset of the target center from the center of the camera, and converting that pixel offset into an angle offset based off of the FOV and pixel density of the camera. A gyro can then be used to gradually reduce this discrepancy.

 Good luck to any future programmers!!! Programming can be hard (and underappreciated*) but just remember that the robot wouldn't be a robot without you-- it would just be a giant hunk of expensive scrap metal.

glhf <3



--Ryan Longood, Programming Captain 2016 

Feel free to contact me for any help you may need. Email me at longoodr@gmail.com or text me at (804) 840-7372.







\* No, really. Multiple times this build season, electrical team messed up and caused issues that took HOURS to fix, and nobody bat an eye. But when the code stopped working due to a single-line bug that could be fixed in 15 seconds, EVERYBODY LOST THEIR DAMN MINDS. It's like the old adage of IT: "Everything works, why am I paying you?"... "Nothing works, why am I paying you?"
