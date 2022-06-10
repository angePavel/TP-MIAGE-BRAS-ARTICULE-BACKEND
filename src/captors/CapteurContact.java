package captors;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;

public class CapteurContact {

    EV3TouchSensor sensor;

    /**
     * Constructeur pour le capteur de contact
     * 
     * @param port
     *            Port sur lequel le le capteur est connecté à la brique EV3
     */
    public CapteurContact( Port port ) {
        sensor = this.getSensor( port );
        // TODO Auto-generated constructor stub
    }

    /**
     * Cette méthode permer de retourner vrai si un capteur est enfoncé. Elle
     * retourne faux s'il n'est pas enfoncé.
     */
    public boolean contact() {

        return this.enContact() == 1;
    }

    public float enContact() {

        float[] tab = { 0, 1 };
        this.sensor.fetchSample( tab, 0 );
        return tab[0];

    }

    public EV3TouchSensor getSensor( Port port ) {
        return new EV3TouchSensor( port );
    }
}
