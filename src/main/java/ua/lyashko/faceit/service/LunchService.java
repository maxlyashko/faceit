package ua.lyashko.faceit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lyashko.faceit.entity.LunchEntity;
import ua.lyashko.faceit.repository.LunchRepository;

@Service
public class LunchService {

    private final LunchRepository lunchRepository;

    @Autowired
    public LunchService(LunchRepository lunchRepository) {
        this.lunchRepository = lunchRepository;
    }

    public void createLunch(LunchEntity lunch) {
        lunchRepository.save(lunch);
    }
}
