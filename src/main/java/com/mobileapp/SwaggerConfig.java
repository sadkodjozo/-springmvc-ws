package com.mobileapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;

@Configuration
public class SwaggerConfig {
	
	Contact contact = new Contact(
            "Sadko Djozo",
            "https://github.com/sadkodjozo", 
            "sadko.djozo@gmail.com"
    );
    
    List<VendorExtension> vendorExtensions = new ArrayList<>();
	
	ApiInfo apiInfo = new ApiInfo(
			"Photo app RESTful Web Service documentation",
			"This pages documents Photo app RESTful Web Service endpoints", 
			"1.0",
			"https://github.com/sadkodjozo", 
			contact, 
			"Apache 2.0",
			"http://www.apache.org/licenses/LICENSE-2.0", 
			vendorExtensions);
	
	
	@Bean
	public Docket apiDocket() {

		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.protocols(new HashSet<>(Arrays.asList("HTTP","HTTPs")))
				.apiInfo(apiInfo)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.mobileapp"))
				.paths(PathSelectors.any())
				.build();

		return docket;

	}

}
