package com.entelgy;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ChromeTest {

	static String path = System.getProperty("user.dir");
	private WebDriver driver;
	static File srcFile = null;

	@BeforeClass
	public static void setupClass() {
		WebDriverManager.chromedriver().setup();
	}

	@Before
	public void setupTest() {
		driver = new ChromeDriver();
	}

	@After
	public void teardown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	public void test() throws InterruptedException, IOException, EmailException {
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.navigate().to("https://sigcpd.policia.gob.pe/NuestraGente/NuestraGente.aspx");

		driver.findElement(By.xpath("//*[@id=\"ModalComunicado1\"]/div/div/div[2]/button")).click();

		driver.findElement(By.id("btnLugarvacuna")).click();

		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.name("txtNroDni")));
		driver.findElement(By.name("txtNroDni")).sendKeys("07462444"); // 28200068
		wait.until(ExpectedConditions.elementToBeClickable(By.name("btnConsultar")));
		driver.findElement(By.name("btnConsultar")).click();

		Thread.sleep(2000);
		
//		WebDriverWait wait2 = new WebDriverWait(driver, 20);
//		wait2.until(ExpectedConditions.elementToBeClickable(By.id("gvDetalle")));
//		srcFile = driver.findElement(By.id("gvDetalle")).getScreenshotAs(OutputType.FILE);

		
		if(driver.findElement(By.id("gvDetalle")).isEnabled()) {
			srcFile = driver.findElement(By.id("gvDetalle")).getScreenshotAs(OutputType.FILE);
		}else {
			srcFile = driver.findElement(By.id("lblmsj")).getScreenshotAs(OutputType.FILE);
		}
		
		FileUtils.copyFile(srcFile, new File(path+"/src/test/resources/screenshot/screen.png"));
		
		  // Create the attachment
		  EmailAttachment attachment = new EmailAttachment();
		  attachment.setPath(path+"/src/test/resources/screenshot/screen.png");
		  attachment.setDisposition(EmailAttachment.ATTACHMENT);
		  attachment.setDescription("verificacion de punto de vacunacion");
		  attachment.setName("Joel Ramos");
		
		  // Create the email message
		  Email email = new MultiPartEmail();
		  
		  email.setHostName("smtp.googlemail.com");
		  email.setSmtpPort(465);
		  email.setSSLOnConnect(true);
		  email.setAuthenticator(new DefaultAuthenticator("testperu2021@gmail.com", "test2064"));
		  
		  email.setFrom("testperu2021@gmail.com","Commons");		 
		  email.addTo("joel2064@gmail.com");
		  email.setSubject("TestMail");
		  email.setMsg("Resultados");
		
		  ((MultiPartEmail) email).attach(attachment);
		  
		  email.send();
		
	}

}
