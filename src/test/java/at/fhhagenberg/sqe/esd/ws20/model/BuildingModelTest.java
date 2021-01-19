package at.fhhagenberg.sqe.esd.ws20.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Tests the model of the building
 * 
 * @author Stefan Wohlrab (s1910567010)
 * @since 2021-01-16 00:40
 */
class BuildingModelTest {

    @Test
    void testCreate_and_get() {
    	BuildingModel buildingModel = new BuildingModel();
    	
        assertEquals(0, buildingModel.getNumElevators());
        assertEquals(0, buildingModel.getNumFloors());
    }
    
    @Test
    void testNumElevaors_Set_and_get() {
    	BuildingModel buildingModel = new BuildingModel();
    	
    	buildingModel.setNumElevators(1);
    	
        assertEquals(1, buildingModel.getNumElevators());
    }
    
    @Test
    void testNumFloors_Set_and_get() {
    	BuildingModel buildingModel = new BuildingModel();
    	
    	buildingModel.setNumFloors(2);
    	
        assertEquals(2, buildingModel.getNumFloors());
    }
	
}
