package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDirection;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDoorStatus;

/**
 * Tests the AutoModeEveryFloor implementation of the Automode algorithm implementation.
 * 
 * @author Stefan Wohlrab (s1910567010)
 * @since 2021-01-17 22:00
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
class AutoModeEveryFloorTests {

	@Mock
	IBuildingModel MockedBuilding;
	@Mock
	IFloorModel Mockedfloor;

	@Mock
	IElevatorModel MockedElevator0;

	@Mock
	StatusAlert MockedStatusAlert;

	@Mock
	UpdateData MockedUpdateData;
	
	private List<IElevatorModel> MockedElevators;

	private AutoModeEveryFloor automode;
	
	@BeforeEach
	public void setup() {
		MockedElevators = new ArrayList<IElevatorModel>();
		MockedElevators.add(MockedElevator0);
		automode = new AutoModeEveryFloor();
		automode.init(MockedBuilding, MockedElevators, MockedUpdateData);
	}
	
	@Test
	void testdoGetNext_UncommitedElevator_OpenDoors_BottomFloor_GoesUp() {
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		Mockito.when(MockedElevator0.getDirection()).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED);
		Mockito.when(MockedElevator0.getPosition()).thenReturn(0);
		Mockito.when(MockedElevator0.getTarget()).thenReturn(0);
		Mockito.when(MockedElevator0.getDoors()).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_OPEN);
		automode.enable(0);
		
		automode.updateAllElevatorTargets();

		Mockito.verify(MockedUpdateData).setTarget(1, 0);
	}
	
	@Test
	void testdoGetNext_UncommitedElevator_OpenDoors_MiddleFloor_GoesUp() {
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		Mockito.when(MockedElevator0.getDirection()).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED);
		Mockito.when(MockedElevator0.getPosition()).thenReturn(3);
		Mockito.when(MockedElevator0.getTarget()).thenReturn(3);
		Mockito.when(MockedElevator0.getDoors()).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_OPEN);
		automode.enable(0);
		
		automode.updateAllElevatorTargets();

		Mockito.verify(MockedUpdateData).setTarget(4, 0);
	}
	
	@Test
	void testdoGetNext_UncommitedElevator_OpenDoors_TopFloor_GoesDown() {
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		Mockito.when(MockedElevator0.getDirection()).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED);
		Mockito.when(MockedElevator0.getPosition()).thenReturn(10);
		Mockito.when(MockedElevator0.getTarget()).thenReturn(10);
		Mockito.when(MockedElevator0.getDoors()).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_OPEN);
		automode.enable(0);

		automode.updateAllElevatorTargets();

		Mockito.verify(MockedUpdateData).setTarget(9, 0);
	}
	
	
	@Test
	void testdoGetNext_UpElevator_OpenDoors_BottomFloor_GoesUp() {
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		Mockito.when(MockedElevator0.getDirection()).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_UP);
		Mockito.when(MockedElevator0.getPosition()).thenReturn(0);
		Mockito.when(MockedElevator0.getTarget()).thenReturn(0);
		Mockito.when(MockedElevator0.getDoors()).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_OPEN);
		automode.enable(0);
		
		automode.updateAllElevatorTargets();

		Mockito.verify(MockedUpdateData).setTarget(1, 0);
	}
	
	@Test
	void testdoGetNext_UpElevator_OpenDoors_MiddleFloor_GoesUp() {
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		Mockito.when(MockedElevator0.getDirection()).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_UP);
		Mockito.when(MockedElevator0.getPosition()).thenReturn(3);
		Mockito.when(MockedElevator0.getTarget()).thenReturn(3);
		Mockito.when(MockedElevator0.getDoors()).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_OPEN);
		automode.enable(0);
		
		automode.updateAllElevatorTargets();

		Mockito.verify(MockedUpdateData).setTarget(4, 0);
	}
	
	@Test
	void testdoGetNext_UpElevator_OpenDoors_TopFloor_GoesDown_ChangeDirection() {
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		Mockito.when(MockedElevator0.getDirection()).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_UP);
		Mockito.when(MockedElevator0.getPosition()).thenReturn(10);
		Mockito.when(MockedElevator0.getTarget()).thenReturn(10);
		Mockito.when(MockedElevator0.getDoors()).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_OPEN);
		automode.enable(0);

		automode.updateAllElevatorTargets();

		Mockito.verify(MockedUpdateData).setTarget(9, 0);
	}

	
	@Test
	void testdoGetNext_DownElevator_OpenDoors_BottomFloor_ChangeDirection() {
		Mockito.when(MockedElevator0.getDirection()).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_DOWN);
		Mockito.when(MockedElevator0.getPosition()).thenReturn(0);
		Mockito.when(MockedElevator0.getTarget()).thenReturn(0);
		Mockito.when(MockedElevator0.getDoors()).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_OPEN);
		automode.enable(0);
		
		automode.updateAllElevatorTargets();

		Mockito.verify(MockedUpdateData).setTarget(1, 0);
	}
	
	@Test
	void testdoGetNext_DownElevator_OpenDoors_MiddleFloor_GoesDown() {
		Mockito.when(MockedElevator0.getDirection()).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_DOWN);
		Mockito.when(MockedElevator0.getPosition()).thenReturn(3);
		Mockito.when(MockedElevator0.getTarget()).thenReturn(3);
		Mockito.when(MockedElevator0.getDoors()).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_OPEN);
		automode.enable(0);
		
		automode.updateAllElevatorTargets();

		Mockito.verify(MockedUpdateData).setTarget(2, 0);
	}
	
	@Test
	void testdoGetNext_DownElevator_OpenDoors_TopFloor_GoesDown() {
		Mockito.when(MockedElevator0.getDirection()).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_DOWN);
		Mockito.when(MockedElevator0.getPosition()).thenReturn(10);
		Mockito.when(MockedElevator0.getTarget()).thenReturn(10);
		Mockito.when(MockedElevator0.getDoors()).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_OPEN);
		automode.enable(0);

		automode.updateAllElevatorTargets();

		Mockito.verify(MockedUpdateData).setTarget(9, 0);
	}
	
	
	
	@Test
	void testdoGetNext_KeepTarget_NOpenDoors() {
		Mockito.when(MockedElevator0.getTarget()).thenReturn(10);
		Mockito.when(MockedElevator0.getDoors()).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_CLOSING);
		automode.enable(0);

		automode.updateAllElevatorTargets();

		Mockito.verify(MockedUpdateData).setTarget(10, 0);
	}
	
	
	@Test
	void testdoGetNext_KeepTarget_PosNotTarget() {
		Mockito.when(MockedElevator0.getPosition()).thenReturn(10);
		Mockito.when(MockedElevator0.getTarget()).thenReturn(11);
		Mockito.when(MockedElevator0.getDoors()).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_OPEN);
		automode.enable(0);

		automode.updateAllElevatorTargets();

		Mockito.verify(MockedUpdateData).setTarget(11, 0);
	}
	
}
