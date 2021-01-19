package at.fhhagenberg.sqe.esd.ws20.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests the model of the floor
 * 
 * @author Stefan Wohlrab (s1910567010)
 * @since 2021-01-16 00:40
 */
class FloorModelTest {

	@Test
	void testCreate_and_get() {
		FloorModel floorModel = new FloorModel();

		assertEquals(0, floorModel.getDownButtonsList().size());
		assertEquals(0, floorModel.getUpButtonsList().size());
	}

	@Test
	void testAddButtonUp() {
		FloorModel floorModel = new FloorModel();

		floorModel.addButtonUp(3);

		assertEquals(1, floorModel.getUpButtonsList().size());
		assertEquals(3, floorModel.getUpButtonsList().get(0));
	}

	@Test
	void testAddButtonDown() {
		FloorModel floorModel = new FloorModel();

		floorModel.addButtonDown(3);

		assertEquals(1, floorModel.getDownButtonsList().size());
		assertEquals(3, floorModel.getDownButtonsList().get(0));
	}

	@Test
	void testClearUpButtonsList() {
		FloorModel floorModel = new FloorModel();
		floorModel.addButtonUp(4);
		assertEquals(1, floorModel.getUpButtonsList().size());

		floorModel.clearUpButtonsList();

		assertEquals(0, floorModel.getUpButtonsList().size());
	}

	@Test
	void testClearDownButtonsList() {
		FloorModel floorModel = new FloorModel();
		floorModel.addButtonDown(4);
		assertEquals(1, floorModel.getDownButtonsList().size());

		floorModel.clearDownButtonsList();

		assertEquals(0, floorModel.getDownButtonsList().size());
	}

	@Test
	void testUpButtonsListSet_Get() {
		FloorModel floorModel = new FloorModel();
		List<Integer> list = new ArrayList<Integer>();

		floorModel.setUpButtonsList(list);

		assertEquals(list, floorModel.getUpButtonsList());
	}

	@Test
	void testDownButtonsListSet_Get() {
		FloorModel floorModel = new FloorModel();
		List<Integer> list = new ArrayList<Integer>();

		floorModel.setDownButtonsList(list);

		assertEquals(list, floorModel.getDownButtonsList());
	}
}
