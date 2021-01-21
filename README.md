# Elevator Control Center: Team C

[![Sonarcloud Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=fhhagenberg-sqe-esd-ws20_elevator-control-center-team-c&metric=alert_status)](https://sonarcloud.io/dashboard?id=fhhagenberg-sqe-esd-ws20_elevator-control-center-team-c)
![Java CI with Maven](https://github.com/fhhagenberg-sqe-esd-ws20/elevator-control-center-team-c/workflows/Java%20CI%20with%20Maven/badge.svg?branch=master)

## Team
- Florian Atzenhofer (s1910567001)  
- Lukas Ebenstein (s1910567015)  
- Stefan Wohlrab (s1910567010)  


## GUI overview
The following picture should give an overview of the different parts of the GUI. The colors mark the different sections of the GUI.  
Click [here](https://github.com/fhhagenberg-sqe-esd-ws20/elevator-control-center-team-c/raw/Documentation/doc/includes/GUI-explanation.png) to see the image in full resolution.  

1. Red: On the left side there is a list showing all elevators in the building. After clicking on one of the elevators in the list the middle section, market in cyan, shows all information about this elevator. At the bottom there is also the list of floors in the building.  
2. Cyan: All information about the elevator. On the left side in this section the manual mode can be enabled for this elevator. The right side of this section shows the stop buttons pressed inside the elevator and which floors the elevator does not service.  
3. Yellow: This section applies to all elevators in the building. Calls up and down are valid for all elevators.  

At the bottom of the window the latest status/error message is shown.

![Different parts of the GUI market with colors for explaination of functionality.](doc/includes/GUI-explanation.png)


## How to download and run
Download the elevator simulator from [latest release](https://github.com/winterer/elevator/releases/latest).
### Standalone
Make sure you have a working installation of Java SE/JDK version >=11 (major version >=55).  

Download standalone jar from included assets from [latest release](https://github.com/fhhagenberg-sqe-esd-ws20/elevator-control-center-team-c/releases/latest).

### Build your own
Download the code and open the project in eclipse (or adapt the project to your IDE). Make sure Maven 3 is installed and working correctly.
#### Relevant Maven commands
- Run: ```clean javafx:run```  
- Generate code coverage: ```verify```  
- Build standalone jar: ```clean compile assembly:single```  

## Test concept
We used multiple test files that cover different areas of our program. Most of the tests use the TestFx test framework with Mockito.  
All tests can be categorized in the following categories.  
- Unit tests: Each class gets tested as its own unit. All object the unit requires are replaced by Mock-objects. These tests are implemented as white-box tests.  
- Controller-GUI tests: Only the GUI-controller and the GUI are tested together. All object the controller requires are replaced by Mock-objects. It is tested whether function calls and bindings to the controller are shown correctly in the GUI. This test is implemented as white-box test.  
- End-to-End tests: Every class that relies on the elevator simulator were replaced by mocks, see the class diagram below. It is tested whether an interaction with the GUI results in a correct call to the elevator simulator. This test is implemented as black-box test.  
- Manual tests: Short manual tests were executed to check if the application still works in combination with the elevator simulator. The experience based test technique was used.  

The static analysis results of sonarcloud were used during all tests to improve code quality and resolve bugs.  


### Coverage of classes by testfile
The colors have no special meaning or connection to each other, they are used for better visual differentiation in the image.  
Click [here](https://github.com/fhhagenberg-sqe-esd-ws20/elevator-control-center-team-c/raw/Documentation/doc/includes/ElevatorControl_ClassDiagram_marked.png) to see the image in full resolution.  
Click [here](https://github.com/fhhagenberg-sqe-esd-ws20/elevator-control-center-team-c/raw/Documentation/doc/includes/ElevatorControl_ClassDiagram_full.png) to see the class diagram without markings and in full resolution.  

![Class diagrams. Markings show which test tests which classes.](doc/includes/ElevatorControl_ClassDiagram_marked.png)
