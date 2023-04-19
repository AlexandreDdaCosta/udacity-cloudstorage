package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import java.io.File;
import static java.lang.Thread.sleep;
import java.time.Duration;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private static WebDriver driver;
	private static String baseURL;
	private static final String validPassword = "Val1dP@ssword";
	private static final String testFirstName = "Test";
	private static final String testLastName = "User";
	private static final String noteDescription = "Test description";
	private static final String noteDescriptionUpdated = "Test description, updated";
	private static final String noteTitle = "Test title";
	private static final String noteTitleUpdated = "Test title updated";
	private static final String credentialPassword = "bar";
	private static final String credentialPasswordUpdated = "foo";
	private static final String credentialUrl = "https://www.google.com";
	private static final String credentialUrlUpdated = "https://www.apple.com";
	private static final String credentialUsername = "foo";
	private static final String credentialUsernameUpdated = "bar";
	private static JavascriptExecutor js;
	private static WebDriverWait webDriverWait;

	@BeforeAll
	public static void beforeAll() {
		System.out.println("Starting Chrome web driver.");
		// The following line of code resolves chromedriver 111 bug with Selenium; see
		// https://github.com/SeleniumHQ/selenium/issues/11750
		System.setProperty("webdriver.http.factory", "jdk-http-client");
		//System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
		/*
		bonigarcia's ChromeDriver looks for Chrome driver version 111,
		but the local ChromeDriver distribution might be 110 or something else.
		These tests were successfully run on chromedriver 111, with a workaround
		for a current bug involving Selenium (see webdriver.http.factory setting or
		"remote-allow-origins=*" ChromeDriver option).
		*/
		WebDriverManager.chromedriver().setup();
		//WebDriverManager.chromedriver().version("110").setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--no-sandbox"); // Bypass OS security model
		options.addArguments("--disable-dev-shm-usage"); // Overcome limited resource problems
		options.addArguments("--headless"); // Stop Chrome browser from flashing the screen
		// The following line can also resolve the bug with the current
		// chromedriver 111 and Selenium.
		//options.addArguments("--remote-allow-origins=*");
		//options.setBinary("/path/to/other/chrome/binary");
		System.out.println("Instantiating Chrome web driver.");
		driver = new ChromeDriver(options);
		System.out.println("Finished instantiating Chrome web driver.");
		js = (JavascriptExecutor) driver;
		webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	@AfterAll
	public static void afterAll() {
		if (driver != null) {
			driver.quit();
		}
	}

	@BeforeEach
	public void beforeEach() {
		baseURL = "http://localhost:" + this.port;
		System.out.println("Starting test; base URl is " + baseURL);
 	}

	@AfterEach
	public void afterEach() throws InterruptedException {
		System.out.println("Ending test; one second break . . .");
		sleep(1000);
	}

	// Tests

	// 1. Write tests for User Signup, Login, and Unauthorized Access Restrictions.

	// Write a test that verifies that an unauthorized user can only access the
	// login and signup pages.

	@Test
	@Order(1)
	public void checkUnauthorizedUserAccess() {
		driver.get(baseURL + "/login");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signup-link")));
		Assertions.assertEquals("Login", driver.getTitle());
		driver.get(baseURL + "/signup");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		Assertions.assertEquals("Sign Up",driver.getTitle());
		driver.get(baseURL + "/home");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signup-link")));
		Assertions.assertNotEquals("Home",driver.getTitle());
	}

	// Write a test that signs up a new user, logs in, verifies that the home page is
	// accessible, logs out, and verifies that the home page is no longer accessible.

	@Test
	@Order(2)
	public void signupUserAndVerifyLogout() {
		String testUserName = "test2";
		doMockSignUp(testFirstName, testLastName, testUserName, validPassword);
		doLogIn(testUserName, validPassword);
		Assertions.assertEquals("Home", driver.getTitle());
		doLogout();
		driver.get(baseURL + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	// 2. Write Tests for Note Creation, Viewing, Editing, and Deletion

	// Write a test that creates a note and verifies it is displayed.

	@Test
	@Order(3)
	public void createAndVerifyNote() {
		String testUserName = "test3";

		doMockSignUp(testFirstName, testLastName, testUserName, validPassword);
		doLogIn(testUserName, validPassword);
		Assertions.assertEquals("Home", driver.getTitle());
		createNote();
		verifyNote(noteTitle, noteDescription, false, false);
		doLogout();
	}

	// Write a test that edits an existing note and verifies that the changes are displayed.

	@Test
	@Order(4)
	public void editAndVerifyNote() {
		String testUserName = "test4";

		doMockSignUp(testFirstName, testLastName, testUserName, validPassword);
		doLogIn(testUserName, validPassword);
		Assertions.assertEquals("Home", driver.getTitle());
		createNote();
		verifyNote(noteTitle, noteDescription, false, false);
		editNote();
		verifyNote(noteTitleUpdated, noteDescriptionUpdated, false, false);
		verifyNote(noteTitle, noteDescription, true, true);
		doLogout();
	}

	// Write a test that deletes a note and verifies that the note is no longer displayed.

	@Test
	@Order(5)
	public void deleteAndVerifyNote() {
		String testUserName = "test5";

		doMockSignUp(testFirstName, testLastName, testUserName, validPassword);
		doLogIn(testUserName, validPassword);
		Assertions.assertEquals("Home", driver.getTitle());
		createNote();
		verifyNote(noteTitle, noteDescription, false, false);
		deleteNote();
		verifyNote(noteTitle, noteDescription, true, false);
		doLogout();
	}

	// 3. Write tests for Credential Creation, Viewing, Editing, and Deletion

	// Write a test that creates a set of credentials, verifies that they are displayed,
	// and verifies that the displayed password is encrypted.

	@Test
	@Order(6)
	public void createAndVerifyCredential() {
		String testUserName = "test6";

		doMockSignUp(testFirstName, testLastName, testUserName, validPassword);
		doLogIn(testUserName, validPassword);
		Assertions.assertEquals("Home", driver.getTitle());
		createCredential();
		verifyCredential(credentialUrl, credentialUsername, credentialPassword, false, false);
		doLogout();
	}

	// Write a test that views an existing set of credentials, verifies that the viewable
	// password is unencrypted, edits the credentials, and verifies that the changes
	// are displayed.

	@Test
	@Order(7)
	public void editAndVerifyCredential() {
		String testUserName = "test7";

		doMockSignUp(testFirstName, testLastName, testUserName, validPassword);
		doLogIn(testUserName, validPassword);
		Assertions.assertEquals("Home", driver.getTitle());
		createCredential();
		verifyCredential(credentialUrl, credentialUsername, credentialPassword, false, false);
		editCredential();
		verifyCredential(credentialUrlUpdated, credentialUsernameUpdated, credentialPasswordUpdated, false, false);
		verifyCredential(credentialUrl, credentialUsername, credentialPassword, true, true);
		doLogout();
	}

	// Write a test that deletes an existing set of credentials and verifies that the
	// credentials are no longer displayed.

	@Test
	@Order(8)
	public void deleteAndVerifyCredential() {
		String testUserName = "test8";

		doMockSignUp(testFirstName, testLastName, testUserName, validPassword);
		doLogIn(testUserName, validPassword);
		Assertions.assertEquals("Home", driver.getTitle());
		createCredential();
		verifyCredential(credentialUrl, credentialUsername, credentialPassword, false, false);
		deleteCredential();
		verifyCredential(credentialUrl, credentialUsername, credentialPassword, true, false);
		doLogout();
	}

	// Helper methods

	private void createCredential() {
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement navCredentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		js.executeScript("arguments[0].click()", navCredentialsTab);
		WebElement addCredentialButton = driver.findElement(By.id("addCredentialButton"));
		webDriverWait.until(ExpectedConditions.elementToBeClickable(addCredentialButton)).click();
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url"))).sendKeys(credentialUrl);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("credential-username"))).sendKeys(credentialUsername);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("credential-password"))).sendKeys(credentialPassword);
		WebElement saveNoteButton = driver.findElement(By.id("saveCredentialButton"));
		saveNoteButton.click();
	}

	private void createNote() {
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement navNotesTab = driver.findElement(By.id("nav-notes-tab"));
		js.executeScript("arguments[0].click()", navNotesTab);
		WebElement addNoteButton = driver.findElement(By.id("addNoteButton"));
		webDriverWait.until(ExpectedConditions.elementToBeClickable(addNoteButton)).click();
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).sendKeys(noteTitle);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("note-description"))).sendKeys(noteDescription);
		WebElement saveNoteButton = driver.findElement(By.id("saveNoteButton"));
		saveNoteButton.click();
	}

	private void deleteCredential() {
		WebElement credentialsTable = driver.findElement(By.id("credentialsTable"));
		WebElement credentialDeleteLink = null;
		WebElement credentialEditButton = null;
		List<WebElement> credentialTableRows = credentialsTable.findElements(By.className("credentialTableRow"));
		for (WebElement credentialTableRow : credentialTableRows) {
				WebElement credentialTableRowUsername = credentialTableRow.findElement(By.className("credentialUsername"));
				WebElement credentialTableRowPassword = credentialTableRow.findElement(By.className("credentialPassword"));
				WebElement credentialTableUrlSpan = credentialTableRow.findElement(By.className("credentialUrlSpan"));
				if (credentialTableRowUsername.getAttribute("innerHTML").equals(credentialUsername) &&
						! credentialTableRowPassword.getAttribute("innerHTML").equals(credentialPassword) &&
						credentialTableUrlSpan.getAttribute("innerHTML").equals(credentialUrl)
				) {
					credentialDeleteLink = credentialTableRow.findElement(By.className("credentialDeleteLink"));
					credentialEditButton = credentialTableRow.findElement(By.className("credentialEditButton"));
					break;
				}
		}
		Assertions.assertNotNull(credentialDeleteLink);
		Assertions.assertNotNull(credentialEditButton);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(credentialEditButton)).click();
		WebElement editCredentialPassword = driver.findElement(By.id("credential-password"));
		Assertions.assertTrue(editCredentialPassword.getAttribute("value").equals(credentialPassword));
		WebElement editCredentialClose = driver.findElement(By.className("editCredentialClose"));
		webDriverWait.until(ExpectedConditions.elementToBeClickable(editCredentialClose)).click();
		webDriverWait.until(ExpectedConditions.elementToBeClickable(credentialDeleteLink)).click();
	}

	private void deleteNote() {
		WebElement notesTable = driver.findElement(By.id("notesTable"));
		WebElement noteDeleteLink = null;
		List<WebElement> noteTableRows = notesTable.findElements(By.className("noteTableRow"));
		for (WebElement noteTableRow : noteTableRows) {
			WebElement noteTableRowTitle = noteTableRow.findElement(By.className("noteTitle"));
			WebElement noteTableRowDescription = noteTableRow.findElement(By.className("noteDescription"));
			if (noteTableRowTitle.getAttribute("innerHTML").equals(noteTitle) &&
					noteTableRowDescription.getAttribute("innerHTML").equals(noteDescription)) {
				noteDeleteLink = noteTableRow.findElement(By.className("noteDeleteLink"));
				break;
			}
		}
		Assertions.assertNotNull(noteDeleteLink);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(noteDeleteLink)).click();
	}

	private void doLogout() {
		driver.get(baseURL + "/home");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logoutButton")));
		WebElement logoutButton = driver.findElement(By.id("logoutButton"));
		logoutButton.click();
		Assertions.assertEquals("Login", driver.getTitle());
	}


	private void editCredential() {
		WebElement credentialsTable = driver.findElement(By.id("credentialsTable"));
		WebElement credentialEditButton = null;
		List<WebElement> credentialTableRows = credentialsTable.findElements(By.className("credentialTableRow"));
		for (WebElement credentialTableRow : credentialTableRows) {
			WebElement credentialTableRowUsername = credentialTableRow.findElement(By.className("credentialUsername"));
			WebElement credentialTableRowUrlSpan = credentialTableRow.findElement(By.className("credentialUrlSpan"));
			if (credentialTableRowUsername.getAttribute("innerHTML").equals(credentialUsername) &&
					credentialTableRowUrlSpan.getAttribute("innerHTML").equals(credentialUrl)) {
				credentialEditButton = credentialTableRow.findElement(By.className("credentialEditButton"));
				break;
			}
		}
		Assertions.assertNotNull(credentialEditButton);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(credentialEditButton)).click();
		WebElement credentialPassword = driver.findElement(By.id("credential-password"));
		webDriverWait.until(ExpectedConditions.elementToBeClickable(credentialPassword));
		Assertions.assertEquals(credentialPassword.getAttribute("value"), this.credentialPassword);
		credentialPassword.clear();
		credentialPassword.sendKeys(credentialPasswordUpdated);
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		webDriverWait.until(ExpectedConditions.elementToBeClickable(credentialUsername));
		credentialUsername.clear();
		credentialUsername.sendKeys(credentialUsernameUpdated);
		WebElement credentialUrl = driver.findElement(By.id("credential-url"));
		webDriverWait.until(ExpectedConditions.elementToBeClickable(credentialUrl));
		credentialUrl.clear();
		credentialUrl.sendKeys(credentialUrlUpdated);
		WebElement saveCredentialButton = driver.findElement(By.id("saveCredentialButton"));
		saveCredentialButton.click();
	}

	private void editNote() {
		WebElement notesTable = driver.findElement(By.id("notesTable"));
		WebElement noteEditButton = null;
		List<WebElement> noteTableRows = notesTable.findElements(By.className("noteTableRow"));
		for (WebElement noteTableRow : noteTableRows) {
			WebElement noteTableRowTitle = noteTableRow.findElement(By.className("noteTitle"));
			WebElement noteTableRowDescription = noteTableRow.findElement(By.className("noteDescription"));
			if (noteTableRowTitle.getAttribute("innerHTML").equals(noteTitle) &&
					noteTableRowDescription.getAttribute("innerHTML").equals(noteDescription)) {
				noteEditButton = noteTableRow.findElement(By.className("noteEditButton"));
				break;
			}
		}
		Assertions.assertNotNull(noteEditButton);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(noteEditButton)).click();
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		webDriverWait.until(ExpectedConditions.elementToBeClickable(noteTitle));
		noteTitle.clear();
		noteTitle.sendKeys(noteTitleUpdated);
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		webDriverWait.until(ExpectedConditions.elementToBeClickable(noteDescription));
		noteDescription.clear();
		noteDescription.sendKeys(noteDescriptionUpdated);
		WebElement saveNoteButton = driver.findElement(By.id("saveNoteButton"));
		saveNoteButton.click();
	}

	public void verifyCredential(String credentialUrl, String credentialUsername, String credentialPassword, boolean notFound, boolean atHome) {
		if (! atHome) {
			Assertions.assertEquals("Result", driver.getTitle());
			WebElement resultSuccess = driver.findElement(By.id("resultSuccess"));
			webDriverWait.until(ExpectedConditions.elementToBeClickable(resultSuccess)).click();
		}

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		WebElement navCredentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		js.executeScript("arguments[0].click()", navCredentialsTab);

		WebElement credentialsTable = driver.findElement(By.id("credentialsTable"));
		boolean entryVerified = false;
		WebElement credentialEditButton = null;
		List<WebElement> credentialTableRows = credentialsTable.findElements(By.className("credentialTableRow"));
		for (WebElement credentialTableRow : credentialTableRows) {
			WebElement credentialTableRowUsername = credentialTableRow.findElement(By.className("credentialUsername"));
			WebElement credentialTableRowPassword = credentialTableRow.findElement(By.className("credentialPassword"));
			WebElement credentialTableUrlSpan = credentialTableRow.findElement(By.className("credentialUrlSpan"));
			if (credentialTableRowUsername.getAttribute("innerHTML").equals(credentialUsername) &&
					! credentialTableRowPassword.getAttribute("innerHTML").equals(credentialPassword) &&
					credentialTableUrlSpan.getAttribute("innerHTML").equals(credentialUrl)
			) {
				entryVerified = true;
				credentialEditButton = credentialTableRow.findElement(By.className("credentialEditButton"));
				break;
			}
		}
		if (credentialEditButton != null) {
			webDriverWait.until(ExpectedConditions.elementToBeClickable(credentialEditButton)).click();
			WebElement editCredentialPassword = driver.findElement(By.id("credential-password"));
			if (editCredentialPassword.getAttribute("value").equals(credentialPassword)) {
				entryVerified = true;
			} else {
				entryVerified = false;
			}
			WebElement editCredentialClose = driver.findElement(By.className("editCredentialClose"));
			webDriverWait.until(ExpectedConditions.elementToBeClickable(editCredentialClose)).click();
		}
		if (notFound) {
			Assertions.assertFalse(entryVerified);
		} else {
			Assertions.assertTrue(entryVerified);
		}
	}

	public void verifyNote(String noteTitle, String noteDescription, boolean notFound, boolean atHome) {
		if (! atHome) {
			Assertions.assertEquals("Result", driver.getTitle());
			WebElement resultSuccess = driver.findElement(By.id("resultSuccess"));
			webDriverWait.until(ExpectedConditions.elementToBeClickable(resultSuccess)).click();
		}

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement navNotesTab = driver.findElement(By.id("nav-notes-tab"));
		js.executeScript("arguments[0].click()", navNotesTab);
		WebElement notesTable = driver.findElement(By.id("notesTable"));
		boolean entryVerified = false;
		List<WebElement> noteTableRows = notesTable.findElements(By.className("noteTableRow"));
		for (WebElement noteTableRow : noteTableRows) {
			WebElement noteTableRowTitle = noteTableRow.findElement(By.className("noteTitle"));
			WebElement noteTableRowDescription = noteTableRow.findElement(By.className("noteDescription"));
			if (noteTableRowTitle.getAttribute("innerHTML").equals(noteTitle) &&
					noteTableRowDescription.getAttribute("innerHTML").equals(noteDescription)) {
				entryVerified = true;
				break;
			}
		}
		if (notFound) {
			Assertions.assertFalse(entryVerified);
		} else {
			Assertions.assertTrue(entryVerified);
		}
	}

	// Pre-packaged tests

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling redirecting users
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric:
	 * https://review.udacity.com/#!/rubrics/2724/view
	 */

	@Test
	@Order(9)
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","rt", validPassword);

		// Check if we have been redirected to the log-in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
		driver.get(baseURL + "/logout");
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling bad URLs
	 * gracefully, for example with a custom error page.
	 *
	 * Read more about custom error pages at:
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */

	@Test
	@Order(10)
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","ut", validPassword);
		doLogIn("ut", validPassword);

		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));

		driver.get(baseURL + "/logout");
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code.
	 *
	 * Read more about file size limits here:
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */

	@Test
	@Order(11)
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","lft", validPassword);
		doLogIn("lft", validPassword);

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));
	}

	// Helper methods

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password) {
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign-up was successful.
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depending on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You've successfully signed up!"));
	}
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password) {
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));
	}
}
