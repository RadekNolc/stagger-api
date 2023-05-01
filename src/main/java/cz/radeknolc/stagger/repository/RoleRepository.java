package cz.radeknolc.stagger.repository;

import cz.radeknolc.stagger.model.Role;
import cz.radeknolc.stagger.model.RoleName;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}
