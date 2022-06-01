package com.mobileapp.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobileapp.exceptions.UserServiceException;
import com.mobileapp.io.entity.PasswordResetTokenEntity;
import com.mobileapp.io.entity.RoleEntity;
import com.mobileapp.io.entity.UserEntity;
import com.mobileapp.io.repositories.PasswordResetTokenRepository;
import com.mobileapp.io.repositories.RoleRepository;
import com.mobileapp.io.repositories.UserRepository;
import com.mobileapp.security.UserPrincipal;
import com.mobileapp.service.UserService;
import com.mobileapp.shared.AmazonSES;
import com.mobileapp.shared.Utils;
import com.mobileapp.shared.dto.AddressDTO;
import com.mobileapp.shared.dto.UserDto;
import com.mobileapp.ui.model.response.ErrorMessages;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Autowired
	RoleRepository roleRepository;

	@Override
	public UserDto createUser(UserDto user) {

		if (userRepository.findByEmail(user.getEmail()) != null)
			throw new RuntimeException("Record already exist!");
		
		for(int i=0;i<user.getAddresses().size();i++) {
			AddressDTO address = user.getAddresses().get(i);
			address.setUserDetails(user);
			address.setAddressId(utils.generateAddressId(10));
			user.getAddresses().set(i, address);
		}

//		UserEntity userEntity = new UserEntity();
//		BeanUtils.copyProperties(user, userEntity);
		
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);
		
		String publicUserId = utils.generateUserId(20);
		userEntity.setUserId(publicUserId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userEntity.setEmailVerificationToken(Utils.generateEmailVerificationToken(publicUserId));
		
		//Set roles
		
		Collection<RoleEntity> roleEntities = new HashSet<>();
		
		for(String role : user.getRoles()) {
			
		RoleEntity roleEntity =  roleRepository.findByName(role);
		
		if(roleEntity != null) {
			roleEntities.add(roleEntity);			
		}
		}
		
		userEntity.setRoles(roleEntities);
		
		UserEntity storedUserDetails = userRepository.save(userEntity);

//		UserDto returnValue = new UserDto();
//		BeanUtils.copyProperties(storedUserDetails, returnValue);
		UserDto returnValue = modelMapper.map(storedUserDetails, UserDto.class);
		
		// Send an email message to user to verify their email address
		//new AmazonSES().verifyEmail(returnValue);

		return returnValue;
	}

	@Override
	public UserDto getUser(String email) {

		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email);

		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email);
		
		return new UserPrincipal(userEntity);
		
//		return new  User(userEntity.getEmail(), userEntity.getEncryptedPassword(), userEntity.getEmailVerificationStatus(), true,
//				true, true,new ArrayList<>());

//		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		UserDto returnValue = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null)
			throw new UsernameNotFoundException("User with ID: "+userId+" not found");

//		BeanUtils.copyProperties(userEntity, returnValue);
		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map( userEntity, UserDto.class);
		
		return returnValue;
	}

	@Override
	public UserDto updateUser(String userId, UserDto user) {
		
		UserDto returnValue = new UserDto();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		
		UserEntity updatedUserDetails = userRepository.save(userEntity);
		
		//BeanUtils.copyProperties(updatedUserDetails, returnValue);
		returnValue = new ModelMapper().map(updatedUserDetails, UserDto.class);
		
		return returnValue;
	}
	
	@Transactional
	@Override
	public void deleteUser(String userId) {

		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		userRepository.delete(userEntity);
		
		
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> returnValue = new ArrayList<>();
		
		if (page>0) page -=1;
		
		Pageable pageableRequest = PageRequest.of(page, limit);
		Page<UserEntity> usersPage =  userRepository.findAll(pageableRequest);
		List<UserEntity> users = usersPage.getContent();
		
		for (UserEntity userEntity : users ) {
			UserDto userDto = new UserDto();
			//BeanUtils.copyProperties(userEntity, userDto);
			userDto = new ModelMapper().map(userEntity, UserDto.class);
			returnValue.add(userDto);
		}
		return returnValue;
	}

	@Override
	public boolean verifyEmailToken(String token) {
		boolean returnValue = false;
		
		//Find user by token 
		UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);
		
		
		if(userEntity !=null) {
			boolean hasTokenExpired = Utils.hasTokenExpired(token);
			if(!hasTokenExpired) {
				userEntity.setEmailVerificationToken(null);
				userEntity.setEmailVerificationStatus(Boolean.TRUE);
				userRepository.save(userEntity);
				returnValue=true;
			}
		}
		
		return returnValue;
	}

	@Override
	public boolean requestPasswordReset(String email) {
		
		boolean returnValue = false;
		
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if (userEntity == null) {
			return returnValue;
		}
		
		String token = new Utils().generatePasswordResetToken(userEntity.getUserId());
		
		PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setToken(token);
        passwordResetTokenEntity.setUserDetails(userEntity);
        passwordResetTokenRepository.save(passwordResetTokenEntity);
		
		returnValue = new AmazonSES().sendPasswordResetRequest(
				userEntity.getFirstName(),
				userEntity.getEmail(),
				token);
				
		
		return returnValue;
	}
 
	@Override
	public boolean resetPassword(String token, String password) {
        boolean returnValue = false;
        
        if( Utils.hasTokenExpired(token) )
        {
            return returnValue;
        }
 
        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);

        if (passwordResetTokenEntity == null) {
            return returnValue;
        }

        // Prepare new password
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        
        // Update User password in database
        UserEntity userEntity = passwordResetTokenEntity.getUserDetails();
        userEntity.setEncryptedPassword(encodedPassword);
        UserEntity savedUserEntity = userRepository.save(userEntity);
 
        // Verify if password was saved successfully
        if (savedUserEntity != null && savedUserEntity.getEncryptedPassword().equalsIgnoreCase(encodedPassword)) {
            returnValue = true;
        }
   
        // Remove Password Reset token from database
        passwordResetTokenRepository.delete(passwordResetTokenEntity);
        
        return returnValue;
	}

}
