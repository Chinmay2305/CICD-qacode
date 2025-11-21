package api.tests;

import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIUser
{
	private int userId;
	
	@BeforeClass
	public void setup()
	{
		RestAssured.baseURI = "https://qa.api.aerfinity.acumenaviation.in/";
	}
	
	String email = "chinmayshiv@gmail.com";
	
	@Test(dependsOnMethods = "api.tests.APILogin.testLoginAPI")
	public void addUserAPI()
	{
		String userPayload = "{\n" +
                "    \"name\": \"Test name\",\n" +
                "    \"email\": \""+email+"\",\n" +
                "    \"mobile_number\": \"100203004\",\n" +
                "    \"designation\": \"Manager\",\n" +
                "    \"department\": \"Business\",\n" +
                "    \"roles\": \"Test\"\n" +
                "}";
		
		Response response = given()
				.header("Content-Type","application/json")
				.header("Authorization", "Bearer " + APILogin.token)
				.body(userPayload)
				.post("users/users");
		
		// PRINT EVERYTHING
	    System.out.println("Status Code: " + response.statusCode());
	    System.out.println("Content-Type: " + response.getContentType());
	    System.out.println("Raw Response: " + response.asString());
		
	 // Only parse JSON if Content-Type is JSON
	    if (response.getContentType() != null && response.getContentType().contains("application/json")) {
	        String status = response.jsonPath().getString("status");
	        int statusCode = response.jsonPath().getInt("statusCode");
	        String message = response.jsonPath().getString("message");

	        System.out.println("Response Body: {");
	        System.out.println("\"status\": \"" + status + "\",");
	        System.out.println("\"statusCode\": " + statusCode + ",");
	        System.out.println("\"message\": \"" + message + "\"");
	        System.out.println("}");
	    } else {
	        System.out.println("The response was not JSON, skipping parsing.");
	    }

		if(response.statusCode()==200)
		{
			userId = response.jsonPath().getInt("data.id");
			System.out.println("User adding API Passed with user ID: "+userId);
		}
		else
		{
			System.out.println("User adding API Failed");
		}
	}
	
	
	@Test(dependsOnMethods = "api.tests.APILogin.getUserAfterAdd")
	public void editUserAPI()
	{
	    // ✅ Prepare the payload as a map (clean and avoids invalid JSON)
	    Map<String, Object> payload = new HashMap<>();
	    payload.put("id", userId);  // ✅ ID must be in the body
	    payload.put("name", "Chinmay Agrawal");
	    payload.put("designation", "QA");
	    payload.put("department", "Quality");
	    payload.put("roles", "AerFinity Super Admin");
	    payload.put("mobile_number", "8238997664");

	    // ✅ Send PATCH request
	    Response response = given()
	            .header("Content-Type", "application/json")
	            .header("Authorization", "Bearer " + APILogin.token)
	            .body(payload)
	            .patch("users/users");  // ✅ Confirm this is the correct endpoint (e.g. check /users/users/{id})

	    // ✅ Log status
	    System.out.println("Status Code: " + response.statusCode());
	    System.out.println("Raw Response: " + response.asString());

	    // ✅ Check for JSON and parse safely
	    if (response.getContentType() != null && response.getContentType().contains("application/json")) {
	        String status = response.jsonPath().getString("status");
	        Integer statusCode = response.jsonPath().get("statusCode"); // ⚠️ Use Integer to avoid NullPointerException
	        String message = response.jsonPath().getString("message");

	        System.out.println("Response Body: {");
	        System.out.println("\"status\": \"" + status + "\",");
	        System.out.println("\"statusCode\": " + statusCode + ",");
	        System.out.println("\"message\": \"" + message + "\"");
	        System.out.println("}");
	    } else {
	        System.out.println("The response was not JSON, skipping parsing.");
	    }

	    // ✅ Status code check
	    if (response.statusCode() == 200) {
	        System.out.println("User edit API Passed");
	    } else {
	        System.out.println("User edit API Failed");
	    }
	}

	
	public void getuserAPI()
	{
		Response response = given()
	            .header("Content-Type", "application/json")
	            .header("Authorization", "Bearer " + APILogin.token)
	            .get("users/users");
		
		System.out.println("Status Code: " + response.statusCode());
		
		List<Map<String, Object>> users = response.jsonPath().getList("data");
		for(Map<String, Object> user:users)
	    {
	    	if(email.equals(user.get("email")))
	    	{
	    		System.out.println("Matched User: " + user);
	    	}
	    }
	    
	    if(response.statusCode()==200)
	    {
	    	boolean userfound = response.asString().contains(email);
	    	
	    	if(userfound)
	    	{
	    		System.out.println("User found in GET response: " + email);
	    	}
	    	else
	    	{
	    		System.out.println("User NOT found in GET response.");
	    	}
	    }
	    else
	    {
	    	System.out.println("Get User API Failed");
	    }
	}
	
	@Test(dependsOnMethods = "api.tests.APIUser.addUserAPI")
	public void getUserAfterAdd() {
	    getuserAPI(); // 🔁 Reused code
	}

	@Test(dependsOnMethods = "api.tests.APIUser.editUserAPI")
	public void getUserAfterEdit() {
		getuserAPI(); // 🔁 Reused code
	}
	
	@Test(dependsOnMethods = "api.tests.APILogin.getUserAfterEdit")
	public void deleteUserAPI()
	{
		Response response = given()
	            .header("Authorization", "Bearer " + APILogin.token)
	            .delete("users/users/" + userId); // 👈 Pass userId in the URL path

	    System.out.println("Status Code: " + response.statusCode());
	    System.out.println("Raw Response: " + response.asString());

	    if (response.getContentType() != null && response.getContentType().contains("application/json")) {
	        String status = response.jsonPath().getString("status");
	        Integer statusCode = response.jsonPath().get("statusCode");
	        String message = response.jsonPath().getString("message");

	        System.out.println("Response Body: {");
	        System.out.println("\"status\": \"" + status + "\",");
	        System.out.println("\"statusCode\": " + statusCode + ",");
	        System.out.println("\"message\": \"" + message + "\"");
	        System.out.println("}");
	    } else {
	        System.out.println("The response was not JSON, skipping parsing.");
	    }

	    if (response.statusCode() == 200 || response.statusCode() == 204) {
	        System.out.println("User delete API Passed");
	    } else {
	        System.out.println("User delete API Failed");
	    }
	}
}