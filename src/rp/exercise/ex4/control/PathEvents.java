package rp.exercise.ex4.control;

import lejos.robotics.navigation.Pose;

public interface PathEvents {
	void pathInterrupted(Pose location);
	void pathComplete();
}
