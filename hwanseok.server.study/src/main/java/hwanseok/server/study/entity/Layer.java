package hwanseok.server.study.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hwanseok.server.study.util.UUIDUtil;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "V2_Layer")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "LType")
@EntityListeners(AuditingEntityListener.class)
public abstract class Layer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    @JsonIgnore
    private Long id;

    @CreatedDate
    @Column(name = "CreateTime", updatable = false)
    private Date createTime;

    @LastModifiedDate
    @Column(name = "UpdateTime")
    private Date updateTime;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Description", nullable = true)
    private String description;

    @Column(name = "UUID", nullable = false, updatable = false, unique = true)
    private String uuid;

    @Column(name = "UHash", nullable = false)
    private Long uhash;

//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinTable(name = "V2_LayerClosure",
//            joinColumns = @JoinColumn(name = "Child"),
//            inverseJoinColumns = @JoinColumn(name = "Parent"))
//    private Layer parent;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinTable(name = "V2_LayerClosure",
//            joinColumns = @JoinColumn(name = "Parent"),
//            inverseJoinColumns = @JoinColumn(name = "Child"))
//    private List<Layer> child;

    public Layer() {
    }

    public Layer(String uuid) {
        this.uuid = uuid;
        this.uhash = UUIDUtil.toLong(uuid);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getUhash() {
        return uhash;
    }

    public void setUhash(Long uhash) {
        this.uhash = uhash;
    }

//    public Layer getParent() {
//        return parent;
//    }
//
//    public void setParent(Layer parent) {
//        this.parent = parent;
//    }

//    public List<Layer> getChild() {
//        return child;
//    }
//
//    public void setChild(List<Layer> child) {
//        this.child = child;
//    }
//
//    public void addChild(Layer child) {
//        if (this.child == null) {
//            this.child = new ArrayList<Layer>();
//        }
//        this.child.add(child);
//    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

