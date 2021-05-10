package hwanseok.server.study.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hwanseok.server.study.util.UUIDUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("ORGANIZATION_COPY")
public class OrganizationLayerCopy extends Layer {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "V2_LayerClosure",
            joinColumns = @JoinColumn(name = "Parent"),
            inverseJoinColumns = @JoinColumn(name = "Child"))
    @JsonIgnoreProperties("parent")
    private List<DivisionLayer> child;

    public OrganizationLayerCopy(){
    }

    public OrganizationLayerCopy(String uuid) {
        super(uuid);
    }

    public static OrganizationLayerCopy create(String name, String description) {
        OrganizationLayerCopy organization = new OrganizationLayerCopy(UUIDUtil.generate());
        organization.setName(name);
        organization.setDescription(description);
        return organization;
    }

    public List<DivisionLayer> getChild() {
        return child;
    }

    public void setChild(List<DivisionLayer> child) {
        this.child = child;
    }

    public void addChild(DivisionLayer child) {
        if (this.child == null) {
            this.child = new ArrayList<DivisionLayer>();
        }
        this.child.add(child);
    }
}
