#Planning

##Exercise 3
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
(This idea has been disregarded since there are too many issues to overcome)
  * One issue with this solution for the problem is that the robot will struggle with sharp corners or curves because in such cases, both of the sensors will likely not be able to stay on the line at the same time.
  * The second issue that was realised for this solution is that the sensor will not be able to detect crossroads in the lines.
2. The second idea was to have two light sensors at the front of the robot that will be approximately one line's width from either side of the line that the robot has to track. The sensors will be able to follow the line by keeping their light values higher than they would be on the line. If the robot started to cross the line that it is following then the sensor readings would pick up less light and depending on which sensor is detecting darker, would be able to adjust to continue following the line.
  * One issue with this solution is that the robot might spin on the spot if it reaches a crossroad in the lines or have issues when both light sensors are above the black line.
    * A solution to this problem is to add behaviour for when both sensors are reading less light so that the robot turns to follow a different line or even continue on the same one.
3. Another solution which may or may not be harder to implement is to use the NXTCam in line tracking mode on the robot. We should be able to set up the camera to follow the black lines since the camera should output the coordinates relative to what it sees for the lines. The camera would be mounted on the front of the robot, 2-3" off the ground. The NXTCam would be likely be facing directly at the floor in order to not have problems with coordinates of lines further away, this will depend on the viewing angle of the camera since we still need to see upcoming crossroads. We would use LEDs to light up the area under the camera so that we do not have problems with shadows or other light sources interfering with our configurations.
  * Configuring our program to adjust the robot's speed dependent on the coordinates that are output from the camera could be rather complicated. The process will certainly be more complicated if we have to angle the camera to face forward.
4. It might be necessary to add a light sensor as well as the NXTCamera is we have issues with the robot following lines and behaving incorrectly with crossroads and curves. (For now we are ignoring this solution until we might need it)
  * This solution is more complicated than the others since it uses two different types of sensors.


###Part 2
####Meeting a Junction
Our solution for handling being able to detect junctions and change to another line involves using both light sensors to detect a single line at the same time. When both light sensors are detecting darkness, this mean that they have crossed on to a junction (in our current environment).

When the robot has detected a junction it can log it's current position and heading. This will allow us to follow a route consisting of coordinates in order to complete a set path. In this current assignment we just just have to be able to follow a set path however in future assignments we will have to use a search algorithm to find a path, this path can then be passed on to the robot in the same way.

Since we have moved the light sensors closer together in order to combat the robot moving at a larger angle to lines we do not need to worry about the sensors passing junction lines at different times.


###Part 3
Part 3 of this exercise is a little more complicated than the other parts since it requires using new software that we are not familiar with and configuring a camera in order for it to output object coordinates. There are many difficulties to overcome when using the camera since the image quality is not very good and so the camera often picks up unwanted objects or loses sight of the object we want. The camera does not have a wide viewing angle.

One issue that we anticipate is that there is far too much interference from the camera. If there is too much interference from the camera output then we may be able to use a PID in order to follow the rectangles that are present most often.

1. Our first solution for part 3 was to put the NXTCam on a motor so that we can track a brightly coloured ball independently of the robot's location. Moving the camera on a motor would allow us to always have the ball in the site of the camera, even while the robot is adjusting its rotation/movement.
  * While this would be a very nice solution to part 3, we all agreed that it is a little more complicated than the solution needs to be. This might be something that we come back to if we have extra time. A much simpler solution is to just have the robot rotate to keep the ball in view.
2. Our second solution for part 3 was to have the camera on the front of the robot in order to follow a red ball that will move in front of the robot. When the ball moves away from the center of the camera image, the robot will turn to correct this.
Should the ball move out of view of the robot's camera, we will turn the robot until it finds the ball again.
  * One issue is that the ball could move out of range of the camera, to combat this we may have to make the robot travel in a direction again before rotating to search for the ball again. We could even move the robot towards where it last saw the ball before starting its rotation.

We decided that we can use our previous code from part 1A of the exercise, this adjusts the speed of the robot dependent on the distance from an object. We would use the code from part 1A in order to do the same but base the speed at which the robot travels on the rectangle size output from the camera. If the output rectangle size is small then the ball is far away and we can speed the robot up and vice versa.
We can adjust the robot behaviour to stop when it reaches a certain distance from the ball.
