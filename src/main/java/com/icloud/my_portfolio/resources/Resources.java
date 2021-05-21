package com.icloud.my_portfolio.resources;

import com.icloud.my_portfolio.role.Role;
import lombok.*;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "roleList")
public class Resources implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "resource_id")
    private Long id;

    private String resourceName;

    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    private int orderNum;

    @Enumerated(EnumType.STRING)
    private HttpMethod httpMethod;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "resource_role",
            joinColumns = @JoinColumn(name = "resource_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roleList = new ArrayList<>();
}
