package org.example.finalprojecttuwaiq.Service;

import lombok.RequiredArgsConstructor;
import org.example.finalprojecttuwaiq.Api.ApiException;
import org.example.finalprojecttuwaiq.Model.BA;
import org.example.finalprojecttuwaiq.Model.DraftRequirement;
import org.example.finalprojecttuwaiq.Model.Project;
import org.example.finalprojecttuwaiq.Repository.BARepository;
import org.example.finalprojecttuwaiq.Repository.DraftRequirementRepository;
import org.example.finalprojecttuwaiq.Repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DraftRequirementService {
    private final DraftRequirementRepository draftRequirementRepository;
    private final BARepository baRepository;
    private final ProjectRepository projectRepository;
    public  List<DraftRequirement> getDraftRequirementByProjectId(Integer project_id){
        return draftRequirementRepository.findDraftRequirementByProject_id(project_id);
    }
    public List<DraftRequirement> getAllRequirement(){
        return draftRequirementRepository.findAll();
    }

    public  DraftRequirement getDraftById(Integer ba_id,Integer draft_id){
        DraftRequirement draftRequirement = draftRequirementRepository.findDraftRequirementById(draft_id);
        if (draftRequirement == null)
            throw new ApiException("Draft Not Found");
        Project project = projectRepository.findProjectById(draftRequirement.getProject_id());
        if (project == null)
            throw new ApiException("project not found");
        BA ba = baRepository.findBAById(ba_id);
        if (ba == null)
            throw new ApiException("BA not found");

        if (!project.getBas().contains(ba))
            throw new ApiException("Not Authorized");
        return draftRequirement;
    }

    //TODO: Reject Draft And Delete it from DB By Id
    public void rejectDraft(Integer ba_id,Integer draft_id){
        DraftRequirement draftRequirement = draftRequirementRepository.findDraftRequirementById(draft_id);
        if (draftRequirement == null)
            throw new ApiException("Draft Not Found");
        Project project = projectRepository.findProjectById(draftRequirement.getProject_id());
        if (project == null)
            throw new ApiException("project not found");
        BA ba = baRepository.findBAById(ba_id);
        if (ba == null)
            throw new ApiException("BA not found");

        if (!project.getBas().contains(ba))
            throw new ApiException("Not Authorized");



        draftRequirementRepository.delete(draftRequirement);
    }
}
