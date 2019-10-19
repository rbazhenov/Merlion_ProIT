package ru.bazhen.jooq.dto;

public class WorkerExtraDTO extends WorkerDTO{
    private String organizationName;
    private String mainWorkerName;

    public WorkerExtraDTO(Integer id, String name, Integer organizationId,Integer mainWorkerId,
                          String organizationName,  String mainWorkerName) {
        super(id, name, organizationId, mainWorkerId);
        this.organizationName = organizationName;
        this.mainWorkerName = mainWorkerName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getMainWorkerName() {
        return mainWorkerName;
    }

    public void setMainWorkerName(String mainWorkerName) {
        this.mainWorkerName = mainWorkerName;
    }
}
