package at.fhhagenberg.sqe.esd.ws20.integration;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import java.rmi.RemoteException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.matcher.control.TextMatchers;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevator;
import at.fhhagenberg.sqe.esd.ws20.view.ElevatorControlCenter;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;



@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class EndToEndTest {
	
	private final static int waitUpdateInterval = 2000;
	
	
	
	@Mock
	private IElevator mockedElevator;
	
	/**
	 * Initialize and open gui.
	 * Gets executed before each test, works like BeforeEach.
	 * 
	 * @param stage
	 * @throws Exception
	 */
	@Start
	public void start(Stage stage) throws Exception {
		ElevatorControlCenter ecs = new ElevatorControlCenter();
		ecs.setup(stage, mockedElevator);
	}
	
	@BeforeEach
	void setUp() {
	}

  
	@Test
	public void testExample(FxRobot robot) throws RemoteException {
		Mockito.when(mockedElevator.getElevatorSpeed(0)).thenReturn(4711);
		Mockito.when(mockedElevator.getElevatorSpeed(1)).thenReturn(4712);
		Mockito.when(mockedElevator.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevator.getFloorNum()).thenReturn(5);
		
		try { Thread.sleep(waitUpdateInterval); } catch (InterruptedException e) { e.printStackTrace(); }	//make sure the ui thread has enough time to update the ui
		
		robot.clickOn("#listview_elevators");
		robot.type(KeyCode.DOWN);
		
		try { Thread.sleep(waitUpdateInterval); } catch (InterruptedException e) { e.printStackTrace(); }	//make sure the ui thread has enough time to update the ui
		
		FxAssert.verifyThat("#label_speed_text", LabeledMatchers.hasText("4711"));
	}
	

}
