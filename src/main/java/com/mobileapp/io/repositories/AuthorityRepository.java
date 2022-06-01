package com.mobileapp.io.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mobileapp.io.entity.AuthorityEntity;

@Repository
public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Long> {
	
	AuthorityEntity findByName(String name);
}
