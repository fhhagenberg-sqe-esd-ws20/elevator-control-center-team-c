package at.fhhagenberg.sqe.esd.ws20.others;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;

import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Provides helper functions that are needed in multiple tests
 * 
 * @author Lukas Ebenstein (s1910567015)
 * @since 2021-01-12 02:10
 */
public class TestUtils {
	
	private int uiUpdateWaitDelayMs;
	
	public TestUtils(int uiUpdateWaitDelayMs) {
		this.uiUpdateWaitDelayMs = uiUpdateWaitDelayMs;
	}

	
	
	/**
	 * waits until a label has a given text or a timeout occurs.
	 * Tip: Use the last label that should get updated to increase the chance every label was updated before.
	 * 
	 * @param labelCssId			Example: "#label_doors_text"
	 * @param labelText				Example: "Closed"
	 * @param robot
	 * @throws TimeoutException
	 */
	public void waitUntilLabelTextChangedTo(String labelCssId, String labelText, FxRobot robot) throws TimeoutException {
		WaitForAsyncUtils.waitFor(uiUpdateWaitDelayMs, TimeUnit.MILLISECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return ((Label) (robot.lookup(labelCssId).query())).getText().equals(labelText);
			}
		});
	}

	/**
	 * waits until a listview contains a cell with given text or a timeout occurs.
	 * Tip: Use the last cell of a listview that should get updated to increase the chance every cell was set before.
	 * 
	 * @param stviewCssId			Example: "#listview_calls_down"
	 * @param cellText				Example: "Elevator 1"
	 * @param robot
	 * @throws TimeoutException
	 */
	public void waitUntilListviewHasCellText(String stviewCssId, String cellText, FxRobot robot) throws TimeoutException {
		WaitForAsyncUtils.waitFor(uiUpdateWaitDelayMs, TimeUnit.MILLISECONDS, new Callable<Boolean>() {
			@SuppressWarnings("unchecked")
			@Override
			public Boolean call() throws Exception {
				Object[] cells = robot.lookup(stviewCssId).queryListView().lookupAll(".cell").toArray();
				for (Object cell : cells) {
					if (Objects.equals(((Cell<String>) cell).getText(), cellText)) {
						return true;
					}
				}
				return false;
			}
		});
	}
	
	/**
	 * waits until a node is visible or a timeout occurs.
	 * Can be used to wait till a new window finished opening.
	 * 
	 * @param nodeCssId				Example: "#button_send_to_floor"
	 * @param robot
	 * @throws TimeoutException
	 */
	public void waitUntilNodeIsVisible(String nodeCssId, FxRobot robot) throws TimeoutException {
		WaitForAsyncUtils.waitFor(uiUpdateWaitDelayMs, TimeUnit.MILLISECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				Optional<Node> button = robot.lookup(nodeCssId).tryQuery();
				if(button.isPresent()) {
					return robot.lookup(nodeCssId).query().isVisible();
				}
				return false;
			}
		});
	}
	
	
	/**
	 * waits until a node is enabled or a timeout occurs.
	 * Can be used to wait till a button is enabled after it got enabled through other ui element.
	 * 
	 * @param nodeCssId				Example: "#button_send_to_floor"
	 * @param robot
	 * @throws TimeoutException
	 */
	public void waitUntilNodeIsEnabled(String nodeCssId, FxRobot robot) throws TimeoutException {
		WaitForAsyncUtils.waitFor(uiUpdateWaitDelayMs, TimeUnit.MILLISECONDS, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				Optional<Node> button = robot.lookup(nodeCssId).tryQuery();
				if(button.isPresent()) {
					return !robot.lookup(nodeCssId).query().isDisabled();
				}
				return false;
			}
		});
	}
	
	
	
	
	
	/**
	 * Checks the current alert dialog displayed (on the top of the window stack)
	 * has the expected contents.
	 *
	 * From https://stackoverflow.com/a/48654878/8355496
	 * 
	 * @param expectedHeader  Expected header of the dialog
	 * @param expectedContent Expected content of the dialog
	 */
	public void verifyAlertDialogHasHeaderAndContent(final String expectedHeader, final String expectedContent) {
		final Stage actualAlertDialog = getTopModalStage();
		assertNotNull(actualAlertDialog);

		final DialogPane dialogPane = (DialogPane) actualAlertDialog.getScene().getRoot();
		assertEquals(expectedHeader, dialogPane.getHeaderText());
		assertEquals(expectedContent, dialogPane.getContentText());
	}
	/**
	 * Checks the current alert dialog displayed (on the top of the window stack)
	 * has the expected contents.
	 *
	 * Adapted from https://stackoverflow.com/a/48654878/8355496
	 * 
	 * @param expectedHeader  Expected header of the dialog
	 */
	public void verifyAlertDialogHasHeader(final String expectedHeader) {
		final Stage actualAlertDialog = getTopModalStage();
		assertNotNull(actualAlertDialog);

		final DialogPane dialogPane = (DialogPane) actualAlertDialog.getScene().getRoot();
		assertEquals(expectedHeader, dialogPane.getHeaderText());
	}

	/**
	 * Get the top modal window.
	 *
	 * Adapted from https://stackoverflow.com/a/48654878/8355496
	 * 
	 * @return the top modal window
	 */
	private Stage getTopModalStage() {
		// Get a list of windows but ordered from top[0] to bottom[n] ones.
		// It is needed to get the first found modal window.
		final List<Window> allWindows = new ArrayList<>(new FxRobot().robotContext().getWindowFinder().listWindows());
		Collections.reverse(allWindows);

		return (Stage) allWindows.stream().filter(window -> window instanceof Stage).findFirst().orElse(null);
	}
}
