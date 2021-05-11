package hwanseok.server.study.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hwanseok.server.study.util.UUIDUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("GROUP")
public class GroupLayer extends Layer {

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
    private DivisionLayer parent;

    public GroupLayer() {
    }

    public GroupLayer(String uuid) {
        super(uuid);
    }

    public static GroupLayer create(String name, String description) {
        GroupLayer group = new GroupLayer(UUIDUtil.generate());
        group.setName(name);
        group.setDescription(description);
        return group;
    }

    public DivisionLayer getParent() {
        return parent;
    }

    public void setParent(DivisionLayer parent) {
        this.parent = parent;
    }

//    public void setChild(List<ProjectLayer> child) {
//        this.child = child;
//    }
//
//    public void addChild(ProjectLayer child) {
//        if (this.child == null) {
//            this.child = new ArrayList<ProjectLayer>();
//        }
//        this.child.add(child);
//    }
}
