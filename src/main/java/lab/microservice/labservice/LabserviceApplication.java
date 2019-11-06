package lab.microservice.labservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lab.microservice.labservice.business.UserBusiness;
import lab.microservice.labservice.util.DbInitializationUtil;


@SpringBootApplication
public class LabserviceApplication implements CommandLineRunner {
	
	@Autowired
	UserBusiness userBusiness;
	@Autowired
	DbInitializationUtil dbInitializationUtil;

	public static void main(String[] args) {
		SpringApplication.run(LabserviceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Create the default tour packages
		System.out.println("Run application...");
	}

}
