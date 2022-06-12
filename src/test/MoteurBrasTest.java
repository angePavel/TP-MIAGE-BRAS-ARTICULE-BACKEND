package test;

import static org.mockito.Mockito.never;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import captors.CapteurContact;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import motors.MoteurBras;

/**
 * @author M1 MIAGE Alternance : Ange-pavel ISHIMWE, MEUNIER Matthias, Florette
 *         DIEU, Gaëtan PELLERIN , Liam RIGBY
 * @version 2.2
 */
public class MoteurBrasTest {

    @Mock
    private MoteurBras moteurBras;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
    }

    @Test
    public void leverBrasTest()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        EV3LargeRegulatedMotor motor = Mockito.mock( EV3LargeRegulatedMotor.class );
        moteurBras.motor = motor;

        Mockito.doNothing().when( moteurBras.motor ).rotate( Mockito.anyInt() );
        Mockito.doCallRealMethod().when( moteurBras ).leverBras( Mockito.anyInt() );

        moteurBras.leverBras( 1 );

        Mockito.verify( moteurBras.motor ).rotate( Mockito.anyInt() );

    }

    @Test
    public void baisserBras()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        EV3LargeRegulatedMotor motor = Mockito.mock( EV3LargeRegulatedMotor.class );
        moteurBras.motor = motor;

        Mockito.doNothing().when( moteurBras.motor ).rotate( Mockito.anyInt() );
        Mockito.doCallRealMethod().when( moteurBras ).baisserBras( Mockito.anyInt() );

        moteurBras.baisserBras( 1 );

        Mockito.verify( moteurBras.motor ).rotate( Mockito.anyInt() );

    }

    @Test
    public void initialisationBrasTest()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        EV3LargeRegulatedMotor motor = Mockito.mock( EV3LargeRegulatedMotor.class );
        CapteurContact capteurContact = Mockito.mock( CapteurContact.class );
        moteurBras.motor = motor;

        Mockito.doNothing().when( moteurBras.motor ).backward();
        Mockito.doNothing().when( moteurBras.motor ).stop();
        Mockito.when( capteurContact.contact() ).thenReturn( true );
        Mockito.doCallRealMethod().when( moteurBras ).initialisationBras( capteurContact );

        moteurBras.initialisationBras( capteurContact );

        Mockito.verify( moteurBras.motor, never() ).backward();
        Mockito.verify( moteurBras.motor, never() ).stop();

    }

    @Test
    public void initialisationRotationTest()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        EV3LargeRegulatedMotor motor = Mockito.mock( EV3LargeRegulatedMotor.class );
        CapteurContact capteurContact = Mockito.mock( CapteurContact.class );
        moteurBras.motor = motor;

        Mockito.doNothing().when( moteurBras.motor ).forward();
        Mockito.doNothing().when( moteurBras.motor ).stop();
        Mockito.when( capteurContact.contact() ).thenReturn( true );
        Mockito.doCallRealMethod().when( moteurBras ).initialisationRotation( capteurContact );

        moteurBras.initialisationRotation( capteurContact );

        Mockito.verify( moteurBras.motor, never() ).forward();
        Mockito.verify( moteurBras.motor, never() ).stop();
        Mockito.verify( moteurBras.motor ).setSpeed( 200 );

    }

}
