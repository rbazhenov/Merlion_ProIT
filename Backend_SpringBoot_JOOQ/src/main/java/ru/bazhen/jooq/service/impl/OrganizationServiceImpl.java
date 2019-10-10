package ru.bazhen.jooq.service.impl;
import jooq.db.tables.Organization;
import jooq.db.tables.Worker;
import jooq.db.tables.records.OrganizationRecord;
import jooq.db.tables.records.WorkerRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bazhen.jooq.dto.OrganizationTreeDTO;
import ru.bazhen.jooq.dto.OrganizationExtraDTO;
import ru.bazhen.jooq.service.interfaces.OrganizationService;
import java.util.*;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private final DSLContext create;

    public OrganizationServiceImpl(DSLContext create) {
        this.create = create;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrganizationExtraDTO> findByOrganizationName(String searchName, Pageable pageable){
        String name =(searchName.equals("")) ? searchName : "(?i)^"+searchName+".*";
        Organization o = Organization.ORGANIZATION.as("o");
        Organization o2 = Organization.ORGANIZATION.as("o2");
        Worker w = Worker.WORKER.as("w");
        List<OrganizationExtraDTO> orgEntries = this.create
                .select(o.ID,o.NAME,o.MAIN_ORGANIZATION_ID, (this.create
                            .select(o2.NAME)
                            .from(o2)
                            .where(o.MAIN_ORGANIZATION_ID.eq(o2.ID)).asField("mainOrganizationName")),
                        DSL.count(w.ID).as("workersCount"))
                .from(o)
                .leftJoin(w)
                .on(w.ORGANIZATION_ID.eq(o.ID))
                .where(o.NAME.likeRegex(name))
                .and(o.ID.notEqual(0))
                .groupBy(o.ID)
                .orderBy(o.ID)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch()
                .into(OrganizationExtraDTO.class);
        long totalCount = findCountByLikeExpression(name);
        return new PageImpl<>(orgEntries, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<OrganizationTreeDTO> getOrganizationsTree() {
        Organization o = Organization.ORGANIZATION.as("o");

        List<OrganizationTreeDTO> orgs = this.create
                .select(o.NAME,o.ID,o.MAIN_ORGANIZATION_ID)
                .from(o)
                .orderBy(o.ID)
                .fetchInto(OrganizationTreeDTO.class);
        return createTree(orgs);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrganizationExtraDTO> getAllOrganizations(Pageable pageable){
        String name ="^.*";
        Organization o = Organization.ORGANIZATION.as("o");
        Organization o2 = Organization.ORGANIZATION.as("o2");
        Worker w = Worker.WORKER.as("w");

        List<OrganizationExtraDTO> orgEntries = this.create
                .select(o.ID,o.NAME,o.MAIN_ORGANIZATION_ID, (this.create
                                .select(o2.NAME)
                                .from(o2)
                                .where(o.MAIN_ORGANIZATION_ID.eq(o2.ID)).asField("mainOrganizationName")),
                        DSL.count(w.ID).as("workersCount"))
                .from(o)
                .leftJoin(w)
                .on(w.ORGANIZATION_ID.eq(o.ID))
                .where(o.ID.notEqual(0))
                .groupBy(o.ID)
                .orderBy(o.ID)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch()
                .into(OrganizationExtraDTO.class);
        long totalCount = findCountByLikeExpression(name);
        return new PageImpl<>(orgEntries, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    @Override
    public OrganizationExtraDTO getOrganization(int id) {
        Organization o = Organization.ORGANIZATION.as("o");
        Organization o2 = Organization.ORGANIZATION.as("o2");
        Worker w = Worker.WORKER.as("w");
        return this.create
                .select(o.ID,o.NAME, o.MAIN_ORGANIZATION_ID,(this.create
                                .select(o2.NAME)
                                .from(o2)
                                .where(o.MAIN_ORGANIZATION_ID.eq(o2.ID))).asField("mainOrganizationName"),
                        DSL.count(w.ID).as("workersCount"))
                .from(o)
                .leftJoin(w)
                .on(w.ORGANIZATION_ID.eq(o.ID))
                .where(o.ID.eq(id))
                .groupBy(o.ID)
                .orderBy(o.ID)
                .fetchOne()
                .into(OrganizationExtraDTO.class);
    }

    @Override
    @Transactional
    public void createOrganization(OrganizationExtraDTO organizationExtraDTO) {
        Organization o = Organization.ORGANIZATION.as("o");
            this.create
                    .insertInto(o)
                    .set(o.NAME, organizationExtraDTO.getName())
                    .set(o.MAIN_ORGANIZATION_ID, organizationExtraDTO.getMainOrganizationId())
                    .execute();
    }

    @Override
    @Transactional
    public void updateOrganization(int id, OrganizationExtraDTO organizationExtraDTO) {
        Organization o = Organization.ORGANIZATION.as("o");
        Optional<OrganizationRecord> organizationId = Optional.ofNullable(this
                .create.fetchOne(o, o.ID.eq(id)));

        if (organizationId.isPresent()) {
            this.create
                    .update(o)
                    .set(o.NAME, organizationExtraDTO.getName())
                    .set(o.MAIN_ORGANIZATION_ID, organizationExtraDTO.getMainOrganizationId())
                    .where(o.ID.eq(id))
                    .execute();
        } else
            System.out.println("Organization not found");
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteOrganization(int id) {
        Optional<OrganizationRecord> organization = Optional.ofNullable(this
                .create.fetchOne(Organization.ORGANIZATION,Organization.ORGANIZATION.ID.eq(id)));
        Optional<OrganizationRecord> childOrganization = Optional.ofNullable(this
                .create.fetchAny(Organization.ORGANIZATION, Organization.ORGANIZATION.MAIN_ORGANIZATION_ID.eq(id)));
        Optional<WorkerRecord> workerInOrganization = Optional.ofNullable((this
                .create.fetchAny(Worker.WORKER,Worker.WORKER.ORGANIZATION_ID.eq(id))));

            if (!childOrganization.isPresent()&&!workerInOrganization.isPresent()){
                organization.get().delete();
                System.out.println("Org Deleted");
                return ResponseEntity.ok().build();
            } else
                System.out.println("Organization have got a child");
                return ResponseEntity.notFound().build();
    }

    private long findCountByLikeExpression(String name) {
        return create.fetchCount(create.select()
                .from(Organization.ORGANIZATION)
                .where(Organization.ORGANIZATION.NAME.likeRegex(name))
                .and(Organization.ORGANIZATION.ID.notEqual(0))
        );
    }
    private static List<OrganizationTreeDTO> createTree(List<OrganizationTreeDTO> nodes) {
        Map<Integer, OrganizationTreeDTO> mapTmp = new HashMap<>();
        for (OrganizationTreeDTO current : nodes) {
            mapTmp.put(current.getId(), current);
        }
        for (OrganizationTreeDTO current : nodes) {
            Integer parentId = current.getMainOrganizationId();
            if (parentId != null) {
                OrganizationTreeDTO parent = mapTmp.get(parentId);
                if (parent != null) {
                    current.setParent(parent);
                    parent.addChild(current);
                    mapTmp.put(parentId, parent);
                    mapTmp.put(current.getId(), current);
                }
            }
        }
        OrganizationTreeDTO root = null;
        for (OrganizationTreeDTO organizationTreeDTO : mapTmp.values()) {
            if(organizationTreeDTO.getParent() == null) {
                root = organizationTreeDTO;
                break;
            }
        }
        List<OrganizationTreeDTO> result = flatten(root, new ArrayList<>());
        return result;
    }
    private static List<OrganizationTreeDTO> flatten(OrganizationTreeDTO orgTreeDTO,  List<OrganizationTreeDTO> flatList) {
        if(flatList.size()<1){
            OrganizationTreeDTO n = new OrganizationTreeDTO(orgTreeDTO.getId(),orgTreeDTO.getName(), orgTreeDTO.getChildren()); // get rid of children & parent references
            flatList.add(n);
        }
        List<OrganizationTreeDTO> children = orgTreeDTO.getChildren();
        for (OrganizationTreeDTO child : children) {
            child.setParent(null);
            if(child.getChildren() != null) {
                flatten(child, flatList);
            }
        }
        return flatList;
    }
}
