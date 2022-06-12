package motors;

import captors.CapteurContact;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;

/**
 * Classe Moteur héritée de EV3LargeRegulatedMotor. Elle est utile pour le
 * moteur de rotation et le moteur du bras
 * 
 * @author M1 MIAGE Alternance : Ange-pavel ISHIMWE, MEUNIER Matthias, Florette
 *         DIEU, Gaëtan PELLERIN , Liam RIGBY
 * @version 2.2
 */
public class MoteurBras {

    public int                    positionActuelle;
    public int                    angleMax;
    public EV3LargeRegulatedMotor motor;
    private static int            SPEED = 200;

    /**
     * Constructeur pour le moteur (Rotation ou Bras)
     * 
     * @param port
     *            Port sur lequel le moteur est connecté à la brique EV3.
     */
    public MoteurBras( Port port, int angleMax ) {
        this.motor = new EV3LargeRegulatedMotor( port );
        this.positionActuelle = 0;
        this.angleMax = angleMax;
        this.motor.setSpeed( 100 );
    }

    /**
     * Cette méthode permet de retourner la position actuelle du moteur
     * 
     * @return La position initiale
     */
    public int getPositionActuelle() {
        return positionActuelle;
    }

    /**
     * Cette méthode permet de modifier la valeur de la position actuelle
     * 
     * @param position
     *            La nouvelle valeur de la position actuelle
     */
    public void setPositionActuelle( int position ) {
        this.positionActuelle = position;
    }

    /**
     * Cette méthode permet de retourner l'angle maximum que le moteur peut
     * effectuer
     * 
     * @return L'angle maximum
     */
    public int getAngleMax() {
        return angleMax;
    }

    /**
     * Cette méthode permet de modifier la valeur de l'angle maximum
     * 
     * @param angleMax
     *            La nouvelle valeur de l'ange maximum
     */
    public void setAngleMax( int angleMax ) {
        this.angleMax = angleMax;
    }

    /**
     * Cette méthode permet d'initialiser le moteur de rotation. Il est
     * initialiser quand son capteur (capteur de rotation) est enfoncé.
     * 
     * @param capteur
     *            Le capteur de rotation
     */
    public void initialisationRotation( CapteurContact capteur ) {
        try {
            this.motor.setSpeed( SPEED );
            boolean enfonce = false;
            if ( capteur.contact() == false ) {
                while ( enfonce == false ) {
                    this.motor.forward();
                    if ( capteur.contact() == true ) {
                        this.motor.stop();
                        enfonce = true;
                        // System.out.println("Le moteur de rotation est
                        // initialisé");
                        this.positionActuelle = 0;
                    }
                }
            }
        } catch ( NullPointerException e ) {
            System.out.println( "NullPointerException => Le moteur de rotation n'est pas initialisé" );
        }
    }

    /**
     * Cette méthode permet d'initialiser le moteur du bras. Il est initialiser
     * quand son capteur (capteur de bras) est enfoncé.
     * 
     * @param capteur
     *            Le capteur du bras
     */
    public void initialisationBras( CapteurContact capteur ) {
        try {
            boolean enfonce = false;
            if ( capteur.contact() == false ) {
                while ( enfonce == false ) {
                    this.motor.backward();
                    if ( capteur.contact() == true ) {
                        this.motor.stop();
                        enfonce = true;
                        // System.out.println("Le moteur du bras est
                        // initialisé");
                        this.positionActuelle = 0;
                    }
                }
            }
        } catch ( NullPointerException e ) {
            System.out.println( "NullPointerException => Le moteur du bras n'est pas initialisé" );
        }
    }

    /**
     * Cette méthode permet de faire pivoter le robot. Un angle > 0 permet de
     * pivoter dans le sens inverss des aiguilles d'une montre. Un angle < 0
     * permet de pivoter dans le sens des aiguilles d'une montre
     * 
     * @param angle
     *            Angle de rotation (en degré)
     */
    public void pivoter( int angle ) {
        if ( angle >= 0 ) {
            if ( angle + this.positionActuelle <= this.angleMax ) {
                this.motor.rotate( -angle );
                this.positionActuelle = this.positionActuelle + angle;
                // System.out.println(this.positionActuelle);
            } else {
                this.motor.rotate( -( this.angleMax - this.positionActuelle ) );
                // System.out.println("Angle trop grand");
                this.positionActuelle = this.positionActuelle + this.angleMax - this.positionActuelle;
                // System.out.println(this.positionActuelle);
            }
        } else {
            if ( this.positionActuelle + angle >= 0 ) {
                this.motor.rotate( -angle );
                this.positionActuelle = this.positionActuelle + angle;
                // System.out.println(this.positionActuelle);
            } else {
                this.motor.rotate( this.positionActuelle );
                this.positionActuelle = this.positionActuelle - this.positionActuelle;
                // System.out.println("Angle trop grand 2");
                // System.out.println(this.positionActuelle);
            }
        }

    }

    /**
     * Cette méthode permet d'abaisser le bras du robot en fonction d'un angle
     * donné
     * 
     * @param angle
     *            Angle d'abaissement (en degré)
     */
    public void baisserBras( int angle ) {
        if ( angle > this.angleMax - this.positionActuelle ) {
            this.motor.rotate( this.angleMax - this.positionActuelle );
            this.positionActuelle = this.angleMax;
        } else {
            this.motor.rotate( angle );
            this.positionActuelle = this.positionActuelle + angle;
        }
    }

    /**
     * Cette méthode permet de lever le bras du robot en fonction d'un angle
     * donné.
     * 
     * @param capteur
     *            Le capteur associé au moteur du bras
     * @param angle
     *            Angle de levage (en degré)
     */
    public void leverBras( int angle ) {
        if ( angle > this.positionActuelle ) {
            this.motor.rotate( this.positionActuelle );
            this.positionActuelle = 0;
        } else {
            this.motor.rotate( -angle );
            this.positionActuelle = this.positionActuelle - angle;
        }
    }
}
