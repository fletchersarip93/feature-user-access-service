package com.featureuseraccess;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.featureuseraccess.entity.Authority;
import com.featureuseraccess.entity.Feature;
import com.featureuseraccess.entity.User;
import com.featureuseraccess.repository.FeatureRepository;
import com.featureuseraccess.repository.UserRepository;
import com.featureuseraccess.security.Role;
import com.featureuseraccess.service.ResourceNotFoundException;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
@WithMockUser(authorities = "PRODUCT_MANAGER")
class UserFeatureAccessServiceApplicationTests {

	private static final int NON_ZERO_INT = 3;
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
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private FeatureRepository featureRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private boolean isInitialized;
	
	@BeforeEach
	public void initDatabase() {
		if (!isInitialized) {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			
			Authority auth = new Authority();
			auth.setAuthority(Role.PRODUCT_MANAGER.toString());
			auth = entityManager.persist(auth);
			
			User user = new User();
			user.setEmail(USER_EMAIL);
			user.setPassword(passwordEncoder.encode("password"));
			user.setEnabled(true);
			user.addAuthority(auth);
			user = entityManager.persist(user);
			
			User userNotAllowed = new User();
			userNotAllowed.setEmail(USER_EMAIL_NOT_ALLOWED);
			userNotAllowed.setPassword(passwordEncoder.encode("password"));
			userNotAllowed.setEnabled(true);
			entityManager.persist(userNotAllowed);
			
			Feature feature = new Feature();
			feature.setName(FEATURE_NAME);
			feature.allowUser(user);
			entityManager.persist(feature);
			
			Feature featureVeryImportant = new Feature();
			featureVeryImportant.setName(FEATURE_NAME_NOT_ALLOWED);
			entityManager.persist(featureVeryImportant);
			
			entityManager.flush();
			
			isInitialized = true;
		}
	}
	
	@Test
	void contextLoads() {
	}
	
	//////// GET tests ////////
	
	//// POSITIVE TESTS ////
	// positive test on can access TRUE
	@Test
	public void getFeatureOnAllowedUserEmailShouldReturn200OkCanAccessTrue() throws Exception {
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL)
				.queryParam("featureName", FEATURE_NAME))
		.andExpect(jsonPath("canAccess", is(true)));
	}
	
	// positive test on can access TRUE case-insensitive email
	@Test
	public void getFeatureOnAllowedUserWithDifferentStringCaseForEmailShouldReturn200OkCanAccessTrue() throws Exception {
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL.toLowerCase())
				.queryParam("featureName", FEATURE_NAME))
		.andExpect(jsonPath("canAccess", is(true)));
		
		
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL.toUpperCase())
				.queryParam("featureName", FEATURE_NAME))
		.andExpect(jsonPath("canAccess", is(true)));
	}
	
	// positive test on can access TRUE case-insensitive featureName
	@Test
	public void getFeatureOnAllowedUserWithDifferentStringCaseForFeatureNameShouldReturn200OkCanAccessTrue() throws Exception {
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL)
				.queryParam("featureName", FEATURE_NAME.toLowerCase()))
		.andExpect(jsonPath("canAccess", is(true)));
		
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL)
				.queryParam("featureName", FEATURE_NAME.toUpperCase()))
		.andExpect(jsonPath("canAccess", is(true)));
	}
	
	// positive test on can access FALSE not allowed user
	@Test
	public void getFeatureOnNotAllowedUserShouldReturn200OkCanAccessFalse() throws Exception {
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL_NOT_ALLOWED)
				.queryParam("featureName", FEATURE_NAME))
		.andExpect(jsonPath("canAccess", is(false)));
	}
	
	// positive test on can access FALSE not allowed user case-insensitive email
	@Test
	public void getFeatureOnNotAllowedUserWithDifferentStringCaseForEmailShouldReturn200OkCanAccessFalse() throws Exception {
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL_NOT_ALLOWED.toLowerCase())
				.queryParam("featureName", FEATURE_NAME))
		.andExpect(jsonPath("canAccess", is(false)));
		
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL_NOT_ALLOWED.toUpperCase())
				.queryParam("featureName", FEATURE_NAME))
		.andExpect(jsonPath("canAccess", is(false)));
	}
	
	// positive test on can access FALSE not allowed user case-insensitive featureName
	@Test
	public void getFeatureOnNotAllowedUserWithDifferentStringCaseForFeatureNameShouldReturn200OkCanAccessTrue() throws Exception {
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL_NOT_ALLOWED)
				.queryParam("featureName", FEATURE_NAME.toLowerCase()))
		.andExpect(jsonPath("canAccess", is(false)));
		
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL_NOT_ALLOWED)
				.queryParam("featureName", FEATURE_NAME.toUpperCase()))
		.andExpect(jsonPath("canAccess", is(false)));
	}
	
	// positive test on can access FALSE not allowed feature case-insensitive email
	@Test
	public void getFeatureOnNotAllowedFeatureWithDifferentStringCaseForEmailShouldReturn200OkCanAccessFalse() throws Exception {
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL.toLowerCase())
				.queryParam("featureName", FEATURE_NAME_NOT_ALLOWED))
		.andExpect(jsonPath("canAccess", is(false)));
		
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL.toUpperCase())
				.queryParam("featureName", FEATURE_NAME_NOT_ALLOWED))
		.andExpect(jsonPath("canAccess", is(false)));
	}
	
	// positive test on can access FALSE not allowed feature case-insensitive featureName
	@Test
	public void getFeatureOnNotAllowedFeatureWithDifferentStringCaseForFeatureNameShouldReturn200OkCanAccessFalse() throws Exception {
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL)
				.queryParam("featureName", FEATURE_NAME_NOT_ALLOWED.toLowerCase()))
		.andExpect(jsonPath("canAccess", is(false)));
		
		mockMvc
		.perform(get("/feature")
				.queryParam("email", USER_EMAIL)
				.queryParam("featureName", FEATURE_NAME_NOT_ALLOWED.toUpperCase()))
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
		requestBody.put("email", USER_EMAIL_NOT_ALLOWED);
		requestBody.put("featureName", FEATURE_NAME);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(200));
		
		assertThatDatabaseHasRecord(USER_EMAIL_NOT_ALLOWED, FEATURE_NAME, true);
	}

	// valid and existent case sensitive test for email (use an existent email but with different case)
	public void postFeatureWithCaseInsensitiveEmailShouldReturn200() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL_NOT_ALLOWED.toLowerCase());
		requestBody.put("featureName", FEATURE_NAME);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(200));
		
		assertThatDatabaseHasRecord(USER_EMAIL_NOT_ALLOWED, FEATURE_NAME, true);
		
		requestBody.put("email", USER_EMAIL_NOT_ALLOWED.toUpperCase());
		requestBody.put("featureName", FEATURE_NAME_NOT_ALLOWED);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(200));
		
		assertThatDatabaseHasRecord(USER_EMAIL_NOT_ALLOWED, FEATURE_NAME_NOT_ALLOWED, true);
	}
	
	// valid format and existent case sensitive test for featureName (use an existent featureName but with different case)
	public void postFeatureWithCaseInsensitiveFeatureNameShouldReturn200() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL_NOT_ALLOWED);
		requestBody.put("featureName", FEATURE_NAME.toLowerCase());
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(200));
		
		assertThatDatabaseHasRecord(USER_EMAIL_NOT_ALLOWED, FEATURE_NAME, true);
		
		requestBody.put("email", USER_EMAIL);
		requestBody.put("featureName", FEATURE_NAME.toUpperCase());
		requestBody.put("enable", false);

		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(200));
		
		assertThatDatabaseHasRecord(USER_EMAIL, FEATURE_NAME, false);
	}
	
	// non-boolean data type enable parameter
	@Test
	public void whenPostFeatureWithNonBooleanEnableParameterThenReturn200() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL_NOT_ALLOWED);
		requestBody.put("featureName", FEATURE_NAME);
		requestBody.put("enable", NON_ZERO_INT);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(200));
		
		assertThatDatabaseHasRecord(USER_EMAIL_NOT_ALLOWED, FEATURE_NAME, true);
	}
	
	//// NEGATIVE TESTS ////
	
	// security test: unauthorized user
	@Test @WithAnonymousUser
	public void whenPostFeatureWithUnauthenticatedUserShouldReturn401() throws Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL_NOT_ALLOWED);
		requestBody.put("featureName", FEATURE_NAME);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(401));
		
		assertThatDatabaseHasRecord(USER_EMAIL_NOT_ALLOWED, FEATURE_NAME, false);
	}
	
	// security test: unauthorized user
	@Test @WithMockUser(authorities = "DEVELOPER")
	public void whenPostFeatureWithoutTheRightAuthorityShouldReturn403() throws Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL_NOT_ALLOWED);
		requestBody.put("featureName", FEATURE_NAME);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(403));
		
		assertThatDatabaseHasRecord(USER_EMAIL_NOT_ALLOWED, FEATURE_NAME, false);
	}
	
	// no email parameter
	@Test
	public void whenPostFeatureWithoutEmailParameterThenReturn400BadRequest() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("featureName", FEATURE_NAME_NOT_ALLOWED);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem(EMAIL_MUST_NOT_BE_BLANK)));
		
		assertThatFeatureHasNoUserAllowed(FEATURE_NAME_NOT_ALLOWED);
	}

	// null email parameter
	@Test
	public void whenPostFeatureWithNullEmailParameterThenReturn400BadRequest() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", null);
		requestBody.put("featureName", FEATURE_NAME_NOT_ALLOWED);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem(EMAIL_MUST_NOT_BE_BLANK)));
		
		assertThatFeatureHasNoUserAllowed(FEATURE_NAME_NOT_ALLOWED);
	}
	
	// invalid data type email parameter
	@Test
	public void whenPostFeatureWithNonStringEmailParameterThenReturn400BadRequest() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", NON_ZERO_INT);
		requestBody.put("featureName", FEATURE_NAME_NOT_ALLOWED);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem(EMAIL_MUST_BE_A_WELL_FORMED_EMAIL_ADDRESS)));
		
		assertThatFeatureHasNoUserAllowed(FEATURE_NAME_NOT_ALLOWED);
	}
	
	// empty string email parameter
	@Test
	public void whenPostFeatureWithEmptyStringEmailParameterThenReturn400BadRequest() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", "");
		requestBody.put("featureName", FEATURE_NAME_NOT_ALLOWED);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem(EMAIL_MUST_NOT_BE_BLANK)));
		
		assertThatFeatureHasNoUserAllowed(FEATURE_NAME_NOT_ALLOWED);
	}
	
	// invalid format email parameter value
	@Test
	public void whenPostFeatureWithInvalidEmailParameterFormatThenReturn400BadRequest() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", "user1emaildomain.com");
		requestBody.put("featureName", FEATURE_NAME_NOT_ALLOWED);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem(EMAIL_MUST_BE_A_WELL_FORMED_EMAIL_ADDRESS)));
		
		assertThatFeatureHasNoUserAllowed(FEATURE_NAME_NOT_ALLOWED);
	}
	
	// valid format but non-existent email parameter
	@Test
	public void whenPostFeatureWithNonExistentEmailParameterThenReturn304() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", NON_EXISTENT_EMAIL);
		requestBody.put("featureName", FEATURE_NAME_NOT_ALLOWED);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(304));
		
		assertThatFeatureHasNoUserAllowed(FEATURE_NAME_NOT_ALLOWED);
	}
	
	// no featureName parameter
	@Test
	public void whenPostFeatureWithoutFeatureNameParameterThenReturn400BadRequest() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL_NOT_ALLOWED);
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
		requestBody.put("email", USER_EMAIL_NOT_ALLOWED);
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
	public void whenPostFeatureWithNonStringFeatureNameParameterThenTreatParamValueAsStringAndReturn304() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL_NOT_ALLOWED);
		requestBody.put("featureName", NON_ZERO_INT);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(304));
	}
	
	// empty string featureName parameter
	@Test
	public void whenPostFeatureWithEmptyStringFeatureNameParameterThenReturn400BadRequest() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL_NOT_ALLOWED);
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
	public void whenPostFeatureWithNonExistentFeatureNameParameterThenReturn304() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL_NOT_ALLOWED);
		requestBody.put("featureName", NON_EXISTENT_FEATURE_NAME);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(304));
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
		
		assertThatDatabaseHasRecord(USER_EMAIL, FEATURE_NAME, true);
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
		
		assertThatDatabaseHasRecord(USER_EMAIL, FEATURE_NAME, true);
	}
	
	// test multiple bad request messages
	@Test
	public void whenPostFeatureWithMissingAllMandatoryParametersInRequestBodyThenReturn400BadRequestWithMultipleMessages() throws Exception {
		JSONObject requestBody = new JSONObject();
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString()))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasSize(3)))
		.andExpect(jsonPath("messages", hasItem(FEATURE_NAME_MUST_NOT_BE_BLANK)))
		.andExpect(jsonPath("messages", hasItem(EMAIL_MUST_NOT_BE_BLANK)))
		.andExpect(jsonPath("messages", hasItem(ENABLE_MUST_NOT_BE_NULL)));
	}
	
	// test malformed JSON request body
	@Test
	public void whenPostFeatureWithMalformedJsonRequestBodyReturn400BadRequest() throws JSONException, Exception {
		JSONObject requestBody = new JSONObject();
		requestBody.put("email", USER_EMAIL_NOT_ALLOWED);
		requestBody.put("featureName", FEATURE_NAME);
		requestBody.put("enable", true);
		
		mockMvc
		.perform(post("/feature")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody.toString().replace("}", ",}")))
		.andExpect(status().is(400))
		.andExpect(jsonPath("messages", hasItem(containsString("JSON parse error"))));
		
		assertThatDatabaseHasRecord(USER_EMAIL_NOT_ALLOWED, FEATURE_NAME, false);
	}
	
	private void assertThatDatabaseHasRecord(String userEmail, String featureName, boolean canAccess) {
		Optional<Feature> featureOptional = featureRepository.findByNameIgnoreCase(featureName);
		assertThat(featureOptional.isPresent()).isTrue();
		Optional<User> userOptional = userRepository.findByEmailIgnoreCase(userEmail);
		assertThat(userOptional.isPresent()).isTrue();
		assertThat(featureOptional.get().checkAllows(userOptional.get())).isEqualTo(canAccess);
	}
	
	private void assertThatFeatureHasNoUserAllowed(String featureName) throws ResourceNotFoundException {
		Feature feature = featureRepository.findByNameIgnoreCase(featureName).orElseThrow(()->new ResourceNotFoundException());
		assertThat(feature.getUsersAllowed()).hasSize(0);
	}

}
