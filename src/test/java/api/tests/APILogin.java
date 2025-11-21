package api.tests;

import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import static io.restassured.RestAssured.given;

public class APILogin
{
	public static String token;
	
	@BeforeClass
	public void setup()
	{
		RestAssured.baseURI = "https://qa.api.aerfinity.acumenaviation.in/";
	}
	
	@Test
	public void testLoginAPI()
	{
		Response response = given()
				.header("Content-Type","application/json")
				.body("{\"email\":\"support@acumen.aero\", \"password\":\"Acumen@123\"}")
				.post("users/auth/login");
		
		String status = response.jsonPath().getString("status");
		int statusCode = response.jsonPath().getInt("statusCode");
		String message = response.jsonPath().getString("message");
		
		System.out.println("Response Body: {");
		System.out.println("    \"status\": \"" + status + "\",");
		System.out.println("    \"statusCode\": " + statusCode + ",");
		System.out.println("    \"message\": \"" + message + "\"");
		System.out.println("}");
		
		if(response.statusCode()==200)
		{
			token = response.jsonPath().getString("token");
			System.out.println("Login API Passed. Token: "+token);
		}
		else
		{
			System.out.println("Login API Failed");
		}
	}
}