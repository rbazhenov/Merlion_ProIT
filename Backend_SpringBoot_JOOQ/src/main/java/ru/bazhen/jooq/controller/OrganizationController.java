package ru.bazhen.jooq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bazhen.jooq.dto.OrganizationDTO;
import ru.bazhen.jooq.dto.OrganizationTreeDTO;
import ru.bazhen.jooq.dto.OrganizationExtraDTO;
import ru.bazhen.jooq.service.interfaces.OrganizationService;

import java.util.Collection;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;


    @RequestMapping(value = "/organizations/search", method = RequestMethod.GET)
    public Page<OrganizationDTO> findBySearchTerm(@RequestParam("name") String searchName,
                                                  Pageable pageable) {
        return organizationService.findByOrganizationName(searchName, pageable);
    }

    @GetMapping(value = "organizations/tree")
    public Collection<OrganizationDTO> getOrganizationsTree(){
        return organizationService.getOrganizationsTree();
    }

    @GetMapping(value = "/organizations")
    public Page<OrganizationDTO> getAllOrganizations(Pageable pageable) {
        return organizationService.getAllOrganizations(pageable);
    }

    @GetMapping(value = "/organizations/id/{id}")
    public OrganizationDTO getOrganization(@PathVariable int id) {
        return organizationService.getOrganization(id);
    }
    @PostMapping(value = "/organizations")
    public void addOrganization(@RequestBody OrganizationDTO organizationExtraDTO){
        organizationService.createOrganization(organizationExtraDTO);
    }

    @PutMapping (value = "/organizations/id/{id}")
    public void updateOrganization(@RequestBody OrganizationDTO organizationExtraDTO, @PathVariable int id){
        organizationService.updateOrganization(id, organizationExtraDTO);
    }

    @DeleteMapping(value = "/organizations/id/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable int id){
        return organizationService.deleteOrganization(id);
    }

}