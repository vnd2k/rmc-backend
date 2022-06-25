package rmc.backend.rmc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmc.backend.rmc.entities.RCompany;
import rmc.backend.rmc.entities.RMember;
import rmc.backend.rmc.entities.SavedCompany;
import rmc.backend.rmc.entities.dto.GetSavedResponse;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedCompanyRepository extends JpaRepository<SavedCompany, String> {
    SavedCompany findByMemberAndCompany(RMember member, RCompany company);

    List<SavedCompany> findAllByMember(RMember member);
}
