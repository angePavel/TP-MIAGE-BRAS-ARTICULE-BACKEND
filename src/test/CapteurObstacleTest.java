package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import captors.CapteurObstacle;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class CapteurObstacleTest {
    @Mock
    private CapteurObstacle capteurObstacle;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
    }

    @Test
    public void enContactMethodTest() {

        EV3UltrasonicSensor touchSensor = Mockito.mock( EV3UltrasonicSensor.class );
        Mockito.when( capteurObstacle.getSensor( Mockito.any( Port.class ) ) ).thenReturn( touchSensor );
        Mockito.when( capteurObstacle.isObstacleDetected() ).thenReturn( (float) 1 );
        Mockito.when( capteurObstacle.obstacleDetected() ).thenCallRealMethod();
        assertFalse( capteurObstacle.obstacleDetected() );

        Mockito.when( capteurObstacle.isObstacleDetected() ).thenReturn( (float) 0 );
        assertTrue( capteurObstacle.obstacleDetected() );
    }
}
