package hwanseok.server.study.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hwanseok.server.study.util.UUIDUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("PROJECT")
public class ProjectLayer extends Layer {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "V2_LayerClosure",
            joinColumns = @JoinColumn(name = "Child"),
            inverseJoinColumns = @JoinColumn(name = "Parent"))
    @JsonIgnoreProperties("child")
    private GroupLayer parent;

    public ProjectLayer() {
    }

    public ProjectLayer(String uuid) {
        super(uuid);
    }

    public static ProjectLayer create(String name, String description) {
        ProjectLayer project = new ProjectLayer(UUIDUtil.generate());
        project.setName(name);
        project.setDescription(description);
        return project;
    }

    public GroupLayer getParent() {
        return parent;
    }

    public void setParent(GroupLayer parent) {
        this.parent = parent;
    }
}
