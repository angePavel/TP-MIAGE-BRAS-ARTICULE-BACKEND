package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import motors.MoteurPince;
import utils.EnumEtatPince;

/**
 * @author M1 MIAGE Alternance : Ange-pavel ISHIMWE, MEUNIER Matthias, Florette
 *         DIEU, Gaëtan PELLERIN , Liam RIGBY
 * @version 2.2
 */
public class MoteurPinceTest {

    @Mock
    private MoteurPince moteurPince;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks( this );

    }

    @Test
    public void fermerTest()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        EV3MediumRegulatedMotor motor = Mockito.mock( EV3MediumRegulatedMotor.class );
        moteurPince.motor = motor;
        moteurPince.etat = EnumEtatPince.ouverte;
        Mockito.doNothing().when( moteurPince.motor ).rotate( 100 );
        Mockito.doCallRealMethod().when( moteurPince ).fermer();
        moteurPince.fermer();

        Mockito.verify( moteurPince.motor ).rotate( 100 );

        assertEquals( moteurPince.etat, EnumEtatPince.fermee );

    }

    @Test
    public void ouvrirTest()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        EV3MediumRegulatedMotor motor = Mockito.mock( EV3MediumRegulatedMotor.class );
        moteurPince.motor = motor;
        moteurPince.etat = EnumEtatPince.fermee;
        Mockito.doNothing().when( moteurPince.motor ).rotate( -100 );
        Mockito.doCallRealMethod().when( moteurPince ).ouvrir();
        moteurPince.ouvrir();

        Mockito.verify( moteurPince.motor ).rotate( -100 );
        Mockito.verify( moteurPince.motor ).stop();

        assertEquals( moteurPince.etat, EnumEtatPince.ouverte );

    }

}
