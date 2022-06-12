import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import captors.CapteurContact;
import lejos.hardware.Bluetooth;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.remote.nxt.BTConnection;
import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;
import motors.MoteurBras;
import motors.MoteurPince;

/**
 * Classe Controleur
 * 
 * @author M1 MIAGE Alternance : AUGEREAU Yan, CELLIER ALexandre, MEUNIER
 *         Matthias & PERRET Pierre-Yves
 * @version 2.1
 */
public class Controleur {

    private static BTConnection     BTLink;
    private static DataOutputStream donneeSortie;
    private static DataInputStream  donneeEntree;
    private static int              line   = 0;
    private static DataInputStream  objetA;
    private static DataInputStream  objetB;
    private static DataInputStream  pile;
    private final static Logger     logger = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME );

    /**
     * Cette methode permet de initialiser le Logging
     */

    private static void setUpLogger() {
        LogManager.getLogManager().reset();
        logger.setLevel( Level.ALL );

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel( Level.SEVERE );
        logger.addHandler( consoleHandler );

        try {
            FileHandler fHandler = new FileHandler( "myLogger.log", true );
            fHandler.setLevel( Level.FINE );
            logger.addHandler( fHandler );
        } catch ( java.io.IOException e ) {
            logger.log( Level.SEVERE, "File logger ne marche pas", e );
        }

    }

    /**
     * Cette méthode permet d'initialiser tous les moteurs et capteurs du robot
     * 
     * @param mr
     *            Le moteur de rotation
     * @param mb
     *            Le moteur du bras
     * @param mp
     *            Le moteur de la pince
     * @param cr
     *            Le capteur de rotation
     * @param cb
     *            Le capteur du bras
     */
    public void initialisation( MoteurBras mr, MoteurBras mb, MoteurPince mp, CapteurContact cr, CapteurContact cb ) {
        mb.initialisationBras( cb );
        mr.initialisationRotation( cr );
        mp.initialisationPince();

        // System.out.println("Moteurs initialisés");
    }

    /**
     * Cette méthode permet de savoir s'il y a une distance suffisante entre
     * deux objets
     * 
     * @param A
     *            Objet A
     * @param B
     *            Objet B
     * @return Retourne vrai si la position est suffisante, faux sinon
     */
    public boolean distanceOk( int A, int B ) {
        boolean res = false;
        if ( Math.abs( A - B ) > 120 ) {
            res = true;
        }
        return res;
    }

    /**
     * Cette méthode permet de générer un entier aléatoire compris entre deux
     * bornes
     * 
     * @param borneInf
     *            Borne inférieure
     * @param borneSup
     *            Borne supérieure
     * @return Retourne l'entier aléatoire
     */
    public int genererInt( int borneInf, int borneSup ) {
        Random random = new Random();
        int nb;
        nb = random.nextInt( borneSup - borneInf );
        return nb;
    }

    /**
     * Cette méthode permet d'intervertir deux objets. L'objet A va aller à la
     * place del'objet B et inversement.
     * 
     * @param mr
     *            Moteur de rotation
     * @param mb
     *            Moteur du bras
     * @param mp
     *            Moteur de la pince
     * @param cr
     *            Capteur de rotation
     * @param cb
     *            Cpateur du bars
     * @param positionA
     *            Posiiton de l'objet A
     * @param positionB
     *            Position de l'objet B
     */
    public void inverserObjets( MoteurBras mr, MoteurBras mb, MoteurPince mp, CapteurContact cr, CapteurContact cb,
            int positionA,
            int positionB ) {
        int tmp = genererInt( 0, mr.angleMax );
        if ( positionA >= 0 && positionA <= mr.angleMax && positionB >= 0 && positionB <= mr.angleMax ) {
            if ( distanceOk( positionA, positionB ) ) {
                while ( distanceOk( tmp, positionA ) == false || distanceOk( tmp, positionB ) == false ) {
                    tmp = genererInt( 0, mr.angleMax );
                }

                // System.out.println("TMP : "+tmp);

                mr.pivoter( positionA );
                mb.baisserBras( 270 );
                mp.fermer();
                mb.initialisationBras( cb );
                // L'objet A est saisi

                mr.pivoter( tmp - positionA );
                mb.baisserBras( 270 );
                mp.ouvrir();
                mb.initialisationBras( cb );
                // L'objet A est déposé en tmp

                mr.pivoter( positionB - tmp );
                mb.baisserBras( 270 );
                mp.fermer();
                mb.initialisationBras( cb );
                // L'objet B est saisi

                mr.pivoter( positionA - positionB );
                mb.baisserBras( 270 );
                mp.ouvrir();
                mb.initialisationBras( cb );
                // L'objet B est dépose en A

                mr.pivoter( tmp - positionA );
                mb.baisserBras( 270 );
                mp.fermer();
                mb.initialisationBras( cb );
                // L'objet A est saisi

                mr.pivoter( positionB - tmp );
                mb.baisserBras( 270 );
                mp.ouvrir();
                mb.initialisationBras( cb );
                // L'objet A est dépose en B

                this.initialisation( mr, mb, mp, cr, cb );

            } else {
                System.out.println( "Les deux objets sont trop proches l un de l autre" );
            }
        } else {
            System.out.println( "Les positions sont hors champs pour le moteur de rotation" );
        }

    }

    /**
     * Cette méthode permet d'envoyer une couleur selon la couleur reçu et
     * d'envoyer sur le point d'envoi.
     * 
     * @param mr
     *            Moteur de rotation
     * @param mb
     *            Moteur du bras
     * @param mp
     *            Moteur de la pince
     * @param cr
     *            Capteur de rotation
     * @param cb
     *            Capteur du bras
     * @param positionD
     *            position de départ l'objet à chercher
     * @param positionF
     *            Future position de l'objet
     * @param couleur
     *            information de la couleur à déplacer
     */
    public void ReceptionCouleur( MoteurBras mr, MoteurBras mb, MoteurPince mp, CapteurContact cr, CapteurContact cb,
            int positionD, int positionF, String couleur ) {
        if ( couleur == "vert" ) {
            positionF = 120;
            // Voir explication pour les informations sur les couleurs dans le
            // dossier de Programmation
        } else if ( couleur == "jaune" ) {
            positionF = 240;
            // Voir explication pour les informations sur les couleurs dans le
            // dossier de Programmation
        } else if ( couleur == "rouge" ) {
            positionF = 360;
            // Voir explication pour les informations sur les couleurs dans le
            // dossier de Programmation
        } else if ( couleur == "bleu" ) {
            positionF = 480;
            // Voir explication pour les informations sur les couleurs dans le
            // dossier de Programmation
        } else {
            positionF = 0;
            // Si on ne reçoit pas d'information on envoie le bac d'envoi vers
            // la position Réception
            // Voir explication pour les informations sur les couleurs dans le
            // dossier de Programmation
        }
        positionD = 615;
        // Voir explication pour les informations sur les couleurs dans le
        // dossier de Programmation
        if ( positionD != 615 ) {
            mr.pivoter( positionD );
            mb.baisserBras( 270 );
            mp.fermer();
            mb.initialisationBras( cb );
            // On va chercher l'objet en positionD

            mr.pivoter( positionF );
            mb.baisserBras( 270 );
            mp.ouvrir();
            mb.initialisationBras( cb );
            // On envoie la pièce sur la zone de Réception en positionF
        }

        this.initialisation( mr, mb, mp, cr, cb );
    }

    /**
     * Cette méthode permet de Réceptionner une couleur. C'est à dire qu'on va
     * réceptionner l'objet en position 365 et l'envoyer sur le bloc de couleur
     * correspondant
     * 
     * @param mr
     *            Moteur de rotation
     * @param mb
     *            Moteur du bras
     * @param mp
     *            Moteur de la pince
     * @param cr
     *            Capteur de rotation
     * @param cb
     *            Capteur du bras
     * @param positionD
     *            position de départ l'objet à chercher
     * @param positionF
     *            Future position de l'objet
     * @param couleur
     *            information de la couleur à déplacer
     */
    public void EnvoiCouleur( MoteurBras mr, MoteurBras mb, MoteurPince mp, CapteurContact cr, CapteurContact cb,
            int positionD, int positionF, String couleur ) {
        if ( couleur == "vert" ) {
            positionD = 120;
            // Voir explication pour les informations sur les couleurs dans le
            // dossier de Programmation
        } else if ( couleur == "jaune" ) {
            positionD = 240;
            // Voir explication pour les informations sur les couleurs dans le
            // dossier de Programmation
        } else if ( couleur == "rouge" ) {
            positionD = 360;
            // Voir explication pour les informations sur les couleurs dans le
            // dossier de Programmation
        } else if ( couleur == "bleu" ) {
            positionD = 480;
            // Voir explication pour les informations sur les couleurs dans le
            // dossier de Programmation
        } else {
            positionD = 615;
            // Si on ne reçoie pas d'information, On déplace le bac de la zone
            // réception vers la zone d'envoie
        }
        positionF = 0;
        // Voir explication pour les informations sur les couleurs dans le
        // dossier de Programmation
        if ( positionD != 615 ) {
            mr.pivoter( positionD );
            mb.baisserBras( 270 );
            mp.fermer();
            mb.initialisationBras( cb );
            // On va chercher l'objet en positionD

            mr.pivoter( positionF );
            mb.baisserBras( 270 );
            mp.ouvrir();
            mb.initialisationBras( cb );
            // On envoie la pièce sur la zone d'envoie en positionF
        }

        this.initialisation( mr, mb, mp, cr, cb );
    }

    /**
     * Cette méthode permet de dépiler une pile de deux objets
     * 
     * @param mr
     *            Moteur de rotation
     * @param mb
     *            Moteur du bras
     * @param mp
     *            Moteur de la pince
     * @param cr
     *            Capteur de rotation
     * @param cb
     *            Capteur du bras
     * @param position
     *            Pile Position de la pile d'objets
     * @param positionA
     *            Future position de l'objet se trouvant au sommet de la pile
     * @param positionB
     *            Future position de l'objet se trouvant à la base de la pile
     */
    public void depiler( MoteurBras mr, MoteurBras mb, MoteurPince mp, CapteurContact cr, CapteurContact cb,
            int positionPile,
            int positionA,
            int positionB ) {
        if ( positionA >= 0 && positionA <= mr.angleMax && positionB >= 0 && positionB <= mr.angleMax
                && positionPile >= 0 && positionPile <= mr.angleMax ) {
            if ( distanceOk( positionPile, positionA ) && distanceOk( positionPile, positionB )
                    && distanceOk( positionA, positionB ) ) {

                mr.pivoter( positionPile );
                mb.baisserBras( 215 );
                mp.fermer();
                mb.initialisationBras( cb );
                // L'objet au sommet de la pile est saisi

                mr.pivoter( positionA - positionPile );
                mb.baisserBras( 270 );
                mp.ouvrir();
                mb.initialisationBras( cb );
                // L'objet du sommet de la pile est déposé en position A

                mr.pivoter( positionPile - positionA );
                mb.baisserBras( 270 );
                mp.fermer();
                mb.initialisationBras( cb );
                // L'objet en bas de la pile est saisi

                mr.pivoter( positionB - positionPile );
                mb.baisserBras( 270 );
                mp.ouvrir();
                mb.initialisationBras( cb );

                this.initialisation( mr, mb, mp, cr, cb );

            } else {
                System.out.println( "Positions sont trop proches" );
            }

        } else {
            System.out.println( "Les positions sont hors champs pour le moteur de rotation" );
        }

    }

    public static void bluetoothConnection() {
        System.out.println( "En ecoute" );
        BTConnector nxtCommConnector = (BTConnector) Bluetooth.getNXTCommConnector();
        BTLink = (BTConnection) nxtCommConnector.waitForConnection( 6000, NXTConnection.RAW );
        donneeSortie = BTLink.openDataOutputStream();
        donneeEntree = BTLink.openDataInputStream();
        objetA = BTLink.openDataInputStream();
        objetB = BTLink.openDataInputStream();
        pile = BTLink.openDataInputStream();
    }

    public static void main( String[] args ) throws IOException, InterruptedException, SocketException {
        Controleur controleur = new Controleur();
        CapteurContact capteurRotation = new CapteurContact( SensorPort.S1 );
        CapteurContact capteurBras = new CapteurContact( SensorPort.S3 );
        MoteurBras moteurBras = new MoteurBras( MotorPort.B, 270 );
        MoteurBras moteurRotation = new MoteurBras( MotorPort.C, 615 );
        MoteurPince moteurP = new MoteurPince( MotorPort.A );

        setUpLogger();

        controleur.initialisation( moteurRotation, moteurBras, moteurP, capteurRotation, capteurBras );

        logger.info( "Connexion bluetooth en cours ....." );
        bluetoothConnection();

        boolean connexion = true;
        int p = -1;
        int a = -1;
        int b = -1;
        while ( connexion ) {

            line = donneeEntree.readByte();

            switch ( line ) {
            // Initialisation des moteurs du robot
            case 0:
                logger.info( "Initialisation des moteurs du robot" );
                controleur.initialisation( moteurRotation, moteurBras, moteurP, capteurRotation, capteurBras );
                break;
            // Echanger des objets
            case 1:
                logger.info( "Echange des objets " );
                System.out.println( "ECHANGER" );
                System.out.println( "Reception en cours ..." );
                line = -1;

                while ( line == -1 ) {
                    line = objetA.readByte();
                    a = line;
                    for ( int i = 1; i <= 4; i++ ) {
                        line = objetA.readByte();
                        a = a + line;
                    }
                }
                line = -1;
                while ( line == -1 ) {
                    line = objetB.readByte();
                    b = line;
                    for ( int j = 1; j <= 4; j++ ) {
                        line = objetB.readByte();
                        b = b + line;
                    }
                }

                System.out.println( "Position A : " + a );
                System.out.println( "Position B : " + b );
                controleur.inverserObjets( moteurRotation, moteurBras, moteurP, capteurRotation, capteurBras, a, b );

                break;
            // Dépiler des objets
            case 2:
                logger.info( "Depiler les objets " );
                System.out.println( "DEPILER" );
                System.out.println( "Reception en cours ..." );
                line = -1;
                while ( line == -1 ) {
                    line = pile.readByte();
                    p = line;
                    for ( int i = 1; i <= 4; i++ ) {
                        line = pile.readByte();
                        p = p + line;
                    }
                }
                line = -1;
                while ( line == -1 ) {
                    line = objetA.readByte();
                    a = line;
                    for ( int i = 1; i <= 4; i++ ) {
                        line = objetA.readByte();
                        a = a + line;
                    }
                }
                line = -1;
                while ( line == -1 ) {
                    line = objetB.readByte();
                    b = line;
                    for ( int j = 1; j <= 4; j++ ) {
                        line = objetB.readByte();
                        b = b + line;
                    }
                }

                System.out.println( "Position Pile : " + p );
                System.out.println( "Position A : " + a );
                System.out.println( "Position B : " + b );

                controleur.depiler( moteurRotation, moteurBras, moteurP, capteurRotation, capteurBras, p, a, b );
                break;
            case 3:
                logger.info( "connexion interrompues" );
                objetA.close();
                objetB.close();
                pile.close();
                donneeEntree.close();
                donneeSortie.close();
                BTLink.close();
                connexion = false;
            }

        }
    }
}