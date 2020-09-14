package com.featureuseraccess.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.featureuseraccess.controller.exception.ResourceNotFoundException;
import com.featureuseraccess.service.ErrorMessageFactory;
import com.featureuseraccess.service.FeatureUserAccessService;

@WebMvcTest(FeatureUserAccessRestController.class)
@WithMockUser(authorities = "PRODUCT_MANAGER")
public class FeatureUserAccessRestControllerTest {

	private static final int NUMBER = 3;
	private static final String NON_EXISTENT_FEATURE_NAME = "non-existent-feature-name";
	private static final String NON_EXISTENT_EMAIL = "nonexistentuser@emaildomain.com";
	private static final String EMAIL_MUST_NOT_BE_BLANK = "email: must not be blank";
	private static final String EMAIL_MUST_BE_A_WELL_FORMED_EMAIL_ADDRESS = "email: must be a well-formed email address";
	private static final String ENABLE_MUST_NOT_BE_NULL = "enable: must not be null";
	private static final String FEATURE_NAME_MUST_NOT_BE_BLANK = "featureName: must not be blank";
	private static final String FEATURE_NAME = "inventory-management-feature";
	private static final String FEATURE_NAME_NOT_ALLOWED = "very-important-feature-name";
	private static final String USER_EMAIL = "user1@emaildomain.com";
	private static final String USER_EMAIL_NOT_ALLOWED = "user2@emaildomain.com";

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private FeatureUserAccessService featureUserAccessService;
	
	//////// GET tests ////////
	
	//// POSITIVE TESTS ////
	// positive test on can access TRUE
	@Test
	public void getFeatureOnAllowedUserEmailShouldReturn200OkCanAccessTrue() throws Exception {
		given(featureUserAccessService.checkAccess(USER_EMAIL, FEATURE_NAME))
		.willReturn(true);
		
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL)
				.queryParam("featureName", FEATURE_NAME))
		.andExpect(status().is(200))
		.andExpect(jsonPath("canAccess", is(true)));
	}
	
	// positive test on can access TRUE case-insensitive email
	@Test
	public void getFeatureOnAllowedUserWithDifferentStringCaseForEmailShouldReturn200OkCanAccessTrue() throws Exception {
		given(featureUserAccessService.checkAccess(argThat(equalToIgnoringCase(USER_EMAIL)), argThat(equalToIgnoringCase(FEATURE_NAME))))
		.willReturn(true);
		
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL.toLowerCase())
				.queryParam("featureName", FEATURE_NAME))
		.andExpect(status().is(200))
		.andExpect(jsonPath("canAccess", is(true)));
		
		
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL.toUpperCase())
				.queryParam("featureName", FEATURE_NAME))
		.andExpect(status().is(200))
		.andExpect(jsonPath("canAccess", is(true)));
	}
	
	// positive test on can access TRUE case-insensitive featureName
	@Test
	public void getFeatureOnAllowedUserWithDifferentStringCaseForFeatureNameShouldReturn200OkCanAccessTrue() throws Exception {
		given(featureUserAccessService.checkAccess(argThat(equalToIgnoringCase(USER_EMAIL)), argThat(equalToIgnoringCase(FEATURE_NAME))))
		.willReturn(true);
		
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL)
				.queryParam("featureName", FEATURE_NAME.toLowerCase()))
		.andExpect(status().is(200))
		.andExpect(jsonPath("canAccess", is(true)));
		
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL)
				.queryParam("featureName", FEATURE_NAME.toUpperCase()))
		.andExpect(status().is(200))
		.andExpect(jsonPath("canAccess", is(true)));
	}
	
	// positive test on can access FALSE not allowed user
	@Test
	public void getFeatureOnNotAllowedUserShouldReturn200OkCanAccessFalse() throws Exception {
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL_NOT_ALLOWED)
				.queryParam("featureName", FEATURE_NAME))
		.andExpect(status().is(200))
		.andExpect(jsonPath("canAccess", is(false)));
	}
	
	// positive test on can access FALSE not allowed user case-insensitive email
	@Test
	public void getFeatureOnNotAllowedUserWithDifferentStringCaseForEmailShouldReturn200OkCanAccessFalse() throws Exception {
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL_NOT_ALLOWED.toLowerCase())
				.queryParam("featureName", FEATURE_NAME))
		.andExpect(status().is(200))
		.andExpect(jsonPath("canAccess", is(false)));
		
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL_NOT_ALLOWED.toUpperCase())
				.queryParam("featureName", FEATURE_NAME))
		.andExpect(status().is(200))
		.andExpect(jsonPath("canAccess", is(false)));
	}
	
	// positive test on can access FALSE not allowed user case-insensitive featureName
	@Test
	public void getFeatureOnNotAllowedUserWithDifferentStringCaseForFeatureNameShouldReturn200OkCanAccessTrue() throws Exception {
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL_NOT_ALLOWED)
				.queryParam("featureName", FEATURE_NAME.toLowerCase()))
		.andExpect(status().is(200))
		.andExpect(jsonPath("canAccess", is(false)));
		
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL_NOT_ALLOWED)
				.queryParam("featureName", FEATURE_NAME.toUpperCase()))
		.andExpect(status().is(200))
		.andExpect(jsonPath("canAccess", is(false)));
	}
	
	// positive test on can access FALSE not allowed feature case-insensitive email
	@Test
	public void getFeatureOnNotAllowedFeatureWithDifferentStringCaseForEmailShouldReturn200OkCanAccessFalse() throws Exception {
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL.toLowerCase())
				.queryParam("featureName", FEATURE_NAME_NOT_ALLOWED))
		.andExpect(status().is(200))
		.andExpect(jsonPath("canAccess", is(false)));
		
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL.toUpperCase())
				.queryParam("featureName", FEATURE_NAME_NOT_ALLOWED))
		.andExpect(status().is(200))
		.andExpect(jsonPath("canAccess", is(false)));
	}
	
	// positive test on can access FALSE not allowed feature case-insensitive featureName
	@Test
	public void getFeatureOnNotAllowedFeatureWithDifferentStringCaseForFeatureNameShouldReturn200OkCanAccessFalse() throws Exception {
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL)
				.queryParam("featureName", FEATURE_NAME_NOT_ALLOWED.toLowerCase()))
		.andExpect(status().is(200))
		.andExpect(jsonPath("canAccess", is(false)));
		
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL)
				.queryParam("featureName", FEATURE_NAME_NOT_ALLOWED.toUpperCase()))
		.andExpect(status().is(200))
		.andExpect(jsonPath("canAccess", is(false)));
	}
	
	//// NEGATIVE TESTS ////
	
	// security test: unauthorized user
	@Test @WithAnonymousUser
	public void whenGetFeatureWithUnauthenticatedUserShouldReturn401() throws Exception {
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL)
				.queryParam("featureName", FEATURE_NAME))
		.andExpect(status().is(401));
	}
	
	// security test: unauthorized user
	@Test @WithMockUser(authorities = "DEVELOPER")
	public void whenGetFeatureWithoutTheRightAuthorityShouldReturn403() throws Exception {
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL)
				.queryParam("featureName", FEATURE_NAME))
		.andExpect(status().is(403));
	}
	
	// no email parameter
	@Test
	public void whenGetFeatureWithoutEmailParamThenReturn400BadRequestMissingRequiredParam() throws Exception {
		mockMvc
		.perform(get("/feature")
				.queryParam("featureName", FEATURE_NAME))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem("Required String parameter 'email' is not present")));
	}
	
	// empty email parameter
	@Test
	public void whenGetFeatureWithEmptyEmailParamThenReturn400BadRequest() throws Exception {
		mockMvc
		.perform(get("/feature")
				.queryParam("email", "")
				.queryParam("featureName", FEATURE_NAME))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem(containsString(EMAIL_MUST_NOT_BE_BLANK))));
	}
	
	// invalid email parameter value
	@Test
	public void whenGetFeatureWithInvalidEmailParamFormatThenReturn400BadRequest() throws Exception {
		mockMvc
		.perform(get("/feature")
				.queryParam("email", "user1emaildomain.com")
				.queryParam("featureName", FEATURE_NAME))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem(containsString(EMAIL_MUST_BE_A_WELL_FORMED_EMAIL_ADDRESS))));
	}
	
	// valid but non-existent email parameter
	@Test
	public void whenGetFeatureWithNonExistentEmailParamThenReturn404NotFound() throws Exception {
		given(featureUserAccessService.checkAccess(NON_EXISTENT_EMAIL, FEATURE_NAME))
		.willThrow(new ResourceNotFoundException(ErrorMessageFactory.userEmailNotFound(NON_EXISTENT_EMAIL)));
		
		mockMvc
		.perform(get("/feature")
				.queryParam("email", NON_EXISTENT_EMAIL)
				.queryParam("featureName", FEATURE_NAME))
		.andExpect(status().is(404))
		.andExpect(jsonPath("messages", hasItem("User with email '" + NON_EXISTENT_EMAIL + "' cannot be found.")));
	}
	
	// no featureName parameter
	@Test
	public void whenGetFeatureWithoutFeatureNameParamThenReturn400BadRequestMissingRequiredParam() throws Exception {
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem("Required String parameter 'featureName' is not present")));
	}
	
	// empty featureName parameter
	@Test
	public void whenGetFeatureWithEmptyFeatureNameParamThenReturn400BadRequest() throws Exception {
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL)
				.queryParam("featureName", ""))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem(containsString(FEATURE_NAME_MUST_NOT_BE_BLANK))));
	}
	
	// valid but non-existent featureName parameter
	@Test
	public void whenGetFeatureWithNonExistentFeatureNameParamThenReturn404NotFound() throws Exception {
		given(featureUserAccessService.checkAccess(USER_EMAIL, NON_EXISTENT_FEATURE_NAME))
		.willThrow(new ResourceNotFoundException(ErrorMessageFactory.featureNameNotFound(NON_EXISTENT_FEATURE_NAME)));
		
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL)
				.queryParam("featureName", NON_EXISTENT_FEATURE_NAME))
		.andExpect(status().is(404))
		.andExpect(jsonPath("messages", hasItem("Feature with name '" + NON_EXISTENT_FEATURE_NAME + "' cannot be found.")));
	}
	
	//////// POST tests ////////
	//// POSITIVE TESTS ////
	@Test
	public void postFeatureShouldReturn200() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL);
		requestBody.put("featureName", FEATURE_NAME);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(200));
	}
	
	// valid and existent case sensitive test for email (use an existent email but with different case)
	public void postFeatureWithCaseInsensitiveEmailShouldReturn200() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL.toLowerCase());
		requestBody.put("featureName", FEATURE_NAME);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(200));
		
		requestBody.put("email", USER_EMAIL.toUpperCase());

		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(200));
	}
	
	// valid format and existent case sensitive test for featureName (use an existent featureName but with different case)
	public void postFeatureWithCaseInsensitiveFeatureNameShouldReturn200() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL);
		requestBody.put("featureName", FEATURE_NAME.toLowerCase());
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(200));
		
		requestBody.put("featureName", FEATURE_NAME.toUpperCase());

		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(200));
	}
	
	// non-boolean data type enable parameter
	@Test
	public void whenPostFeatureWithNonBooleanEnableParameterThenReturn200() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL);
		requestBody.put("featureName", FEATURE_NAME);
		requestBody.put("enable", NUMBER);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(200));
	}
	
	//// NEGATIVE TESTS ////
	
	// security test: unauthorized user
	@Test @WithAnonymousUser
	public void whenPostFeatureWithUnauthenticatedUserShouldReturn401() throws Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL);
		requestBody.put("featureName", FEATURE_NAME);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(401));
	}
	
	// security test: unauthorized user
	@Test @WithMockUser(authorities = "DEVELOPER")
	public void whenPostFeatureWithoutTheRightAuthorityShouldReturn403() throws Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL);
		requestBody.put("featureName", FEATURE_NAME);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(403));
	}
	
	// no email parameter
	@Test
	public void whenPostFeatureWithoutEmailParameterThenReturn400BadRequest() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("featureName", FEATURE_NAME);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem(EMAIL_MUST_NOT_BE_BLANK)));
	}
	
	// null email parameter
	@Test
	public void whenPostFeatureWithNullEmailParameterThenReturn400BadRequest() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", null);
		requestBody.put("featureName", FEATURE_NAME);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem(EMAIL_MUST_NOT_BE_BLANK)));
	}
	
	// invalid data type email parameter
	@Test
	public void whenPostFeatureWithNonStringEmailParameterThenReturn400BadRequest() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", NUMBER);
		requestBody.put("featureName", FEATURE_NAME);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem(EMAIL_MUST_BE_A_WELL_FORMED_EMAIL_ADDRESS)));
	}
	
	// empty string email parameter
	@Test
	public void whenPostFeatureWithEmptyStringEmailParameterThenReturn400BadRequest() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", "");
		requestBody.put("featureName", FEATURE_NAME);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem(EMAIL_MUST_NOT_BE_BLANK)));
	}
	
	// invalid format email parameter value
	@Test
	public void whenPostFeatureWithInvalidEmailParameterFormatThenReturn400BadRequest() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", "user1emaildomain.com");
		requestBody.put("featureName", FEATURE_NAME);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem(EMAIL_MUST_BE_A_WELL_FORMED_EMAIL_ADDRESS)));
	}
	
	// valid format but non-existent email parameter
	@Test
	public void whenPostFeatureWithNonExistentEmailParameterThenReturn404NotFound() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", NON_EXISTENT_EMAIL);
		requestBody.put("featureName", FEATURE_NAME);
		requestBody.put("enable", true);
		
		willThrow(new ResourceNotFoundException(ErrorMessageFactory.userEmailNotFound(NON_EXISTENT_EMAIL)))
		.given(featureUserAccessService)
		.configureAccess(NON_EXISTENT_EMAIL, FEATURE_NAME, true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(404))
		.andExpect(jsonPath("messages", hasItem("User with email '" + NON_EXISTENT_EMAIL + "' cannot be found.")));
	}
	
	// no featureName parameter
	@Test
	public void whenPostFeatureWithoutFeatureNameParameterThenReturn400BadRequest() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem(FEATURE_NAME_MUST_NOT_BE_BLANK)));
	}
	
	// null featureName parameter
	@Test
	public void whenPostFeatureWithNullFeatureNameParameterThenReturn400BadRequest() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL);
		requestBody.put("featureName", null);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem(FEATURE_NAME_MUST_NOT_BE_BLANK)));
	}
	
	// invalid data type featureName parameter
	@Test
	public void whenPostFeatureWithNonStringFeatureNameParameterThenTreatParamValueAsString() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL);
		requestBody.put("featureName", NUMBER);
		requestBody.put("enable", true);
		
		willThrow(new ResourceNotFoundException(ErrorMessageFactory.featureNameNotFound(String.valueOf(NUMBER))))
		.given(featureUserAccessService)
		.configureAccess(USER_EMAIL, String.valueOf(NUMBER), true);;
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(404))
		.andExpect(jsonPath("messages", hasItem("Feature with name '3' cannot be found.")));
	}
	
	// empty string featureName parameter
	@Test
	public void whenPostFeatureWithEmptyStringFeatureNameParameterThenReturn400BadRequest() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL);
		requestBody.put("featureName", "");
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem(FEATURE_NAME_MUST_NOT_BE_BLANK)));
	}
	
	// valid format but non-existent featureName parameter
	@Test
	public void whenPostFeatureWithNonExistentFeatureNameParameterThenReturn404NotFound() throws JSONException, Exception {
		willThrow(new ResourceNotFoundException(ErrorMessageFactory.featureNameNotFound(NON_EXISTENT_FEATURE_NAME)))
		.given(featureUserAccessService)
		.configureAccess(eq(USER_EMAIL), eq(NON_EXISTENT_FEATURE_NAME), anyBoolean());;
		
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL);
		requestBody.put("featureName", NON_EXISTENT_FEATURE_NAME);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(404))
		.andExpect(jsonPath("messages", hasItem("Feature with name '" + NON_EXISTENT_FEATURE_NAME + "' cannot be found.")));
	}
	
	
	// no enable parameter
	@Test
	public void whenPostFeatureWithoutEnableParameterThenReturn400BadRequest() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL);
		requestBody.put("featureName", FEATURE_NAME);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem(ENABLE_MUST_NOT_BE_NULL)));
	}
	
	// null enable parameter
	@Test
	public void whenPostFeatureWithNullEnableParameterThenReturn400BadRequest() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL);
		requestBody.put("featureName", FEATURE_NAME);
		requestBody.put("enable", null);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem(ENABLE_MUST_NOT_BE_NULL)));
	}
	
}
