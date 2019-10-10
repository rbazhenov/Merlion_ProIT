package ru.bazhen.jooq.service.interfaces;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import ru.bazhen.jooq.dto.OrganizationTreeDTO;
import ru.bazhen.jooq.dto.OrganizationExtraDTO;

import java.util.Collection;

public interface OrganizationService {
    @Transactional(readOnly = true)
    Page<OrganizationExtraDTO> findByOrganizationName(String searchName, Pageable pageable);
    @Transactional(readOnly = true)
    Collection<OrganizationTreeDTO> getOrganizationsTree();
    @Transactional(readOnly = true)
    Page <OrganizationExtraDTO> getAllOrganizations(Pageable pageable);
    @Transactional(readOnly = true)
    OrganizationExtraDTO getOrganization(int id);

    @Transactional
    void createOrganization(OrganizationExtraDTO organizationTreeDTO);
    @Transactional
    void updateOrganization(int id, OrganizationExtraDTO organizationTreeDTO);
    @Transactional
    ResponseEntity<Void> deleteOrganization(int id);

}
