package ru.bazhen.jooq.dto;

public class OrganizationExtraDTO {
    private Integer id;
    private String name;
    private Integer mainOrganizationId;
    private String mainOrganizationName;
    private Integer workersCount;

    public OrganizationExtraDTO() {
    }

    public OrganizationExtraDTO(String name, Integer mainOrganizationId) {
        this.name = name;
        this.mainOrganizationId = mainOrganizationId;
    }

    public OrganizationExtraDTO(Integer id, String name, Integer mainOrganizationId,
                                String mainOrganizationName, Integer workersCount) {
        this.id = id;
        this.name = name;
        this.mainOrganizationId = mainOrganizationId;
        this.mainOrganizationName = mainOrganizationName;
        this.workersCount = workersCount;
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

    public Integer getMainOrganizationId() {
        return mainOrganizationId;
    }

    public void setMainOrganizationId(Integer mainOrganizationId) {
        this.mainOrganizationId = mainOrganizationId;
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

