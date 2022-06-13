package rmc.backend.rmc.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rmc.backend.rmc.entities.RCompany;
import rmc.backend.rmc.entities.Rating;
import rmc.backend.rmc.entities.dto.GetCompanyInfoResponse;
import rmc.backend.rmc.entities.dto.PutCompanyInfoRequest;
import rmc.backend.rmc.entities.dto.PutCompanyInfoResponse;
import rmc.backend.rmc.entities.dto.SearchCompanyResponse;
import rmc.backend.rmc.repositories.CompanyRepository;
import rmc.backend.rmc.repositories.RatingRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public PutCompanyInfoResponse updateCompanyInfo(String userId, PutCompanyInfoRequest request) {
        RCompany company = companyRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));

        company.setName(request.getName());
        company.setAddress(request.getAddress());
        company.setType(request.getType());
        company.setWebsite(request.getWebsite());
        company.setDescription(request.getDescription());
        company.setCompanySize(request.getCompanySize());
        company.setNation(request.getNation());
        company.setUpdatedAt(LocalDateTime.now());
        companyRepository.save(company);
        return new PutCompanyInfoResponse(company.getName(), company.getAddress(), company.getType(), company.getWebsite(), company.getDescription(), company.getCompanySize(), company.getNation(), company.getLogoImage());
    }

    @Transactional
    public PutCompanyInfoResponse updateCompanyLogo(String userId, String logoImage) {
        RCompany company = companyRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
        company.setLogoImage(logoImage);
        companyRepository.save(company);
        return new PutCompanyInfoResponse(company.getName(), company.getAddress(), company.getType(), company.getWebsite(), company.getDescription(), company.getCompanySize(), company.getNation(), company.getLogoImage());

    }

    public GetCompanyInfoResponse findById(String userId) {
        RCompany company = companyRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));

        List<Rating> ratings = ratingRepository.findAllByCompany(company);
        return new GetCompanyInfoResponse(
                company.getName(),
                company.getAddress(),
                company.getType(),
                company.getWebsite(),
                company.getDescription(),
                company.getCompanySize(),
                company.getNation(),
                company.getLogoImage(),
                company.getRatingScore(),
                company.getRatingCount(),
                ratings,
                company.getCreatedAt(),
                company.getUpdatedAt()
        );
    }

    public List<SearchCompanyResponse> searchByCharacter(String nameCharacter) {
        List<RCompany> listCompany = companyRepository.findAll();
        List<SearchCompanyResponse> searchResult = new ArrayList<>();
        for (RCompany company : listCompany) {
            if (company.getName().contains(nameCharacter)) {
                SearchCompanyResponse result = new SearchCompanyResponse(company.getId(), company.getName());
                searchResult.add(result);
            }
        }
        return searchResult;
    }
}
