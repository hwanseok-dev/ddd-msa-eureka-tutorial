package hwanseok.server.study.service;

import hwanseok.server.study.entity.DivisionLayer;
import hwanseok.server.study.entity.OrganizationLayer;
import hwanseok.server.study.exception.OrganizationNotFoundException;
import hwanseok.server.study.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    public ResponseEntity<OrganizationLayer> create(String name, String description){
        OrganizationLayer me = organizationRepository
                .save(OrganizationLayer.create(name, description));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(me);
    }

    public ResponseEntity<OrganizationLayer> findByUuid(String uuid){
        return organizationRepository.findByUuid(uuid)
                .map(org -> ResponseEntity
                .status(HttpStatus.OK)
                .body(org))
                .orElseThrow(OrganizationNotFoundException::new);
    }



}