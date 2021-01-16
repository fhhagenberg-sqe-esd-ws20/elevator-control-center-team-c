package at.fhhagenberg.sqe.esd.ws20.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
class AutoModeTest extends AutoMode {

	@Override
	protected int doGetNext(int elevator) {
		return elevator;
	}

	@Mock
	IBuildingModel MockedBuilding;
	@Mock
	IFloorModel Mockedfloor;

	@Mock
	IElevatorModel MockedElevator1;
	@Mock
	IElevatorModel MockedElevator2;
	@Mock
	IElevatorModel MockedElevator3;

	@Mock
	StatusAlert MockedStatusAlert;

	@Mock
	UpdateData MockedUpdateData;

	@Mock
	List<IElevatorModel> MockedElevators;

	// elevators.add(MockedElevator1);
	// elevators.add(MockedElevator2);
	// elevators.add(MockedElevator3);

	@BeforeEach
	public void setup() {
		this.init(MockedBuilding, MockedElevators, MockedUpdateData);
	}

	@Test
	void testInitNullExceptions() {
		assertThrows(NullPointerException.class, () -> {
			this.init(null, MockedElevators, MockedUpdateData);
		});
		assertThrows(NullPointerException.class, () -> {
			this.init(MockedBuilding, null, MockedUpdateData);
		});
		assertThrows(NullPointerException.class, () -> {
			this.init(MockedBuilding, MockedElevators, null);
		});
	}

	@Test
	void testEnable() {
		this.enable(1);
		this.enable(3);
		this.enable(5);

		List<Integer> expectedFloors = new ArrayList<Integer>();
		expectedFloors.add(1);
		expectedFloors.add(3);
		expectedFloors.add(5);
		assertEquals(expectedFloors, this.autoModeEnabledElevators);
	}

	@Test
	void testDisable() {
		this.enable(1);
		this.enable(2);
		this.enable(3);
		this.enable(4);
		this.enable(5);

		this.disable(3);
		this.disable(5);

		List<Integer> expectedFloors = new ArrayList<Integer>();
		expectedFloors.add(1);
		expectedFloors.add(2);
		expectedFloors.add(4);
		assertEquals(expectedFloors, this.autoModeEnabledElevators);
	}

	@Test
	void testCheckIfInAutoMode() {
		this.enable(1);
		this.enable(2);
		this.enable(4);

		assertTrue(this.checkIfInAutoMode(1));
		assertTrue(this.checkIfInAutoMode(2));
		assertFalse(this.checkIfInAutoMode(3));
		assertTrue(this.checkIfInAutoMode(4));
		assertFalse(this.checkIfInAutoMode(5));
	}

	@Test
	void testUpdateElevatorTargets() {
		this.enable(1);
		this.enable(2);
		this.enable(4);

		this.updateAllElevatorTargets();

		Mockito.verify(MockedUpdateData).setTarget(1, 1);
		Mockito.verify(MockedUpdateData).setTarget(2, 2);
		Mockito.verify(MockedUpdateData).setTarget(4, 4);
		Mockito.verifyNoMoreInteractions(MockedUpdateData);
	}

	// TODO this.disable tests
	// TODO test all other functions in AutoMode

}
