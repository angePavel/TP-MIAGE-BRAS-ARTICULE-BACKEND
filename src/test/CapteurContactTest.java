package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import captors.CapteurContact;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;

/**
 * @author M1 MIAGE Alternance : Ange-pavel ISHIMWE, MEUNIER Matthias, Florette
 *         DIEU, Gaëtan PELLERIN , Liam RIGBY
 * @version 2.2
 */
public class CapteurContactTest {
    @Mock
    private CapteurContact capteurContact;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
    }

    @Test
    public void enContactMethodTest() {

        EV3TouchSensor touchSensor = Mockito.mock( EV3TouchSensor.class );
        Mockito.when( capteurContact.getSensor( Mockito.any( Port.class ) ) ).thenReturn( touchSensor );
        Mockito.when( capteurContact.enContact() ).thenReturn( (float) 1 );
        Mockito.when( capteurContact.contact() ).thenCallRealMethod();
        assertTrue( capteurContact.contact() );

        Mockito.when( capteurContact.enContact() ).thenReturn( (float) 0 );
        assertFalse( capteurContact.contact() );
    }

}
