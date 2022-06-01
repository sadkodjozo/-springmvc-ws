package com.mobileapp.io.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mobileapp.io.entity.AddressEntity;
import com.mobileapp.io.entity.UserEntity;


@Repository

public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
	List<AddressEntity> findAllByUserDetails(UserEntity userEntity);
	
	AddressEntity findByAddressId(String addressId);
}