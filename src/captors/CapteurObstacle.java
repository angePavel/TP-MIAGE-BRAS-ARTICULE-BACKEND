package captors;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class CapteurObstacle extends EV3UltrasonicSensor {

    private final double DISTANCE_OBSTACLE = 0.20;

    public CapteurObstacle( Port port ) {
        super( port );

    }

    private boolean obstacleDetected() {
        boolean touche = false;
        SampleProvider sampleProvider = this.getDistanceMode();

        float[] sample = new float[sampleProvider.sampleSize()];
        int offsetSample = 0;

        this.fetchSample( sample, offsetSample );
        float distanceObjet = (float) sample[0];

        if ( distanceObjet < DISTANCE_OBSTACLE ) {
            touche = true;
        }
        return touche;

    }
}
