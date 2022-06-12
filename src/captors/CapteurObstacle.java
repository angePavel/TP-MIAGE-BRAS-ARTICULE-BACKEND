package captors;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class CapteurObstacle {

    private EV3UltrasonicSensor sensor;

    /**
     * Constructeur pour le capteur de contact
     * 
     * @param port
     *            Port sur lequel le le capteur est connecté à la brique EV3
     */
    public CapteurObstacle( Port port ) {

        sensor = this.getSensor( port );

    }

    /**
     * Cette méthode permer de retourner vrai si un obstacle est détectée. Elle
     * retourne faux s'il ne l'est pas
     */
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
