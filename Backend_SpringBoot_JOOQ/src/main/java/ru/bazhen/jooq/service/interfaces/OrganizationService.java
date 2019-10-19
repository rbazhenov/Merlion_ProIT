package ru.bazhen.jooq.service.interfaces;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import ru.bazhen.jooq.dto.OrganizationDTO;
import ru.bazhen.jooq.dto.OrganizationTreeDTO;
import ru.bazhen.jooq.dto.OrganizationExtraDTO;

import java.util.Collection;

public interface OrganizationService {
    @Transactional(readOnly = true)
    Page<OrganizationDTO> findByOrganizationName(String searchName, Pageable pageable);
    @Transactional(readOnly = true)
    Collection<OrganizationDTO> getOrganizationsTree();
    @Transactional(readOnly = true)
    Page <OrganizationDTO> getAllOrganizations(Pageable pageable);
    @Transactional(readOnly = true)
    OrganizationDTO getOrganization(int id);

    @Transactional
    void createOrganization(OrganizationDTO organizationTreeDTO);
    @Transactional
    void updateOrganization(int id, OrganizationDTO organizationTreeDTO);
    @Transactional
    ResponseEntity<Void> deleteOrganization(int id);

}
