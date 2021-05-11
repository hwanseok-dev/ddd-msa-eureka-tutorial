package hwanseok.server.study.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hwanseok.server.study.util.UUIDUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("DIVISION")
public class DivisionLayer extends Layer {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable
            (name = "V2_LayerClosure",
                    joinColumns = @JoinColumn(
                            name = "Child",
                            referencedColumnName = "Id"
                    ),
                    inverseJoinColumns = @JoinColumn(
                            name = "Parent",
                            referencedColumnName = "Id"
                    ))
    @JsonIgnoreProperties(value = "child")
    private OrganizationLayer parent;

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
    @JsonIgnoreProperties(value = "parent")
    private List<GroupLayer> child;

    public DivisionLayer() {
    }

    public DivisionLayer(String uuid) {
        super(uuid);
    }

    public static DivisionLayer create(String name, String description) {
        DivisionLayer division = new DivisionLayer(UUIDUtil.generate());
        division.setName(name);
        division.setDescription(description);
        return division;
    }

    public OrganizationLayer getParent() {
        return parent;
    }

    public void setParent(OrganizationLayer parent) {
        this.parent = parent;
    }

    public List<GroupLayer> getChild() {
        return child;
    }

    public void setChild(List<GroupLayer> child) {
        this.child = child;
    }

    public void addChild(GroupLayer child) {
        if (this.child == null) {
            this.child = new ArrayList<GroupLayer>();
        }
        this.child.add(child);
    }
}
