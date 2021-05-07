package hwanseok.server.study.service;

import hwanseok.server.study.entity.DivisionLayer;
import hwanseok.server.study.entity.OrganizationLayer;
import hwanseok.server.study.exception.DivisionNotFoundException;
import hwanseok.server.study.exception.DivisionRegisterException;
import hwanseok.server.study.exception.OrganizationNotFoundException;
import hwanseok.server.study.repository.DivisionRepository;
import hwanseok.server.study.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DivisionService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private DivisionRepository divisionRepository;

    public ResponseEntity<DivisionLayer> findByUuid(String uuid){
        return divisionRepository.findByUuid(uuid)
                .map(org -> ResponseEntity
                .status(HttpStatus.OK)
                .body(org))
                .orElseThrow(DivisionNotFoundException::new);
    }

    public ResponseEntity<DivisionLayer> create(String name, String description){
        DivisionLayer divisionLayer = divisionRepository
                .save(DivisionLayer.create(name, description));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(divisionLayer);
    }

    public ResponseEntity<DivisionLayer> register(String uuid, String parentUuid){
       Optional<DivisionLayer> divOptional = divisionRepository.findByUuid(uuid);
       Optional<OrganizationLayer> orgOptional = organizationRepository.findByUuid(parentUuid);
       if(divOptional.isPresent() && orgOptional.isPresent()){
           OrganizationLayer organization = orgOptional.get();
           DivisionLayer division = divOptional.get();
           organization.addChild(division);
           division.setParent(organization);
           divisionRepository.save(division);
           organizationRepository.save(organization);
           return ResponseEntity
                   .status(HttpStatus.OK)
                   .body(division);
       }else{
           throw new DivisionRegisterException();
       }
    }
}