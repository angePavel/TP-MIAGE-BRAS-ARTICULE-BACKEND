package motors;

import etats.EnumEtatPince;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;

/**
 * Classe MoteurPince héritée de EV3MediumRegulatedMotor. Elle est utilisée pour
 * le moteur de la pince du robot.
 * 
 * @author yanaugereau
 * @version 2.1
 */
public class MoteurPince {

    public EnumEtatPince            etat;
    public int                      position;
    private EV3MediumRegulatedMotor motor;

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
        int angle = 100;
        if ( this.etat == EnumEtatPince.ouverte ) {
            this.motor.rotate( angle );
            this.etat = EnumEtatPince.fermee;
            this.motor.stop();
        }
    }

    /**
     * Cette méthode permet d'ouvrir la pince du robot
     */
    public void ouvrir() {
        int angle = 100;
        if ( this.etat == EnumEtatPince.fermee ) {
            this.motor.rotate( -angle );
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

    private EV3MediumRegulatedMotor getMotor( Port port ) {

        return new EV3MediumRegulatedMotor( port );
    }
}
