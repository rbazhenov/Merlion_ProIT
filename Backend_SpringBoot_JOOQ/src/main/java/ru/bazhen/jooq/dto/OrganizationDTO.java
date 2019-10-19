package ru.bazhen.jooq.dto;

public class OrganizationDTO {
    private Integer id;
    private String name;
    private Integer mainOrganizationId;


    public OrganizationDTO() {
    }

    public OrganizationDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public OrganizationDTO(Integer id, String name, Integer mainOrganizationId) {
        this.id = id;
        this.name = name;
        this.mainOrganizationId = mainOrganizationId;
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
}
