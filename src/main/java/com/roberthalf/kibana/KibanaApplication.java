package com.roberthalf.kibana;

import com.roberthalf.kibana.configuration.KibanaConfiguration;
import com.roberthalf.kibana.services.TransferObjects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication
public class KibanaApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(KibanaApplication.class, args);
	}

	@Autowired
	TransferObjects transferObjects;

	@Autowired
    private Environment env;

	@Autowired
	KibanaConfiguration config;

	@Override
	public void run(String... args) throws Exception {

		String kibanaSrc = env.getProperty("kibana_url_source");
		String kibanaDest = env.getProperty("kibana_url_dest");
		log.info("src:"+kibanaSrc);
		log.info("dest:"+kibanaDest);
	    transferObjects.transferIndexPattern(kibanaSrc, kibanaDest);

	}
}
