package hwanseok.server.study.service;

import hwanseok.server.study.entity.DivisionLayer;
import hwanseok.server.study.entity.LayerClosure;
import hwanseok.server.study.entity.OrganizationLayer;
import hwanseok.server.study.exception.DivisionNotFoundException;
import hwanseok.server.study.exception.DivisionRegisterException;
import hwanseok.server.study.exception.OrganizationNotFoundException;
import hwanseok.server.study.repository.DivisionRepository;
import hwanseok.server.study.repository.OrganizationRepository;
import hwanseok.server.study.repository.V2LayerClosureRepository;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DivisionService {

    @Autowired
    private OrganizationRepository parentRepository;

    @Autowired
    private DivisionRepository repository;

    @Autowired
    private V2LayerClosureRepository closureRepository;


    public ResponseEntity<DivisionLayer> create(String name, String description){
        DivisionLayer me = repository
                .save(DivisionLayer.create(name, description));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(me);
    }

    public ResponseEntity<OrganizationLayer> createRegister(String name, String description, String parentUuid){
        DivisionLayer me = DivisionLayer.create(name, description);
        Optional<OrganizationLayer> parentOptional = parentRepository.findByUuid(parentUuid);
        
        if(parentOptional.isPresent()){
            OrganizationLayer parent = parentOptional.get();
            parent.addChild(me);
            repository.save(me);
            OrganizationLayer ret = parentRepository.save(parent);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ret);
        }else{
            throw new DivisionRegisterException();
        }
    }

    public ResponseEntity<DivisionLayer> findByUuid(String uuid){
        return repository.findByUuid(uuid)
                .map(org -> ResponseEntity
                .status(HttpStatus.OK)
                .body(org))
                .orElseThrow(DivisionNotFoundException::new);
    }



    public ResponseEntity<OrganizationLayer> register(String uuid, String parentUuid){
       Optional<DivisionLayer> meOptional = repository.findByUuid(uuid);
       Optional<OrganizationLayer> parentOptional = parentRepository.findByUuid(parentUuid);

       if(meOptional.isPresent() && parentOptional.isPresent()){
           OrganizationLayer parent = parentOptional.get();
           DivisionLayer me = meOptional.get();
           parent.addChild(me);

           Optional<LayerClosure> closureOptional = closureRepository.findByParentAndChild(parent.getId(), me.getId());
           if(closureOptional.isPresent()){
               throw new DivisionRegisterException();
           }
           OrganizationLayer ret = parentRepository.save(parent);
           return ResponseEntity
                   .status(HttpStatus.OK)
                   .body(ret);
       }else{
           throw new DivisionRegisterException();
       }
    }
}