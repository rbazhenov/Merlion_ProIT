package ru.bazhen.jooq.dto;
import java.util.ArrayList;
import java.util.List;

public class WorkerTreeDTO extends WorkerDTO{
    private WorkerTreeDTO parent;
    private List<WorkerTreeDTO> children;

    public WorkerTreeDTO() {
        super();
        this.children = new ArrayList<>();
    }

    public WorkerTreeDTO(Integer id, String name, List<WorkerTreeDTO> children) {
        super(id, name);
        this.children = children;
    }

    public WorkerTreeDTO getParent() {
        return parent;
    }

    public void setParent(WorkerTreeDTO parent) {
        this.parent = parent;
    }

    public List<WorkerTreeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<WorkerTreeDTO> children) {
        this.children = children;
    }

    public void addChild(WorkerTreeDTO workerTreeDTO) {
        if (!this.children.contains(workerTreeDTO) && workerTreeDTO != null)
            this.children.add(workerTreeDTO);
    }

    @Override
    public String toString() {
        return "Organization [id=" + getId() + ", parentId=" + getMainWorkerId()
                + ", name=" + getName() + ", children=" + children + "]";
    }
}
