package hwanseok.server.study.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hwanseok.server.study.util.UUIDUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("GROUP")
public class GroupLayer extends Layer {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "V2_LayerClosure",
            joinColumns = @JoinColumn(name = "Child"),
            inverseJoinColumns = @JoinColumn(name = "Parent"))
    @JsonIgnoreProperties({"parent", "child"})
    @Transient
    private DivisionLayer parent;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "V2_LayerClosure",
            joinColumns = @JoinColumn(name = "Parent"),
            inverseJoinColumns = @JoinColumn(name = "Child"))
    @JsonIgnoreProperties({"parent", "child"})
    @Transient
    private List<ProjectLayer> child;

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

    public void setChild(List<ProjectLayer> child) {
        this.child = child;
    }

    public void addChild(ProjectLayer child) {
        if (this.child == null) {
            this.child = new ArrayList<ProjectLayer>();
        }
        this.child.add(child);
    }
}
