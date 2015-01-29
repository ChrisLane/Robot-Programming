#Issues Encountered

##Exercise 2
###Part 1
1. Incorrect patterns: We had set the second argument for all of our movement methods to true which means that they are
not blocking,
this resulted in all movement commands being run at the same time and not making the patterns that we wanted.
   * By removing or changing the argument to false we were able to get the commands to run in the correct order, one
after the other.
2. Getting the robot to turn and move the correct amount was tricky because the values that we input for the robot's
size did not always work due to various instabilities with the robot's build.
   * Through trial and error we were able to get values for the robot that meant that it moved in the correct amounts.

###Part 2
1. When starting the program, the robot would immediately run its code for reversing away from a wall.

###Part 3
Our original solution for this was very simple. The ultrasonic sensor on the side would turn the robot appropriately if
it was too far
or too close to a wall. Since the sensor was on the left hand side, the robot would follow walls on the left side until
it exited the maze.
If the robot was to hit a wall on its front then the touch sensor would cause the robot to reverse, turn a small amount
and continue forward.

1. Issue with first solution was that the touch sensor on the front was not interrupting the forward movement and so the
 robot would continue driving into the wall.
2. Can't use SensorPortListener for Ultrasonic sensors. They don't ever callback or even return a value initially.
   * Fixed by creating custom listener with Thread and polling loop to notify the thread.
3. It is difficult to make the robot reliably stay a fixed distance from the wall due to noise and not working closer
than 7/8 cm
   * One fix could be to use Proportional or PID control.
   * Another being using the Infrared sensor for a higher accuracy with less noise.
4. Getting the function that adjusts how much the robot turns took a lot of trial and error to get appropriate values.
5. The distance that the robot's side sensor detects increases as it turns since it is no longer parallel to the wall.