package vn.com.greencraze.auth.entity.view;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.UuidGenerator;
import vn.com.greencraze.auth.entity.Identity;

@Entity
@Table(name = "user_profile_view")
@Immutable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileView {
    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "identity_id", referencedColumnName = "id")
    private Identity identity;
}
