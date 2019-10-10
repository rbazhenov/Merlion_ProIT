package ru.bazhen.jooq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bazhen.jooq.dto.WorkerExtraDTO;
import ru.bazhen.jooq.dto.WorkerTreeDTO;
import ru.bazhen.jooq.service.interfaces.WorkerService;

import java.util.Collection;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class WorkerController {
    @Autowired
    private WorkerService workerService;

    @RequestMapping(value = "/workers/search", method = RequestMethod.GET)
    public Page<WorkerExtraDTO> findByWorkerOrOrganizationName(@RequestParam("name") String name,
                               @RequestParam("org") String orgName, Pageable pageable) {
        return workerService.findByWorkerOrOrganizationName(name, orgName, pageable);
    }

    @GetMapping("/workers")
    public Page<WorkerExtraDTO> getAllWorkers(Pageable pageable){
        return workerService.getAllWorkers(pageable);
    }

    @GetMapping("/workers/id/{id}")
    public WorkerExtraDTO getWorker(@PathVariable int id){
        return workerService.getWorker(id);
    }

    @GetMapping(value = "workers/tree")
    public Collection<WorkerTreeDTO> getWorkersTree(){
        return workerService.getWorkersTree();
    }

    @PostMapping("/workers")
    public void addWorker(@RequestBody WorkerExtraDTO workerExtraDTO){
        workerService.createWorker(workerExtraDTO);
    }

    @PutMapping ("/workers/id/{id}")
    public void updateWorker(@RequestBody WorkerExtraDTO workerExtraDTO, @PathVariable int id) {
        workerService.updateWorker(workerExtraDTO,id);
    }

    @DeleteMapping("/workers/id/{id}")
    public ResponseEntity<Void> deleteWorker(@PathVariable int id) {
        return workerService.deleteWorker(id);
    }
}
