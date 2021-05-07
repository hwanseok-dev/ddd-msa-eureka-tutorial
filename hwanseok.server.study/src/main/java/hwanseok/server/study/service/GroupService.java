package hwanseok.server.study.service;

import hwanseok.server.study.entity.DivisionLayer;
import hwanseok.server.study.entity.GroupLayer;
import hwanseok.server.study.entity.OrganizationLayer;
import hwanseok.server.study.exception.DivisionNotFoundException;
import hwanseok.server.study.exception.DivisionRegisterException;
import hwanseok.server.study.exception.GroupNotFoundException;
import hwanseok.server.study.exception.GroupRegisterException;
import hwanseok.server.study.repository.DivisionRepository;
import hwanseok.server.study.repository.GroupRepository;
import hwanseok.server.study.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private GroupRepository groupRepository;

    public ResponseEntity<GroupLayer> findByUuid(String uuid){
        return groupRepository.findByUuid(uuid)
                .map(group -> ResponseEntity
                .status(HttpStatus.OK)
                .body(group))
                .orElseThrow(GroupNotFoundException::new);
    }

    public ResponseEntity<GroupLayer> create(String name, String description){
        GroupLayer groupLayer = groupRepository.save(GroupLayer.create(name, description));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(groupLayer);
    }

    public ResponseEntity<GroupLayer> register(String uuid, String parentUuid){
        Optional<GroupLayer> optional = groupRepository.findByUuid(uuid);
        Optional<DivisionLayer> parentOptional = divisionRepository.findByUuid(parentUuid);
       if(optional.isPresent() && parentOptional.isPresent()){
           GroupLayer group = optional.get();
           DivisionLayer division = parentOptional.get();
           group.setParent(division);
           division.addChild(group);
           divisionRepository.save(division);
           groupRepository.save(group);
           return ResponseEntity
                   .status(HttpStatus.OK)
                   .body(group);
       }else{
           throw new GroupRegisterException();
       }
    }
}