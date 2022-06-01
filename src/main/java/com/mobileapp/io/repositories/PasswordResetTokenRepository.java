package com.mobileapp.io.repositories;

import org.springframework.data.repository.CrudRepository;

import com.mobileapp.io.entity.PasswordResetTokenEntity;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long> {
	
	PasswordResetTokenEntity findByToken(String token);
}
