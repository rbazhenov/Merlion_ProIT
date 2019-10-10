package ru.bazhen.jooq.dto;
import java.util.ArrayList;
import java.util.List;

public class WorkerTreeDTO {
    private Integer id;
    private String  name;
    private Integer mainWorkerId;
    private WorkerTreeDTO parent;
    private List<WorkerTreeDTO> children;

    public WorkerTreeDTO() {
        super();
        this.children = new ArrayList<>();
    }

    public WorkerTreeDTO(Integer id, String name, List<WorkerTreeDTO> children) {
        this.id = id;
        this.name = name;
        this.children = children;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMainWorkerId() {
        return mainWorkerId;
    }

    public void setMainWorkerId(Integer mainWorkerId) {
        this.mainWorkerId = mainWorkerId;
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
        return "Organization [id=" + id + ", parentId=" + mainWorkerId + ", name=" + name + ", children="
                + children + "]";
    }
}
