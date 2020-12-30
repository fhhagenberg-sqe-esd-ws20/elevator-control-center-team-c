package at.fhhagenberg.sqe.esd.ws20.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StatusAlert {
	public StringProperty Status = new SimpleStringProperty("");
	
	//TODO when to clear error status? After the next successful rmi access?
	//TODO how to show if the status is current or old? Add a timestamp?
}
