package com.human.springboot;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LWebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/img/place/**") // Change the mapping pattern if needed
	            .addResourceLocations("C:/Users/admin/git/travelproject/TravelingProject/src/main/resources/static/img/place/");
	}

}
