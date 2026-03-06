package pl.koder95.bso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.koder95.bso.model.Role;
import pl.koder95.bso.model.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName roleName);
}
