package lab.microservice.labservice.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import lab.microservice.labservice.business.UserBusiness;
import lab.microservice.labservice.model.User;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DbInitializationUtil implements InitializingBean {

	@Autowired
	UserBusiness userBusiness;

	Faker faker = new Faker();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("=========== Db Initialization===========");
		try {
			createUsers();
		} catch (Exception e) {
			log.error("Error trying to save new users",e);
		}
	}

	private void createUsers() throws Exception {
		
		for (int i = 0; i < 10; i++) {
			User user = getUser();
			
			userBusiness.salvar(user);
		}
	}

	public User getUser() {
		String firstName = faker.gameOfThrones().character();
		
		User user = User.builder()
						.city(faker.gameOfThrones().city())
						.email(firstName.toLowerCase().replace(" ", ".")+"@gameofthrones.com")
						.name(firstName)
						.phone(faker.phoneNumber().phoneNumber())
						.salary(getRandomSalary())
						.birthDate(getRandomBirthDate())
						.build();
		return user;
	}
	
	public List<String> getCities(){
		int size = 20;
		List<String> list = new ArrayList<String>(size);
		
		for (int i = 0; i < size; i++) {
			String city = faker.gameOfThrones().city();
			while(list.contains(city))
				city = faker.gameOfThrones().city();
			
			list.add(city);
		}
		
		return list;
	}

	private Date getRandomBirthDate() {
		Random rand = new Random();
		int nextIntDay = rand.nextInt(31) + 1;
		int nextIntMonth = rand.nextInt(12) + 1;
		int nextIntYear = 90 + rand.nextInt(10);
		
		Calendar cal = Calendar.getInstance();
		cal.set(nextIntYear, nextIntMonth, nextIntDay);
		
		return cal.getTime();
	}

	private double getRandomSalary() {
		Random rand = new Random(10000);
		return rand.nextInt(72000);
	}


}
