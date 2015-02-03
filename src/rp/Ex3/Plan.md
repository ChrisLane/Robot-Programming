#Planning

##Exercise 2
###Part 1
####Task A - Proportional Feedback
Our first idea for this section of part one was to use an ultrasonic sensor on the front of the NXT robot in order to
detect the distance that the robot is from the wall in front of it.
We are using proportional feedback control in order to adjust the speed at which the robot is traveling relative to how
far it is from the wall.
We will set a distance that we want the robot to stop from the wall and the speed of the robot will decrease as it
gets closer to the wall until it eventually reaches the set distance and stops.

For our proportional feedback control we will be using:
* The process variable (PV) which is the distance that the sensor is from an obstacle.
* The setPoint variable will be the desired distance we want the robot to stop from an obstacle.
* Our manipulated variable (MV) will be the speed of the robot, this is the variable we are changing based on the distance.