package captors;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;

public class CapteurContact extends EV3TouchSensor {

    /**
     * Constructeur pour le capteur de contact
     * 
     * @param port
     *            Port sur lequel le le capteur est connecté à la brique EV3
     */
    public CapteurContact( Port port ) {
        super( port );
        // TODO Auto-generated constructor stub
    }

    /**
     * Cette méthode permer de retourner vrai si un capteur est enfoncé. Elle
     * retourne faux s'il n'est pas enfoncé.
     */
    public boolean contact() {
        boolean res = false;
        float etat = 0;
        float[] tab = { 0, 1 };
        this.fetchSample( tab, 0 );
        etat = tab[0];
        if ( etat == 1 ) {
            res = true;
        }
        return res;
    }

}
