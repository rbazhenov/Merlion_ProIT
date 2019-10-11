package ru.bazhen.jooq.service.interfaces;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import ru.bazhen.jooq.dto.WorkerExtraDTO;
import ru.bazhen.jooq.dto.WorkerTreeDTO;

import java.util.Collection;

public interface WorkerService {
    @Transactional(readOnly = true)
    Page<WorkerExtraDTO> findByWorkerOrOrganizationName (String workerName, String orgName, Pageable pageable);
    @Transactional(readOnly = true)
    Page<WorkerExtraDTO> getAllWorkers(Pageable pageable);
    @Transactional(readOnly = true)
    WorkerExtraDTO getWorker(int id);
    @Transactional(readOnly = true)
    Collection<WorkerTreeDTO> getWorkersTree();
    @Transactional
    void createWorker(WorkerExtraDTO workerTreeDTO);
    @Transactional
    ResponseEntity<Void> updateWorker(WorkerExtraDTO workerTreeDTO, int id);
    @Transactional
    ResponseEntity<Void> deleteWorker(int id);
}
