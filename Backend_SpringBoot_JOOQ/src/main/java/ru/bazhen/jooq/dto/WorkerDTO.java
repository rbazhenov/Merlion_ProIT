package ru.bazhen.jooq.dto;

public class WorkerDTO {
    private Integer id;
    private String  name;
    private Integer organizationId;
    private Integer mainWorkerId;

    public WorkerDTO() {
    }

    public WorkerDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public WorkerDTO(Integer id, String name, Integer organizationId, Integer mainWorkerId) {
        this.id = id;
        this.name = name;
        this.organizationId = organizationId;
        this.mainWorkerId = mainWorkerId;
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

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getMainWorkerId() {
        return mainWorkerId;
    }

    public void setMainWorkerId(Integer mainWorkerId) {
        this.mainWorkerId = mainWorkerId;
    }
}
