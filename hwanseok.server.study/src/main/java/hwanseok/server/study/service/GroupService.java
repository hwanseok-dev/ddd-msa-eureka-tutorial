package hwanseok.server.study.service;

import hwanseok.server.study.entity.DivisionLayer;
import hwanseok.server.study.entity.GroupLayer;
import hwanseok.server.study.entity.LayerClosure;
import hwanseok.server.study.entity.OrganizationLayer;
import hwanseok.server.study.exception.DivisionRegisterException;
import hwanseok.server.study.exception.GroupNotFoundException;
import hwanseok.server.study.exception.GroupRegisterException;
import hwanseok.server.study.repository.DivisionRepository;
import hwanseok.server.study.repository.GroupRepository;
import hwanseok.server.study.repository.V2LayerClosureRepository;
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
    private DivisionRepository parentRepository;

    @Autowired
    private GroupRepository repository;

    @Autowired
    private V2LayerClosureRepository closureRepository;


    public ResponseEntity<GroupLayer> findByUuid(String uuid){
        return repository.findByUuid(uuid)
                .map(group -> ResponseEntity
                .status(HttpStatus.OK)
                .body(group))
                .orElseThrow(GroupNotFoundException::new);
    }

    public ResponseEntity<GroupLayer> create(String name, String description){
        GroupLayer groupLayer = repository.save(GroupLayer.create(name, description));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(groupLayer);
    }

    public ResponseEntity<DivisionLayer> register(String uuid, String parentUuid){
        Optional<GroupLayer> meOptional = repository.findByUuid(uuid);
        Optional<DivisionLayer> parentOptional = parentRepository.findByUuid(parentUuid);

        if(meOptional.isPresent() && parentOptional.isPresent()){
            DivisionLayer parent = parentOptional.get();
            GroupLayer me = meOptional.get();
            parent.addChild(me);

            Optional<LayerClosure> closureOptional = closureRepository.findByParentAndChild(parent.getId(), me.getId());
            if(closureOptional.isPresent()){
                throw new GroupRegisterException();
            }
            DivisionLayer ret = parentRepository.save(parent);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ret);
        }else{
            throw new DivisionRegisterException();
        }
    }
}