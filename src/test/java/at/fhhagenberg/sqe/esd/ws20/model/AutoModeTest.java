package at.fhhagenberg.sqe.esd.ws20.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AutoModeTest extends AutoMode {




		@Override
		public int doGetNext() {
			return 0;
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
    	assertEquals(expectedFloors, this.autoModeEnabledFloors);
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
    	assertEquals(expectedFloors, this.autoModeEnabledFloors);
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
    
    
    
	
}
