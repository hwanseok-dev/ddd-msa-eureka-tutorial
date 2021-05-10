package hwanseok.server.study.entity;

import javax.persistence.*;

@Entity(name = "V2_LayerClosure")
public class LayerClosure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;
    @Column(name = "Parent")
    private Long parent;
    @Column(name = "Child")
    private Long child;
}
