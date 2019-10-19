package ru.bazhen.jooq.dto;

public class OrganizationExtraDTO extends OrganizationDTO{
    private Integer workersCount;
    private String mainOrganizationName;

    public OrganizationExtraDTO(Integer id, String name, Integer mainOrganizationId,
                                String mainOrganizationName, Integer workersCount) {
        super(id, name, mainOrganizationId);
        this.mainOrganizationName = mainOrganizationName;
        this.workersCount = workersCount;
    }

    public String getMainOrganizationName() {
        return mainOrganizationName;
    }

    public void setMainOrganizationName(String mainOrganizationName) {
        this.mainOrganizationName = mainOrganizationName;
    }

    public Integer getWorkersCount() {
        return workersCount;
    }

    public void setWorkersCount(Integer workersCount) {
        this.workersCount = workersCount;
    }
}

