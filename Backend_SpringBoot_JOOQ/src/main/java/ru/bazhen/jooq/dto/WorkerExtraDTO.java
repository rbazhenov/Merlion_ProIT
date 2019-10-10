package ru.bazhen.jooq.dto;

public class WorkerExtraDTO {
    private Integer id;
    private String  name;
    private Integer organizationId;
    private String organizationName;
    private Integer mainWorkerId;
    private String mainWorkerName;

    public WorkerExtraDTO() {
    }

    public WorkerExtraDTO(String name, Integer organizationId, Integer mainWorkerId) {
        this.name = name;
        this.organizationId = organizationId;
        this.mainWorkerId = mainWorkerId;
    }

    public WorkerExtraDTO(Integer id, String name, Integer organizationId,
                          String organizationName, Integer mainWorkerId, String mainWorkerName) {
        this.id = id;
        this.name = name;
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.mainWorkerId = mainWorkerId;
        this.mainWorkerName = mainWorkerName;
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

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Integer getMainWorkerId() {
        return mainWorkerId;
    }

    public void setMainWorkerId(Integer mainWorkerId) {
        this.mainWorkerId = mainWorkerId;
    }

    public String getMainWorkerName() {
        return mainWorkerName;
    }

    public void setMainWorkerName(String mainWorkerName) {
        this.mainWorkerName = mainWorkerName;
    }
}
