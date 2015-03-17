package rp.exercise.ex4.control;

import search.Coordinate;
import search.Node;

import lejos.robotics.navigation.Pose;

public interface PathEvents {
	void pathInterrupted(Pose location, Node<Coordinate> obstacleNode);
	void pathComplete();
}
