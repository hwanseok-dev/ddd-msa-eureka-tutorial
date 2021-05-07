package hwanseok.server.study.service;

import hwanseok.server.study.entity.GroupLayer;
import hwanseok.server.study.entity.ProjectLayer;
import hwanseok.server.study.exception.ProjectNotFoundException;
import hwanseok.server.study.exception.ProjectRegisterException;
import hwanseok.server.study.repository.GroupRepository;
import hwanseok.server.study.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ResponseEntity<ProjectLayer> findByUuid(String uuid){
        return projectRepository.findByUuid(uuid)
                .map(project -> ResponseEntity
                .status(HttpStatus.OK)
                .body(project))
                .orElseThrow(ProjectNotFoundException::new);
    }

    public ResponseEntity<ProjectLayer> create(String name, String description){
        ProjectLayer project = projectRepository
                .save(ProjectLayer.create(name, description));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(project);
    }

    public ResponseEntity<ProjectLayer> register(String uuid, String parentUuid){
       Optional<ProjectLayer> optional = projectRepository.findByUuid(uuid);
       Optional<GroupLayer> parentOptional = groupRepository.findByUuid(parentUuid);
       if(optional.isPresent() && parentOptional.isPresent()){
           ProjectLayer project = optional.get();
           GroupLayer group = parentOptional.get();
           project.setParent(group);
           group.addChild(project);
           projectRepository.save(project);
           groupRepository.save(group);
           return ResponseEntity
                   .status(HttpStatus.OK)
                   .body(project);
       }else{
           throw new ProjectRegisterException();
       }
    }
}