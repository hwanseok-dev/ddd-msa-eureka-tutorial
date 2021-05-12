package hwanseok.server.study.controller;

import hwanseok.server.study.dto.LayerDto;
import hwanseok.server.study.entity.DivisionLayer;
import hwanseok.server.study.entity.GroupLayer;
import hwanseok.server.study.entity.OrganizationLayer;
import hwanseok.server.study.service.DivisionService;
import hwanseok.server.study.service.GroupService;
import hwanseok.server.study.service.OrganizationService;
import io.swagger.annotations.ApiOperation;
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

    /**************************
     * Organization
     * 생성
     * 조회
     * 수정 TODO
     * 삭제 TODO
     *************************/

    @ApiOperation("organization 생성")
    @PostMapping("/organization/create")
    public ResponseEntity<OrganizationLayer> createOrganization(@RequestBody LayerDto dto){
        return organizationService.create(dto.getName(), dto.getDescription());
    }

    @ApiOperation("organization 조회")
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

    @ApiOperation("division 생성")
    @PostMapping("/division/create")
    public ResponseEntity<DivisionLayer> createDivision(@RequestBody LayerDto dto){
        return divisionService.create(dto.getName(), dto.getDescription());
    }

    @ApiOperation("division 생성 및  organization의 자식으로 등록")
    @PostMapping("/division/create/register")
    public ResponseEntity<OrganizationLayer> createRegisterDivision(@RequestBody LayerDto dto){
        return divisionService.createRegister(dto.getName(), dto.getDescription(), dto.getParentUuid());
    }

    @ApiOperation("division organization의 자식으로 등록")
    @PostMapping("/division/register")
    public ResponseEntity<OrganizationLayer> registerDivision(@RequestBody LayerDto dto){
        return divisionService.register(dto.getUuid(), dto.getParentUuid());
    }

    @ApiOperation("division 조회")
    @GetMapping("/division/read")
    public ResponseEntity<DivisionLayer> readDivision(@RequestParam("uuid") String uuid){
        return divisionService.findByUuid(uuid);
    }


    /**************************
     * Group
     *************************/

    @ApiOperation("group 생성")
    @PostMapping("/group/create")
    public ResponseEntity<GroupLayer> createGroup(@RequestBody LayerDto dto){
        return groupService.create(dto.getName(), dto.getDescription());
    }

    @ApiOperation("group 조회")
    @GetMapping("/group/read")
    public ResponseEntity<GroupLayer> readGroup(@RequestParam("uuid") String uuid){
        return groupService.findByUuid(uuid);
    }

    @ApiOperation("group division의 자식으로 등록")
    @PostMapping("/group/register")
    public ResponseEntity<DivisionLayer> registerGroup(@RequestBody LayerDto dto){
        return groupService.register(dto.getUuid(), dto.getParentUuid());
    }

}

