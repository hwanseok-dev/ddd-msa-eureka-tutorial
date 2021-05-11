package hwanseok.server.study.controller;

import hwanseok.server.study.dto.LayerDto;
import hwanseok.server.study.entity.DivisionLayer;
import hwanseok.server.study.entity.GroupLayer;
import hwanseok.server.study.entity.OrganizationLayer;
import hwanseok.server.study.entity.ProjectLayer;
import hwanseok.server.study.service.DivisionService;
import hwanseok.server.study.service.GroupService;
import hwanseok.server.study.service.OrganizationService;
import hwanseok.server.study.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/layer", produces = MediaType.APPLICATION_JSON_VALUE)
public class LayerController {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private DivisionService divisionService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private ProjectService projectService;



    /**************************
     * Organization
     * 생성
     * 조회
     * 수정 TODO
     * 삭제 TODO
     *************************/

    @PostMapping("/organization/create")
    public ResponseEntity<OrganizationLayer> createOrganization(@RequestBody LayerDto dto){
        return organizationService.create(dto.getName(), dto.getDescription());
    }

    @GetMapping("/organization/read")
    public ResponseEntity<OrganizationLayer> readOrganization(@RequestParam("uuid") String uuid){
        return organizationService.findByUuid(uuid);
    }

    /**************************
     * Division
     * 생성
     * 생성 및 등록
     * 등록
     * 조회
     * 수정 TODO
     * 삭제 TODO
     *************************/

    @PostMapping("/division/create")
    public ResponseEntity<DivisionLayer> createDivision(@RequestBody LayerDto dto){
        return divisionService.create(dto.getName(), dto.getDescription());
    }

    @PostMapping("/division/create/register")
    public ResponseEntity<OrganizationLayer> createRegisterDivision(@RequestBody LayerDto dto){
        return divisionService.createRegister(dto.getName(), dto.getDescription(), dto.getParentUuid());
    }

    @PostMapping("/division/register")
    public ResponseEntity<OrganizationLayer> registerDivision(@RequestBody LayerDto dto){
        return divisionService.register(dto.getUuid(), dto.getParentUuid());
    }

    @GetMapping("/division/read")
    public ResponseEntity<DivisionLayer> readDivision(@RequestParam("uuid") String uuid){
        return divisionService.findByUuid(uuid);
    }


    /**************************
     * Group
     *************************/

    @PostMapping("/group/create")
    public ResponseEntity<GroupLayer> createGroup(@RequestBody LayerDto dto){
        return groupService.create(dto.getName(), dto.getDescription());
    }

    @GetMapping("/group/read")
    public ResponseEntity<GroupLayer> readGroup(@RequestParam("uuid") String uuid){
        return groupService.findByUuid(uuid);
    }

    @PostMapping("/group/register")
    public ResponseEntity<DivisionLayer> registerGroup(@RequestBody LayerDto dto){
        return groupService.register(dto.getUuid(), dto.getParentUuid());
    }

    /**************************
     * Project
     *************************/

    @PostMapping("/project/create")
    public ResponseEntity<ProjectLayer> createProject(@RequestBody GroupLayer dto){
        return projectService.create(dto.getName(), dto.getDescription());
    }

    @GetMapping("/project/read")
    public ResponseEntity<ProjectLayer> readProject(@RequestParam("uuid") String uuid){
        return projectService.findByUuid(uuid);
    }

    @PostMapping("/project/register")
    public ResponseEntity<ProjectLayer> registerProject(@RequestBody GroupLayer dto){
        return projectService.register(dto.getUuid(), dto.getParent().getUuid());
    }

}

