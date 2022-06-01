package com.mobileapp.io.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mobileapp.io.entity.RoleEntity;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
	
	RoleEntity findByName(String name);

}
