package at.fhhagenberg.sqe;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import at.fhhagenberg.sqe.esd.ws20.model.SystemInfo;

public class SystemInfoTest {
	
	@Disabled
    @Test
    public void testJavaVersion() {
        assertEquals("13", SystemInfo.javaVersion());
    }

    @Test
    public void testJavafxVersion() {
        assertEquals("13", SystemInfo.javafxVersion());
    }
}