#Issues Encountered

##Exercise 2
###Part 1

###Part 2

###Part 3
1. Can't use SensorPortListener for Ultrasonic sensors. They don't ever callback or even return a value initially
   * Fixed by creating custom listener with Thread and polling loop to notify the thread
2. It is difficult to make the robot reliably stay a fixed distance from the wall due to noise and not working closer than 7/8 cm
   * One fix could be to use Proportional or PID control
   * Another being using the Infrared sensor for a higher accuracy with less noise

