package ru.bazhen.jooq.dto;
import java.util.ArrayList;
import java.util.List;

public class OrganizationTreeDTO extends OrganizationDTO{
    private OrganizationTreeDTO parent;
    private List<OrganizationTreeDTO> children;

    public OrganizationTreeDTO() {
        super();
        this.children = new ArrayList<>();
    }

    public OrganizationTreeDTO(Integer id, String name, List<OrganizationTreeDTO> children) {
        super(id,name);
        this.children = children;
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
        return "Organization [id=" + getId() + ", parentId=" + getMainOrganizationId()
                + ", name=" + getName() + ", children=" + children + "]";
    }
}
