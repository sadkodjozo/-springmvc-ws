package com.mobileapp.service;

import java.util.List;

import com.mobileapp.shared.dto.AddressDTO;

public interface AddressService {
	
	List<AddressDTO> getAddresses(String userId);
	AddressDTO getAddress(String addressId);
}
