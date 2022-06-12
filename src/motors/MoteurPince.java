package motors;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import utils.EnumEtatPince;

/**
 * @author M1 MIAGE Alternance : Ange-pavel ISHIMWE, MEUNIER Matthias, Florette
 *         DIEU, Gaëtan PELLERIN , Liam RIGBY
 * @version 2.2
 */
public class MoteurPince {

    public EnumEtatPince           etat;
    public int                     position;
    public EV3MediumRegulatedMotor motor;
    private static final int       ANGLE = 100;

    /**
     * Constructeur pour le moteur de la pince
     * 
     * @param port
     *            Port sur lequel le moteur est connecté à la brique EV3.
     */
    public MoteurPince( Port port ) {
        this.motor = this.getMotor( port );
        this.etat = EnumEtatPince.ouverte;
        this.position = 0;

    }

    /**
     * Cette méthode permet de fermer la pince du robot
     */
    public void fermer() {
        if ( this.etat == EnumEtatPince.ouverte ) {
            this.motor.rotate( ANGLE );
            this.etat = EnumEtatPince.fermee;
            this.motor.stop();
        }
    }

    /**
     * Cette méthode permet d'ouvrir la pince du robot
     */
    public void ouvrir() {

        if ( this.etat == EnumEtatPince.fermee ) {
            this.motor.rotate( -ANGLE );
            this.etat = EnumEtatPince.ouverte;
            this.motor.stop();
        }
    }

    /**
     * Cette méthode permet d'initialiser la pince dans une certaine position.
     * La pince est initialisée quand elle est ouverte.
     */
    public void initialisationPince() {
        if ( this.etat == EnumEtatPince.fermee ) {
            this.ouvrir();

        }
    }

    public EV3MediumRegulatedMotor getMotor( Port port ) {

        return new EV3MediumRegulatedMotor( port );
    }
}
