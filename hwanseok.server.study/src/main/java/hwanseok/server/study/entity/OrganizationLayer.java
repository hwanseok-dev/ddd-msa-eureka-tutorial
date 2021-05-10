package hwanseok.server.study.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hwanseok.server.study.util.UUIDUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("ORGANIZATION")
public class OrganizationLayer extends Layer {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable
            (name = "V2_LayerClosure",
            joinColumns = @JoinColumn(
                    name = "Parent",
                    referencedColumnName = "Id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "Child",
                    referencedColumnName = "Id"
            ))
    private List<DivisionLayer> child;

    public OrganizationLayer(){
    }

    public OrganizationLayer(String uuid) {
        super(uuid);
    }

    public static OrganizationLayer create(String name, String description) {
        OrganizationLayer organization = new OrganizationLayer(UUIDUtil.generate());
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
