package lab.microservice.labservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lab.microservice.labservice.model.TourPackage;
import lab.microservice.labservice.repo.TourPackageRepository;

@Service
public class TourPackageService {
	@Autowired
	private TourPackageRepository tourPackageRepository;

	public TourPackage createTourPackage(String code, String name) {
		if(!tourPackageRepository.existsById(code))
			tourPackageRepository.save(new TourPackage(code,name));
		
		return null;
	}
	
	public Iterable<TourPackage> lookup(){
		return tourPackageRepository.findAll();
	}
	
	public long total() {
		return tourPackageRepository.count();
	}
}
