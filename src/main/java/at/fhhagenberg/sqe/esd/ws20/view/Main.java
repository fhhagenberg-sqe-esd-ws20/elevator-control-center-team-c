package at.fhhagenberg.sqe.esd.ws20.view;

/**
 * There seems to be a bug where a program that is derived from Application can't be run directly, see https://stackoverflow.com/a/52654791.
 * This becomes a problem when we package this program into a jar and try to run it standalone, without maven.
 * Therefore we have this wrapper here that simple starts the main in the real application class.
 * 
 * @author Lukas Ebenstein (s1910567015)
 * @since 2021-01-16 14:14
 */
public class Main {
	public static void main(String[] args) {
		ElevatorControlCenter.main(args);
	}
}
