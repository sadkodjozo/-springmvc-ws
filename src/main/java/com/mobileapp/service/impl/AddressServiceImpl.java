package com.mobileapp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobileapp.io.entity.AddressEntity;
import com.mobileapp.io.entity.UserEntity;
import com.mobileapp.io.repositories.AddressRepository;
import com.mobileapp.io.repositories.UserRepository;
import com.mobileapp.service.AddressService;
import com.mobileapp.shared.dto.AddressDTO;


@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AddressRepository addressRepository;

	@Override
	public List<AddressDTO> getAddresses(String userId) {

		List<AddressDTO> returnValue = new ArrayList<>();

		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null)
			return returnValue;

		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);

		for (AddressEntity addressEntity : addresses) {
			returnValue.add(new ModelMapper().map(addressEntity, AddressDTO.class));
		}
		return returnValue;
	}

	@Override
	public AddressDTO getAddress(String addressId) {

		AddressDTO returnValue = null;
		
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		
		if(addressEntity !=null )
		{				
		
			returnValue = new ModelMapper().map(addressEntity, AddressDTO.class);
		}
		return returnValue;
		
	}

}
