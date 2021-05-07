package hwanseok.server.study.service;

import hwanseok.server.study.entity.OrganizationLayer;
import hwanseok.server.study.exception.OrganizationNotFoundException;
import hwanseok.server.study.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    public ResponseEntity<OrganizationLayer> findByUuid(String uuid){
        return organizationRepository.findByUuid(uuid)
                .map(org -> ResponseEntity
                .status(HttpStatus.OK)
                .body(org))
                .orElseThrow(OrganizationNotFoundException::new);
    }

    public ResponseEntity<OrganizationLayer> create(String name, String description){
        OrganizationLayer organizationLayer = organizationRepository
                .save(OrganizationLayer.create(name, description));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(organizationLayer);
    }

}