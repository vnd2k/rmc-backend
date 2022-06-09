package rmc.backend.rmc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rmc.backend.rmc.entities.RMember;

@Repository
@Transactional(readOnly = true)
public interface MemberRepository extends JpaRepository<RMember, String> {
}
