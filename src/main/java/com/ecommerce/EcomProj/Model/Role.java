package com.ecommerce.EcomProj.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "role_id")
    private Integer roleId;

    @ToString.Exclude
    @Column(length = 25, name = "role_name")
    @Enumerated(EnumType.STRING)
    private AppRoles roleName;

    public Role(AppRoles roleName) {
        this.roleName = roleName;
    }
}
