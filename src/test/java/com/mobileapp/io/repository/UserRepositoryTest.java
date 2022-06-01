package com.mobileapp.io.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mobileapp.io.entity.AddressEntity;
import com.mobileapp.io.entity.UserEntity;
import com.mobileapp.io.repositories.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {
	/*
	@Autowired
	UserRepository userRepository;
	
	static boolean recordsCreated = false;

	@BeforeEach
	void setUp() throws Exception {
		if(!recordsCreated) createRecords();

	}

	@Test
	void testGetVerifiedUsers() {
		Pageable pageableRequest = PageRequest.of(1, 1);
		Page<UserEntity> page = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
		assertNotNull(page);
		
		List<UserEntity> userEntities = page.getContent();
		assertNotNull(userEntities);
		assertTrue(userEntities.size() == 1);
	}
	
	@Test
	final void testFindUserByFirstName()
	{
		String firstName = "Sadko";
		List<UserEntity> users = userRepository.findUserByFirstName(firstName);
		assertNotNull(users);
		assertTrue(users.size() ==2);
		
		UserEntity user = users.get(0);
		assertEquals(user.getFirstName(),firstName);
		
	
	}
	
	@Test
	final void testFindUserByLastName()
	{
		String lastName = "Djozo";
		List<UserEntity> users = userRepository.findUserByLastName(lastName);
		assertNotNull(users);
		assertTrue(users.size() == 2);
		
		UserEntity user = users.get(0);
		assertTrue(user.getLastName().equals(lastName));
		
	
	}
	
	@Test 
	final void testFindUsersByKeyword()
	{
		String keyword="ozo";
		List<UserEntity> users = userRepository.findUserByKeyword(keyword);
		assertNotNull(users);
		assertTrue(users.size() == 2);
		
		UserEntity user = users.get(0);
		assertTrue(
				user.getLastName().contains(keyword) ||
				user.getFirstName().contains(keyword)
				);
	}
	
	
	@Test
	final void testFindUserFirstNameAndLastNameFromKeyword()
	{
		String keyword = "Sad";
		List<Object[]> users = userRepository.findUserFirstNameAndLastNameByKeyword(keyword);
		assertNotNull(users);
		assertTrue(users.size() == 2);
		
		Object[] user = users.get(0);
		assertTrue(user.length == 2);
		
		String userFirstName = String.valueOf(user[0]);
		String userLastName = String.valueOf(user[1]);
		
		assertNotNull(userFirstName);
		assertNotNull(userLastName);
		
		System.out.println("First name = "+ userFirstName);
		System.out.println("Last name = "+ userLastName);
	
	}
	
	@Test
	final void testUpdateUserEmailVerificationStatus()
	{
		boolean newEmailVerificationStatus = true;
		
		userRepository.updateUserEmailVerificationStatus(newEmailVerificationStatus, "1a2b3c");
		
		UserEntity storedUserDetails = userRepository.findByUserId("1a2b3c");
		
		boolean storedEmailVerificationStatus=storedUserDetails.getEmailVerificationStatus();
		
		assertTrue(newEmailVerificationStatus == storedEmailVerificationStatus);
	}
	
	@Test
	final void testFindUserEntityByUserId()
	{
		String userId = "1a2b3c";
		
		UserEntity userEntity = userRepository.findUserEntityByUserId(userId);
		
		assertNotNull(userEntity);
		assertTrue(userEntity.getUserId().equals(userId));
	}
	
	
	@Test
	final void testGetUserEntityFullNameById()
	{
		String userId = "1a2b3c";
		
		List<Object[]> records = userRepository.getUserEntityFullNameById(userId);
		assertNotNull(records);
		assertTrue(records.size() == 1);
		
		Object[] userDetails = records.get(0); // Object[] user = {firstname, lastname};
		
		String userFirstName = String.valueOf(userDetails[0]);
		String userLastName = String.valueOf(userDetails[1]);
		
		assertNotNull(userFirstName);
		assertNotNull(userLastName);
		
		System.out.println("First name = "+ userFirstName);
		System.out.println("Last name = "+ userLastName);
		
	}
	
	@Test
	final void testUpdateUserEntityEmailVerificationStatus()
	{
		boolean newEmailVerificationStatus = true;
		
		userRepository.updateUserEntityEmailVerificationStatus(newEmailVerificationStatus, "1a2b3c");
		
		UserEntity storedUserDetails = userRepository.findByUserId("1a2b3c");
		
		boolean storedEmailVerificationStatus=storedUserDetails.getEmailVerificationStatus();
		
		assertTrue(newEmailVerificationStatus == storedEmailVerificationStatus);
	}
	
	*/
	/*
	 * Create records
	 */
	
	/*
	private void createRecords()
	{
		// Prepare User Entity
				UserEntity userEntity = new UserEntity();
				userEntity.setFirstName("Sadko");
				userEntity.setLastName("Djozo");
				userEntity.setUserId("1a2b3c");
				userEntity.setEncryptedPassword("ABC");
				userEntity.setEmail("test@test.com");
				userEntity.setEmailVerificationStatus(true);

				// Prepare User Adresses
				AddressEntity addressEntity = new AddressEntity();
				addressEntity.setType("shipping");
				addressEntity.setAddressId("ajkdhkajd");
				addressEntity.setCity("Sarajevo");
				addressEntity.setCountry("Bosna");
				addressEntity.setPostalCode("71000");
				addressEntity.setStreetName("123 StreetName");

				List<AddressEntity> addresses = new ArrayList<>();
				addresses.add(addressEntity);

				userEntity.setAddresses(addresses);

				userRepository.save(userEntity);

				// Second user

				// Prepare User Entity
				UserEntity userEntity2 = new UserEntity();
				userEntity2.setFirstName("Sadko");
				userEntity2.setLastName("Djozo");
				userEntity2.setUserId("2aokk3ak");
				userEntity2.setEncryptedPassword("yyy");
				userEntity2.setEmail("test2@test.com");
				userEntity2.setEmailVerificationStatus(true);

				// Prepare User Addresses
				AddressEntity addressEntity2 = new AddressEntity();
				addressEntity2.setType("shipping");
				addressEntity2.setAddressId("ajkdhkajd");
				addressEntity2.setCity("Sarajevo");
				addressEntity2.setCountry("Bosna");
				addressEntity2.setPostalCode("71000");
				addressEntity2.setStreetName("123 StreetName");

				List<AddressEntity> addresses2 = new ArrayList<>();
				addresses2.add(addressEntity2);

				userEntity2.setAddresses(addresses2);

				userRepository.save(userEntity2);
				
				recordsCreated = true;
	}
*/
}
