package rmc.backend.rmc.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rmc.backend.rmc.entities.RCompany;
import rmc.backend.rmc.entities.Rating;
import rmc.backend.rmc.entities.dto.GetCompanyInfoResponse;
import rmc.backend.rmc.entities.dto.PutCompanyInfoRequest;
import rmc.backend.rmc.repositories.CompanyRepository;
import rmc.backend.rmc.repositories.RatingRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    private final RatingRepository ratingRepository;

    public CompanyService(CompanyRepository companyRepository, RatingRepository ratingRepository) {
        this.companyRepository = companyRepository;
        this.ratingRepository = ratingRepository;
    }

    @Transactional
    public void updateCompanyInfo(String userId, PutCompanyInfoRequest request) {
        RCompany company = companyRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));

        company.setAddress(request.getAddress());
        company.setType(request.getType());
        company.setWebsite(request.getWebsite());
        company.setDescription(request.getDescription());
        company.setLogoImage(request.getLogoImage());
        company.setUpdatedAt(LocalDateTime.now());
        companyRepository.save(company);
    }

    public GetCompanyInfoResponse findById(String userId) {
        RCompany company = companyRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));

        List<Rating> ratings = ratingRepository.findAllByCompany(company);

        return new GetCompanyInfoResponse(
                "Mock Name",
                company.getAddress(),
                company.getDescription(),
                company.getWebsite(),
                company.getType(),
                company.getLogoImage(),
                company.getRatingScore(),
                company.getRatingCount(),
                ratings,
                company.getCreatedAt(),
                company.getUpdatedAt()
        );
    }
}
