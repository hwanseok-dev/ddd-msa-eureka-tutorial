package hwanseok.server.study.controller;

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
     *************************/

    @PostMapping("/organization/create")
    public ResponseEntity<OrganizationLayer> createOrganization(@RequestBody OrganizationLayer dto){
        return organizationService.create(dto.getName(), dto.getDescription());
    }

    @GetMapping("/organization/read")
    public ResponseEntity<OrganizationLayer> readOrganization(@RequestParam("uuid") String uuid){
        return organizationService.findByUuid(uuid);
    }

    /**************************
     * Division
     *************************/

    @PostMapping("/division/create")
    public ResponseEntity<DivisionLayer> createDivision(@RequestBody DivisionLayer dto){
        return divisionService.create(dto.getName(), dto.getDescription());
    }

    @GetMapping("/division/read")
    public ResponseEntity<DivisionLayer> readDivision(@RequestParam("uuid") String uuid){
        return divisionService.findByUuid(uuid);
    }

    @PostMapping("/division/register")
    public ResponseEntity<DivisionLayer> registerDivision(@RequestBody DivisionLayer dto){
        return divisionService.register(dto.getUuid(), dto.getParent().getUuid());
    }

    /**************************
     * Group
     *************************/

    @PostMapping("/group/create")
    public ResponseEntity<GroupLayer> createGroup(@RequestBody GroupLayer dto){
        return groupService.create(dto.getName(), dto.getDescription());
    }

    @GetMapping("/group/read")
    public ResponseEntity<GroupLayer> readGroup(@RequestParam("uuid") String uuid){
        return groupService.findByUuid(uuid);
    }

    @PostMapping("/group/register")
    public ResponseEntity<GroupLayer> registerGroup(@RequestBody GroupLayer dto){
        return groupService.register(dto.getUuid(), dto.getParent().getUuid());
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

