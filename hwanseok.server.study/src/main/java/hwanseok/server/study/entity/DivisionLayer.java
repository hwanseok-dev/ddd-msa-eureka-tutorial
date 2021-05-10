package hwanseok.server.study.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hwanseok.server.study.util.UUIDUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("DIVISION")
public class DivisionLayer extends Layer {

//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JsonIgnoreProperties({"parent", "child"})
//    @Transient
//    private OrganizationLayer parent;

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

//    public OrganizationLayer getParent() {
//        return parent;
//    }
//
//    public void setParent(OrganizationLayer parent) {
//        this.parent = parent;
//    }

}
