package ru.bazhen.jooq.dto;
import java.util.ArrayList;
import java.util.List;

public class OrganizationTreeDTO {
    private Integer id;
    private String name;
    private Integer mainOrganizationId;
    private OrganizationTreeDTO parent;
    private List<OrganizationTreeDTO> children;

    public OrganizationTreeDTO() {
        super();
        this.children = new ArrayList<>();
    }

    public OrganizationTreeDTO(Integer id, String name, List<OrganizationTreeDTO> children) {
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

    public Integer getMainOrganizationId() {
        return mainOrganizationId;
    }

    public void setMainOrganizationId(Integer mainOrganizationId) {
        this.mainOrganizationId = mainOrganizationId;
    }

    public OrganizationTreeDTO getParent() {
        return parent;
    }

    public void setParent(OrganizationTreeDTO parent) {
        this.parent = parent;
    }

    public List<OrganizationTreeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<OrganizationTreeDTO> children) {
        this.children = children;
    }

    public void addChild(OrganizationTreeDTO organizationTreeDTO) {
        if (!this.children.contains(organizationTreeDTO) && organizationTreeDTO != null)
            this.children.add(organizationTreeDTO);
    }

    @Override
    public String toString() {
        return "Organization [id=" + id + ", parentId=" + mainOrganizationId + ", name=" + name + ", children="
                + children + "]";
    }
}
