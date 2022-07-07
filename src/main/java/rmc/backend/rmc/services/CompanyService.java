package rmc.backend.rmc.services;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rmc.backend.rmc.entities.*;
import rmc.backend.rmc.entities.dto.*;
import rmc.backend.rmc.repositories.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {
    private final RUserRepository userRepository;
    private final CompanyRepository companyRepository;

    private final RatingRepository ratingRepository;

    private final JobRepository jobRepository;

    private final CvRepository cvRepository;

    public CompanyService(RUserRepository userRepository, CompanyRepository companyRepository, RatingRepository ratingRepository, JobRepository jobRepository, CvRepository cvRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.ratingRepository = ratingRepository;
        this.jobRepository = jobRepository;
        this.cvRepository = cvRepository;
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

    @Transactional
    public GetCompanyInfoResponse findById(String userId) {
        RCompany company = companyRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));

        List<Rating> ratings = ratingRepository.findAllByCompanyOrderByCreatedAtDesc(company);
        int oneStar = 0;
        int twoStar = 0;
        int threeStar = 0;
        int fourStar = 0;
        int fiveStar = 0;
        if (ratings.size() != 0) {
            for (Rating rating : ratings) {
                if (rating.getRatingPoint() == 1) {
                    oneStar += 1;
                } else if (rating.getRatingPoint() == 2) {
                    twoStar += 1;
                } else if (rating.getRatingPoint() == 3) {
                    threeStar += 1;
                } else if (rating.getRatingPoint() == 4) {
                    fourStar += 1;
                } else if (rating.getRatingPoint() == 5) {
                    fiveStar += 1;
                }
            }
        }
        return new GetCompanyInfoResponse(
                company.getId(),
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
                oneStar,
                twoStar,
                threeStar,
                fourStar,
                fiveStar,
                company.isVerified(),
                company.getCreatedAt(),
                company.getUpdatedAt()
        );
    }

    public List<SearchCompanyResponse> searchByCharacter(String nameCharacter) {
        List<RCompany> listCompany = companyRepository.findAll();
        List<SearchCompanyResponse> searchResult = new ArrayList<>();
        for (RCompany company : listCompany) {
            if (!company.getName().equals("") && company.isVerified()) {
                if (company.getName().toLowerCase().contains(nameCharacter.toLowerCase())) {
                    SearchCompanyResponse result = new SearchCompanyResponse(company.getId(), company.getName());
                    searchResult.add(result);
                }
            }
        }
        return searchResult;
    }

    @Transactional
    public void addJob(String email, PostJobRequest request) {
        RUser rUser = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
        RCompany company = companyRepository.findById(rUser.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));

        Job job = new Job();
        job.setId(NanoIdUtils.randomNanoId());
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompany(company);
        job.setCreatedAt(LocalDateTime.now());
        jobRepository.save(job);
    }

    public List<GetListJobResponse> getListJob(String email) {
        RUser rUser = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
        RCompany company = companyRepository.findById(rUser.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
        List<Job> jobList = jobRepository.findAllByCompanyOrderByCreatedAtDesc(company);
        List<GetListJobResponse> responses = new ArrayList<>();

        for (Job job : jobList) {
            GetListJobResponse item = new GetListJobResponse();
            item.setId(job.getId());
            item.setTitle(job.getTitle());
            item.setDescription(job.getDescription());
            item.setLogo(job.getCompany().getLogoImage());
            item.setCreatedAt(job.getCreatedAt());
            responses.add(item);
        }

        return responses;
    }


    public List<GetListJobResponse> getListJobByCompanyId(String companyId) {
        RCompany company = companyRepository.findById(companyId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
        List<Job> jobList = jobRepository.findAllByCompanyOrderByCreatedAtDesc(company);
        List<GetListJobResponse> responses = new ArrayList<>();

        for (Job job : jobList) {
            GetListJobResponse item = new GetListJobResponse();
            item.setId(job.getId());
            item.setTitle(job.getTitle());
            item.setDescription(job.getDescription());
            item.setLogo(job.getCompany().getLogoImage());
            item.setCreatedAt(job.getCreatedAt());
            responses.add(item);
        }

        return responses;
    }

    @Transactional
    public void editJob(String jobId, PutJobRequest request) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job not found"));
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        jobRepository.save(job);
    }

    @Transactional
    public void deleteJob(String jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job not found"));
        jobRepository.delete(job);
    }

    public GetListJobResponse getJobById(String jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job not found"));
        GetListJobResponse response = new GetListJobResponse();
        response.setId(jobId);
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setLogo(job.getCompany().getLogoImage());
        response.setCompanyId(job.getCompany().getId());
        response.setCreatedAt(job.getCreatedAt());
        return response;
    }

    public List<GetListCvResponse> getCvsByJobId(String jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job not found"));
        List<Cv> cvList = cvRepository.findByJob(job);
        List<GetListCvResponse> responses = new ArrayList<>();
        for (Cv cv : cvList) {
            GetListCvResponse item = new GetListCvResponse();
            item.setId(cv.getId());
            item.setCvUrl(cv.getLinkCv());
            item.setMemberId(cv.getMember().getId());
            item.setJobId(jobId);
            item.setEmail(cv.getMember().getRUser().getEmail());
            responses.add(item);
        }

        return responses;
    }
}
