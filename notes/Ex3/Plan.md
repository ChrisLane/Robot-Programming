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

```
MV = 1
while (PV > SP) {
  getPV() // Reading from the sensor
  
  double error = SP - PV // Most likely PV - SP
  
  MV = MV + (0.1 * error)
  pilot.setSpeed(MV)
}
pilot.stop()
```
(This code is derived from the lecture with the adjusting sound feedback from distance sensors and likely will need to be adjusted for movement of the robot.)


###Task B - Following Lines
####Ideas for solutions
1. The first idea that was brought up was to have two light sensors on the robot, one at the front and one at the back. The two sensors would be able to ensure that the robot is always positioned straight on the line because both sensors would be used to adjust the robot so that they are both above the black line.
  * One issue with this solution for the problem is that the robot will struggle with sharp corners or curves because in such cases, both of the sensors will likely not be able to stay on the line at the same time.
  * The second issue that was realised for this solution is that the sensor will not be able to detect crossroads in the lines.
2. The second idea was to have two light sensors at the front of the robot that will be approximately one line's width from either side of the line that the robot has to track. The sensors will be able to follow the line by keeping their light values higher than they would be on the line. If the robot started to cross the line that it is following then the sensor readings would pick up less light and depending on which sensor is detecting darker, would be able to adjust to continue following the line.
  * One issue with this solution is that the robot might spin on the spot if it reaches a crossroad in the lines or have issues when both light sensors are above the black line.
    * A solution to this problem is to add behaviour for when both sensors are reading less light so that the robot turns to follow a different line or even continue on the same one.
