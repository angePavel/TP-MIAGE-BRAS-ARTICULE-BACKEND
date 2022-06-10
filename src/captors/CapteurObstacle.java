package captors;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class CapteurObstacle {

    private EV3UltrasonicSensor sensor;

    public CapteurObstacle( Port port ) {

        sensor = this.getSensor( port );

    }

    public boolean obstacleDetected() {

        double DISTANCE_OBSTACLE = 0.20;
        return this.isObstacleDetected() < DISTANCE_OBSTACLE;

    }

    public float isObstacleDetected() {
        SampleProvider sampleProvider = this.sensor.getDistanceMode();

        float[] sample = new float[sampleProvider.sampleSize()];
        int offsetSample = 0;

        this.sensor.fetchSample( sample, offsetSample );
        return (float) sample[0];
    }

    public EV3UltrasonicSensor getSensor( Port port ) {
        return new EV3UltrasonicSensor( port );
    }
}
