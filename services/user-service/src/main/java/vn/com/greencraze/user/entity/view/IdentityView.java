package vn.com.greencraze.user.entity.view;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.UuidGenerator;
import vn.com.greencraze.user.enumeration.IdentityStatus;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "identity_view")
@Immutable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdentityView {
    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private IdentityStatus status;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "identity_role_view",
            joinColumns = @JoinColumn(name = "identity_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false)
    )
    @Builder.Default
    private Set<RoleView> roles = new HashSet<>();

}
