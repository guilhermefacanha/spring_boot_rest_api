package lab.microservice.labservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lab.microservice.labservice.model.Tour;
import lab.microservice.labservice.model.TourPackage;
import lab.microservice.labservice.model.enumeration.Difficulty;
import lab.microservice.labservice.model.enumeration.Region;
import lab.microservice.labservice.repo.TourPackageRepository;
import lab.microservice.labservice.repo.TourRepository;

@Service
public class TourService {
	
	@Autowired
	private TourPackageRepository tourPackageRepository;
	@Autowired
	private TourRepository tourRepository;
	
	public Tour createTour(String title, String description, String blurb, Integer price, String duration, String bullets, 
			String keywords, String tourPackageName, Difficulty difficulty, Region region) {
		
		TourPackage tourPackage = tourPackageRepository.findByName(tourPackageName);
		
		if(tourPackage==null) {
			throw new RuntimeException("Tour package does not exist: "+tourPackageName);
		}
		
		return tourRepository.save(new Tour(title,description,blurb,price,duration,bullets,keywords,tourPackage,difficulty,region));
		
	}
	
	public Iterable<Tour> lookup(){
		return tourRepository.findAll();
	}
	
	public long total() {
		return tourRepository.count();
	}
}
