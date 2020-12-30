module at.fhhagenberg.sqe.esd.ws20 {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires java.rmi;
	requires java.sql;
	requires transitive javafx.base;

	exports at.fhhagenberg.sqe.esd.ws20.view;
	exports at.fhhagenberg.sqe.esd.ws20.model;
	exports at.fhhagenberg.sqe.esd.ws20.sqeelevator;

	opens at.fhhagenberg.sqe.esd.ws20.view;
	//opens at.fhhagenberg.sqe.esd.ws20.model;
	//opens at.fhhagenberg.sqe.esd.ws20.sqeelevator;
}
