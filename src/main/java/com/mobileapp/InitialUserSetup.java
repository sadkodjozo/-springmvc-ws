package com.mobileapp;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mobileapp.io.entity.AuthorityEntity;
import com.mobileapp.io.entity.RoleEntity;
import com.mobileapp.io.entity.UserEntity;
import com.mobileapp.io.repositories.AuthorityRepository;
import com.mobileapp.io.repositories.RoleRepository;
import com.mobileapp.io.repositories.UserRepository;
import com.mobileapp.shared.Roles;
import com.mobileapp.shared.Utils;

@Component
public class InitialUserSetup {
	
	@Autowired
	AuthorityRepository authorityRepository;
	
	@Autowired
	RoleRepository roleRepository;

	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	UserRepository userRepository;
	
	@EventListener
	@Transactional
	public void onApplicationEvent(ApplicationReadyEvent event) {
		System.out.println("From Application ready event..."); 
		
		AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
		AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
		AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");
		
		createRole(Roles.ROLE_USER.name(), Arrays.asList(readAuthority,writeAuthority));
		RoleEntity roleAdmin = createRole(Roles.ROLE_ADMIN.name(), Arrays.asList(readAuthority,writeAuthority,deleteAuthority));
		
		if(roleAdmin == null) return;
		
		UserEntity userAdmin = new UserEntity();
		userAdmin.setFirstName("Sadko");
		userAdmin.setLastName("Djozo");
		userAdmin.setEmail("sadko.djozo@gmail.com");
		userAdmin.setEmailVerificationStatus(true);
		userAdmin.setUserId(utils.generateUserId(30));
		userAdmin.setEncryptedPassword(bCryptPasswordEncoder.encode("12345678"));
		userAdmin.setRoles(Arrays.asList(roleAdmin));
		
		userRepository.save(userAdmin);
	}
	
	@Transactional
	private AuthorityEntity createAuthority(String name) 
	{	
		
		AuthorityEntity authority = authorityRepository.findByName(name);
		
		if(authority == null) {
			authority = new AuthorityEntity(name);
			authorityRepository.save(authority);
		}
		return authority;
		
	}
	
	@Transactional
	private RoleEntity createRole(String name, Collection<AuthorityEntity> authorities)
	{
		RoleEntity role = roleRepository.findByName(name);
		if(role == null) {
			role = new RoleEntity(name);
			role.setAuthorities(authorities);
			roleRepository.save(role);
		}
		return role;
	}

}
