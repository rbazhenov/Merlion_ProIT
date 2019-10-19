package ru.bazhen.jooq.service.impl;

import jooq.db.tables.Organization;
import jooq.db.tables.Worker;
import jooq.db.tables.records.OrganizationRecord;
import jooq.db.tables.records.WorkerRecord;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bazhen.jooq.dto.WorkerExtraDTO;
import ru.bazhen.jooq.dto.WorkerTreeDTO;
import ru.bazhen.jooq.service.interfaces.WorkerService;
import java.util.*;

@Service
public class WorkerServiceImpl implements WorkerService {

    @Autowired
    private final DSLContext create;

    public WorkerServiceImpl(DSLContext create) {
        this.create = create;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<WorkerExtraDTO> findByWorkerOrOrganizationName(String workerName, String orgName, Pageable pageable) {
        workerName =(!workerName.equals("undefined")) ? "(?i)^"+workerName+".*": "";
        orgName =(!orgName.equals("undefined")) ? "(?i)^"+orgName+".*": "";
        Organization o = Organization.ORGANIZATION.as("o");
        Worker w = Worker.WORKER.as("w");
        Worker w2 = Worker.WORKER.as("w2");

        List<WorkerExtraDTO> orgEntries = this.create
                .select(w.ID, w.NAME, w.ORGANIZATION_ID, w.MAIN_WORKER_ID, o.NAME.as("organizationName"), (create
                        .select(w2.NAME)
                        .from(w2)
                        .where(w.MAIN_WORKER_ID.eq(w2.ID))).asField("mainWorkerName"))
                .from(w)
                .join(o)
                .on(o.ID.eq(w.ORGANIZATION_ID))
                .where(w.NAME.likeRegex(workerName))
                .and(o.NAME.likeRegex(orgName))
                .and(o.ID.notEqual(0))
                .orderBy(w.ID)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch()
                .into(WorkerExtraDTO.class);
        long totalCount = findCountByLikeExpression(workerName,orgName);
        return new PageImpl<>(orgEntries, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<WorkerTreeDTO> getWorkersTree() {
        Worker w = Worker.WORKER.as("w");
        Worker w2 = Worker.WORKER.as("w2");

        List<WorkerTreeDTO> workers = this.create
                .select(w.NAME,w.ID,w.MAIN_WORKER_ID)
                .from(w)
                .orderBy(w.ID)
                .fetchInto(WorkerTreeDTO.class);
        return createTree(workers);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<WorkerExtraDTO> getAllWorkers(Pageable pageable) {
        String workerName ="^.*";
        String orgName ="^.*";
        Organization o = Organization.ORGANIZATION.as("o");
        Worker w = Worker.WORKER.as("w");
        Worker w2 = Worker.WORKER.as("w2");
        List<WorkerExtraDTO> orgEntries = this.create
                .select(w.ID, w.NAME, w.ORGANIZATION_ID,w.MAIN_WORKER_ID, o.NAME.as("organizationName"), (create
                        .select(w2.NAME)
                        .from(w2)
                        .where(w.MAIN_WORKER_ID.eq(w2.ID))).asField("mainWorkerName"))
                .from(w)
                .join(o)
                .on(o.ID.eq(w.ORGANIZATION_ID))
                .where(o.ID.notEqual(0))
                .orderBy(w.ID)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch()
                .into(WorkerExtraDTO.class);
        long totalCount = findCountByLikeExpression(workerName,orgName);
        return new PageImpl<>(orgEntries, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    @Override
    public WorkerExtraDTO getWorker(int id) {
        Organization o = Organization.ORGANIZATION.as("o");
        Worker w = Worker.WORKER.as("w");
        Worker w2 = Worker.WORKER.as("w2");
        return this.create
                .select(w.ID, w.NAME, w.ORGANIZATION_ID, w.MAIN_WORKER_ID, o.NAME.as("organizationName"),(create
                        .select(w2.NAME)
                        .from(w2)
                        .where(w.MAIN_WORKER_ID.eq(w2.ID))).asField("mainWorkerName"))
                .from(w)
                .join(o)
                .on(o.ID.eq(w.ORGANIZATION_ID))
                .where(w.ID.eq(id))
                .fetchOne()
                .into(WorkerExtraDTO.class);
    }

    @Override
    @Transactional
    public void createWorker(WorkerExtraDTO workerExtraDTO) {
        Worker w = Worker.WORKER.as("w");
        Organization o = Organization.ORGANIZATION.as("o");
        Optional<OrganizationRecord> organization = Optional.ofNullable(this
                .create.fetchOne(o, o.ID.eq(workerExtraDTO.getOrganizationId())));
        if(organization.isPresent()) {
            this.create
                    .insertInto(w)
                    .set(w.NAME, workerExtraDTO.getName())
                    .set(w.MAIN_WORKER_ID, workerExtraDTO.getMainWorkerId())
                    .set(w.ORGANIZATION_ID, workerExtraDTO.getOrganizationId())
                    .execute();
        }
        else
            System.out.println("Organization worker's not found");
    }

    @Override
    @Transactional
    public ResponseEntity<Void> updateWorker(WorkerExtraDTO workerExtraDTO, int id) {
        Worker w = Worker.WORKER.as("w");
        Organization o = Organization.ORGANIZATION.as("o");
        Optional<WorkerRecord> ChildWorker = Optional.ofNullable(this
                .create.fetchAny(w,w.MAIN_WORKER_ID.eq(id)));
        WorkerExtraDTO oldWorker = this
                .create.fetchOne(w, w.ID.eq(id)).into(WorkerExtraDTO.class);

        if (ChildWorker.isPresent() && !workerExtraDTO.getOrganizationId().equals(oldWorker.getOrganizationId())){
            return ResponseEntity.notFound().build();
        }
        else
            this.create
                    .update(w)
                    .set(w.NAME, workerExtraDTO.getName())
                    .set(w.MAIN_WORKER_ID, workerExtraDTO.getMainWorkerId())
                    .set(w.ORGANIZATION_ID, workerExtraDTO.getOrganizationId())
                    .where(w.ID.eq(id))
                    .execute();
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteWorker(int id) {
        Worker w = Worker.WORKER.as("w");
        Optional<WorkerRecord> worker = Optional.ofNullable(this
                .create.fetchOne(w, w.ID.eq(id)));
        Optional<WorkerRecord> ChildWorker = Optional.ofNullable(this
                .create.fetchAny(w,w.MAIN_WORKER_ID.eq(id)));

        if(!ChildWorker.isPresent()){
            worker.get().delete();
            System.out.println("Deleted");
            return ResponseEntity.ok().build();
        } else
            System.out.println("Worker have got a child");
            return ResponseEntity.notFound().build();
    }

    private long findCountByLikeExpression(String name, String org) {
        return create.fetchCount(create.select()
                .from(Worker.WORKER)
                .join(Organization.ORGANIZATION)
                .on(Organization.ORGANIZATION.ID.eq(Worker.WORKER.ORGANIZATION_ID))
                .where(Worker.WORKER.NAME.likeRegex(name))
                .and(Organization.ORGANIZATION.NAME.likeRegex(org))
                .and(Organization.ORGANIZATION.ID.notEqual(0))
        );
    }
    private static List<WorkerTreeDTO> createTree(List<WorkerTreeDTO> nodes) {
        Map<Integer, WorkerTreeDTO> mapTmp = new HashMap<>();
        for (WorkerTreeDTO current : nodes) {
            mapTmp.put(current.getId(), current);
        }
        for (WorkerTreeDTO current : nodes) {
            Integer parentId = current.getMainWorkerId();
            if (parentId != null) {
                WorkerTreeDTO parent = mapTmp.get(parentId);
                if (parent != null) {
                    current.setParent(parent);
                    parent.addChild(current);
                    mapTmp.put(parentId, parent);
                    mapTmp.put(current.getId(), current);
                }
            }
        }
        WorkerTreeDTO root = null;
        for (WorkerTreeDTO workerTreeDTO : mapTmp.values()) {
            if(workerTreeDTO.getParent() == null) {
                root = workerTreeDTO;
                break;
            }
        }
        List<WorkerTreeDTO> result = flatten(root, new ArrayList<>());
        return result;
    }
    private static List<WorkerTreeDTO> flatten(WorkerTreeDTO workerTreeDTO,  List<WorkerTreeDTO> flatList) {
        if(flatList.size()<1){
            WorkerTreeDTO n = new WorkerTreeDTO(workerTreeDTO.getId(),workerTreeDTO.getName(), workerTreeDTO.getChildren()); // get rid of children & parent references
            flatList.add(n);
        }
        List<WorkerTreeDTO> children = workerTreeDTO.getChildren();
        for (WorkerTreeDTO child : children) {
            child.setParent(null);
            if(child.getChildren() != null) {
                flatten(child, flatList);
            }
        }
        return flatList;
    }
}