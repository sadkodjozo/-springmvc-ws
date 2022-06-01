package com.mobileapp.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobileapp.service.AddressService;
import com.mobileapp.service.UserService;
import com.mobileapp.shared.Roles;
import com.mobileapp.shared.dto.AddressDTO;
import com.mobileapp.shared.dto.UserDto;
import com.mobileapp.ui.model.request.PasswordResetModel;
import com.mobileapp.ui.model.request.PasswordResetRequestModel;
import com.mobileapp.ui.model.request.UserDetailsRequestModel;
import com.mobileapp.ui.model.response.AddressesRest;
import com.mobileapp.ui.model.response.OperationStatusModel;
import com.mobileapp.ui.model.response.RequestOperationName;
import com.mobileapp.ui.model.response.RequestOperationStatus;
import com.mobileapp.ui.model.response.UserRest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/users") // http://localhost:8080/users
@CrossOrigin(origins= {"http://localhost:8088", "http://Photoappuserapi-env.eba-ukue9ggg.us-east-1.elasticbeanstalk.com"})
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;

	/*
	 * GET USER
	 */
	
	@PostAuthorize("hasRole('ADMIN') or returnObject.userId == principal.userId")
	@ApiOperation(value="The Get User Details Web Service Endpoint", notes="This Web Service Endpoint returns User Details. "
			+ "Uses public user_id in URL Path. For example: /users/uhf3fj3r4bfb")
	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE , MediaType.APPLICATION_XML_VALUE})
	public UserRest getUser(@PathVariable String id) {

//		UserRest returnValue = new UserRest();

		UserDto userDto = userService.getUserByUserId(id);
//		BeanUtils.copyProperties(userDto, returnValue);
		
		ModelMapper modelMapper = new ModelMapper();
		UserRest returnValue = modelMapper.map( userDto, UserRest.class);

		return returnValue;
	}

	
	/*
	 * CREATE USER
	 */
	@ApiOperation(value="The Create User Details Web Service Endpoint", notes="This Web Service Endpoint cretes new user. ")
	@PostMapping(
				consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
				produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
				)

	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

		UserRest returnValue = new UserRest();

//		UserDto userDto = new UserDto();
//		BeanUtils.copyProperties(userDetails, userDto);
		
		ModelMapper modelMapper = new ModelMapper();
//		OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		userDto.setRoles(new HashSet<>(Arrays.asList(Roles.ROLE_USER.name())));

		UserDto createdUser = userService.createUser(userDto);
		returnValue = modelMapper.map(createdUser, UserRest.class);

		return returnValue;

	}
	
	
	/*
	 * UPDATE USER
	 */
	@ApiOperation(value="The Update User Details Web Service Endpoint", notes="This Web Service Endpoint updates User Details. "
			+ "Uses public user_id in URL Path. For example: /users/uhf3fj3r4bfb")
	@PutMapping(path = "/{id}", 
			consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE }
				)
	
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {

		UserRest returnValue = new UserRest();

//		UserDto userDto = new UserDto();
//		BeanUtils.copyProperties(userDetails, userDto);
		UserDto userDto = new ModelMapper().map(userDetails, UserDto.class);
		
		UserDto updateUser = userService.updateUser(id, userDto);
//		BeanUtils.copyProperties(updateUser, returnValue);
		returnValue = new ModelMapper().map(updateUser, UserRest.class);

		return returnValue;

	}
	
	/*
	 * DELETE USER
	 */
	@Transactional
	@PreAuthorize("hasRole('ADMIN') or #id == principal.userId")
	//@Secured("ROLE_ADMIN")
	@ApiOperation(value="The Delete User Details Web Service Endpoint", notes="This Web Service Endpoint deletes specific user from database. "
			+ "Uses public user_id in URL Path. For example: /users/uhf3fj3r4bfb")
	@DeleteMapping(path = "/{id}", 
			produces = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE }
				  )
	public OperationStatusModel deleteUser(@PathVariable String id) {
		
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		
		userService.deleteUser(id);
		
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
	
	/*
	 * GET USERS
	 * http://localhost:8080/mobile-app-ws/users/
	 */
	@ApiOperation(value="The Get Users Details Web Service Endpoint", notes="This Web Service Endpoint returns Users Details. "
			+ "Uses page and limit parameret. For example: /users?limit=2&page=0")
	@ApiImplicitParams({
		@ApiImplicitParam (name="authorization",value="${userController.authorizationHeader.description}", paramType="header")
	})
	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	
	public List<UserRest> getUsers(@RequestParam(value="page",defaultValue="0") int page,
								@RequestParam(value="limit", defaultValue="25") int limit)
	{
		List<UserRest> returnValue = new ArrayList<>();
		
		List<UserDto> users = userService.getUsers(page,limit);
		
		for (UserDto userDto : users ) {
			UserRest userModel = new UserRest();
			//BeanUtils.copyProperties(userDto, userModel);
			userModel = new ModelMapper().map(userDto, UserRest.class);
			
			returnValue.add(userModel);
		}
		
		return returnValue;
	}
	
	/*
	 * GET USER ADDRESSES
	 * http://localhost:8080/mobile-app-ws/users/io66sYbwLAe6ZjU8ZQYI/addresses
	 */
	
	@ApiImplicitParams({
		@ApiImplicitParam (name="authorization",value="${userController.authorizationHeader.description}", paramType="header")
	})
	
	@GetMapping(path="/{userId}/addresses")
	public CollectionModel<AddressesRest> getUserAddresses(@PathVariable String userId) {

		List<AddressesRest> returnValue = new ArrayList<>();
		
		List<AddressDTO> addressDTO = addressService.getAddresses(userId);
		
		if(addressDTO !=null && !addressDTO.isEmpty() )
		{				
		Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
		returnValue = new ModelMapper().map(addressDTO, listType);
		
		for(AddressesRest addressRest : returnValue) {
			Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
					.getUserAddress(userId, addressRest.getAddressId()))
					.withSelfRel();
			addressRest.add(selfLink);
		}
		}
		
		Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).withRel("user");
		
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
				.getUserAddresses(userId))
				.withSelfRel();
		
		return CollectionModel.of(returnValue, userLink, selfLink);
	}
	
	/*
	 * GET USER ADDRESS
	 * http://localhost:8080/mobile-app-ws/users/io66sYbwLAe6ZjU8ZQYI/addresses/dvY3gdsEdc
	 */
	
	@ApiImplicitParams({
		@ApiImplicitParam (name="authorization",value="${userController.authorizationHeader.description}", paramType="header")
	})
	
	@GetMapping(path="/{userId}/addresses/{addressId}")
	public EntityModel<AddressesRest> getUserAddress(@PathVariable String userId, @PathVariable String addressId) {

		AddressDTO addressDTO = addressService.getAddress(addressId);
		
		AddressesRest returnValue = new ModelMapper().map(addressDTO, AddressesRest.class);
		
		// http://localhost:8080/users/<userId>/addresses/<addressId>
		Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).withRel("user");
		Link userAddressesLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(userId))
//				.slash(userId)
//				.slash("addresses")
				.withRel("addresses");
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
				.getUserAddress(userId, addressId))
				.withSelfRel();
//				.slash(userId)
//				.slash("addresses")
//				.slash(addressId)
				
		
//		returnValue.add(userLink);
//		returnValue.add(userAddressesLink);
//		returnValue.add(selfLink);
						
		return EntityModel.of(returnValue, Arrays.asList(userLink,userAddressesLink,selfLink));
		
	}
	
	/*
	 * VERIFY EMAIL TOKEN
	 * //http://localhost:8080/mobile-app-ws/users/email-verification
	 */
	
		
	@GetMapping(path="/email-verification")
	public OperationStatusModel verifyEmailToken(@RequestParam(value="token") String token) {
		
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
		
		boolean isVerified = userService.verifyEmailToken(token);
		
		if(isVerified)
		{
			returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		}else {
			returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
		}
		return  returnValue;
	}
	
	
	/*
	 *  PASSWORD RESET REQUEST
	 */
	
	// http://localhost:8080/mobile-app-ws/users/password-reset-request
	@PostMapping(path="/password-reset-request")
	public OperationStatusModel requestReset(@RequestBody PasswordResetRequestModel passwordResetRequestModel) {
		
		OperationStatusModel returnValue = new OperationStatusModel();
		
		boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());
		
		
		returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
		returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
		
		if (operationResult) {
			
			returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		}
			
		return returnValue;
		
		
	}
	
	
	/*
	 *  PASSWORD RESET 
	 */
	
	// http://localhost:8080/mobile-app-ws/users/password-reset
	
	@PostMapping(path="/password-reset")
	public OperationStatusModel resetPassword(@RequestBody PasswordResetModel passwordResetModel) {
		
		OperationStatusModel returnValue = new OperationStatusModel();
		
		boolean operationResult = userService.resetPassword(
				passwordResetModel.getToken(),
				passwordResetModel.getPassword()); 
		
		
		returnValue.setOperationName(RequestOperationName.PASSWORD_RESET.name());
		returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
		
		if (operationResult) {
			
			returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		}
			
		return returnValue;
		
		
	}
}
