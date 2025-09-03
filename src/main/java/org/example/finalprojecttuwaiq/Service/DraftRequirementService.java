package org.example.finalprojecttuwaiq.Service;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.Model.DraftRequirement;
import org.example.finalprojecttuwaiq.Repository.DraftRequirementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DraftRequirementService {
    private final DraftRequirementRepository draftRequirementRepository;

    public  List<DraftRequirement> getDraftRequirementByProjectId(Integer project_id){
        return draftRequirementRepository.findDraftRequirementByProject_id(project_id);
    }
    public List<DraftRequirement> getAllRequirement(){
        return draftRequirementRepository.findAll();
    }

    public  DraftRequirement getDraftById(Integer draft_id){
        return draftRequirementRepository.findDraftRequirementById(draft_id);
    }

    //TODO: Reject Draft And Delete it from DB By Id
    public void rejectDraft(Integer draft_id){
        DraftRequirement draftRequirement = draftRequirementRepository.findDraftRequirementById(draft_id);
        if (draftRequirement == null)
            throw new ApiException("Draft Not Found");
        draftRequirementRepository.delete(draftRequirement);
    }
}
