package com.doomsdaylabs.lrf.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
	@Bean(name="discover.mcastgroup")
	public String discoverMcastgroup(){
		return "235.24.24.24";
	}
}
