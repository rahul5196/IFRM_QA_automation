package NegativeScenarios.POSNegativeScenarios;

import Operations.RuleOperations;
import Operations.TxnBody;
import POS.POSTxn;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.Managed;
import org.hamcrest.core.Is;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class NegativeHighAmtMcc extends PageObject {

    String txnId = UUID.randomUUID().toString();
    String txnId1 = UUID.randomUUID().toString();
    String txnId2 = UUID.randomUUID().toString();
    String ruleId = "6918f0dd-4884-4899-93a7-b8b34c70aeb3";
    String authorization = "authToken";
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYXJ0aHBAdXRrYXJzaC5iYW5rIiwicm9sZXMiOlsiYXRtOnN1cGVydmlzb3IiLCJwb3M6c3VwZXJ2aXNvciIsImVjb206c3VwZXJ2aXNvciIsImlibWI6c3VwZXJ2aXNvciJdLCJpc3MiOiJodHRwczpcL1wvcGFjNGoub3JnIiwiZXhwIjoxNTk3MTg0ODc5LCJ1c2VySWQiOiIyIiwiaWF0IjoxNTk3MTUyNDc5LCJqdGkiOiIzYmMzNzc3ZC1kNTQwLTQ4ODktYWRmYS1mODczYjMwMTZjODQifQ.NmtT09uNCQZDspk5LU-93CC5-id1QLqlfzmjRnE5UQA";
    LocalDate currentDate = LocalDate.now();
    DateTime dt = new DateTime();
    DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.hourMinuteSecond();
    String currentTime = dateTimeFormatter.print(dt);
    String postUrl = "http://localhost:9999/api/v1/ifrm/pos/ruleengine/json/score";

    @Managed
    WebDriver webDriver;

    @Given("I login as supervisor to activate HighAmtMcc rule for negative scenarios")
    public void iLoginAsSupervisorToActivateHighAmtMccRuleForNegativeScenarios() {
        webDriver.get("https://13.126.40.171/");
        webDriver.manage().window().maximize();
        WebElementFacade username = $(By.name("username"));
        username.sendKeys("parthp");
        webDriver.findElement(By.xpath("//button[@class='btn btn-primary']")).click();
        $(By.name("type"));
        webDriver.findElement((By.xpath("//option[contains(text(),'supervisor')]"))).click();
        webDriver.findElement(By.name("password")).clear();
        webDriver.findElement(By.name("password")).sendKeys("parth123");
        $(By.xpath("//button[contains(@class,'btn btn-primary')]")).click();
        webDriver.findElement(By.xpath("//a[contains(text(),'Rules')]")).click();
        webDriver.findElement(By.xpath("//a[contains(text(),'pos')]")).click();
        System.out.println("Login Complete");
    }

    @And("The HighAmtMcc rule is on for negative scenarios")
    public void theHighAmtMccRuleIsOnForNegativeScenarios() {
        given().cookie(authorization, token).
                when().put(RuleOperations.posActivateRule(ruleId)).
                then().statusCode(200);

        given().cookie(authorization, token).
                when().put(RuleOperations.posRuleEnable(ruleId)).
                then().statusCode(200);

        given().cookie(authorization, token).
                when().put(RuleOperations.posRuleExplicit(ruleId)).
                then().statusCode(200);
    }

    @And("Check the HighAmtMcc rule is active or not for negative scenarios")
    public void checkTheHighAmtMccRuleIsActiveOrNotForNegativeScenarios() {
        webDriver.get("https://13.126.40.171/dsl");
        webDriver.findElement(By.xpath("//a[contains(text(),'pos')]")).click();
        webDriver.findElement(By.xpath("//div[@class='tab-pane active']//div//option[contains(text(),'Activated')]")).click();
    }

    @When("I post high amount mcc transaction for negative scenarios")
    public void iPostHighAmountMccTransactionForNegativeScenarios() {
        POSTxn posTxn = new POSTxn("1200","6080140888812222","001000",2500,currentDate + " " + currentTime,txnId,currentDate + " " + currentTime,"356","71020171000C","200", "8299", "810697","000000415430", "235678", "RB0058080000000", "Pune", "356", "1300000", "000013");
        HashMap<String, Object> highAmtMcc = TxnBody.POSPurchaseTxnBody(posTxn);
        given().contentType(ContentType.JSON).
                with().body(highAmtMcc).
                when().post(postUrl).
then().assertThat().body("responseCode", Is.is("202")).statusCode(200);

    }

    @And("If i get response code as {int} for HighAmtMcc for negative scenarios")
    public void ifIGetResponseCodeAsForHighAmtMccForNegativeScenarios(int arg0) {
        POSTxn posTxn1 = new POSTxn("1200", "6080140888812222", "001000", 35000, currentDate + " " + currentTime, txnId1, currentDate + " " + currentTime, "356", "71020171000C", "200", "8220", "810697", "000000415430", "235678", "RB0058080000000", "Pune", "356", "1300000","000013");
        HashMap<String,Object> highAmtMcc1 = TxnBody.POSPurchaseTxnBody(posTxn1);
        given().contentType(ContentType.JSON).
                with().body(highAmtMcc1).
                when().post(postUrl).
then().assertThat().body("responseCode", Is.is("202")).statusCode(200);

        POSTxn posTxn2 = new POSTxn("1200","6080140888812222","001000",5000,currentDate + " " + currentTime,txnId2,currentDate + " " + currentTime,"356","posEntryMode", "71020171000C","200","5541","810697","000000415430","235678","RB0058080000000","356", "1300000","000013");
        HashMap<String,Object> highAmtMcc2 = TxnBody.POSPurchaseTxnBody(posTxn2);
        given().contentType(ContentType.JSON).
                with().body(highAmtMcc2).
                when().post(postUrl).
then().assertThat().body("responseCode", Is.is("202")).statusCode(200);
    }

    @Then("I turn off the HighAmtMcc for negative scenarios")
    public void iTurnOffTheHighAmtMccForNegativeScenarios() {
        given().cookie(authorization, token).
                when().put(RuleOperations.posActivateRule(ruleId)).
                then().statusCode(200);

        given().cookie(authorization, token).
                when().put(RuleOperations.posRuleExplicit(ruleId)).
                then().statusCode(200);
    }


}

