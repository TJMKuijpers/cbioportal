package org.cbioportal.legacy.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.cbioportal.legacy.model.ClinicalAttribute;
import org.cbioportal.legacy.model.ClinicalAttributeCount;
import org.cbioportal.legacy.model.meta.BaseMeta;
import org.cbioportal.legacy.persistence.ClinicalAttributeRepository;
import org.cbioportal.legacy.service.ClinicalAttributeService;
import org.cbioportal.legacy.service.StudyService;
import org.cbioportal.legacy.service.exception.ClinicalAttributeNotFoundException;
import org.cbioportal.legacy.service.exception.StudyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;

@Service
public class ClinicalAttributeServiceImpl implements ClinicalAttributeService {

  @Autowired private ClinicalAttributeRepository clinicalAttributeRepository;
  @Autowired private StudyService studyService;

  @Value("${authenticate:false}")
  private String AUTHENTICATE;

  @Override
  @PostFilter(
      "hasPermission(filterObject.cancerStudyIdentifier, 'CancerStudyId', T(org.cbioportal.legacy.utils.security.AccessLevel).READ)")
  public List<ClinicalAttribute> getAllClinicalAttributes(
      String projection, Integer pageSize, Integer pageNumber, String sortBy, String direction) {

    List<ClinicalAttribute> clinicalAttributes =
        clinicalAttributeRepository.getAllClinicalAttributes(
            projection, pageSize, pageNumber, sortBy, direction);
    // copy the list before returning so @PostFilter doesn't taint the list stored in the
    // persistence layer cache
    return (AUTHENTICATE.equals("false"))
        ? clinicalAttributes
        : new ArrayList<ClinicalAttribute>(clinicalAttributes);
  }

  @Override
  public BaseMeta getMetaClinicalAttributes() {

    return clinicalAttributeRepository.getMetaClinicalAttributes();
  }

  @Override
  public ClinicalAttribute getClinicalAttribute(String studyId, String clinicalAttributeId)
      throws ClinicalAttributeNotFoundException, StudyNotFoundException {

    studyService.getStudy(studyId);

    ClinicalAttribute clinicalAttribute =
        clinicalAttributeRepository.getClinicalAttribute(studyId, clinicalAttributeId);

    if (clinicalAttribute == null) {
      throw new ClinicalAttributeNotFoundException(studyId, clinicalAttributeId);
    }

    return clinicalAttribute;
  }

  @Override
  public List<ClinicalAttribute> getAllClinicalAttributesInStudy(
      String studyId,
      String projection,
      Integer pageSize,
      Integer pageNumber,
      String sortBy,
      String direction)
      throws StudyNotFoundException {

    studyService.getStudy(studyId);

    return clinicalAttributeRepository.getAllClinicalAttributesInStudy(
        studyId, projection, pageSize, pageNumber, sortBy, direction);
  }

  @Override
  public BaseMeta getMetaClinicalAttributesInStudy(String studyId) throws StudyNotFoundException {

    studyService.getStudy(studyId);

    return clinicalAttributeRepository.getMetaClinicalAttributesInStudy(studyId);
  }

  @Override
  public List<ClinicalAttribute> fetchClinicalAttributes(List<String> studyIds, String projection) {

    return clinicalAttributeRepository.fetchClinicalAttributes(studyIds, projection);
  }

  @Override
  public BaseMeta fetchMetaClinicalAttributes(List<String> studyIds) {

    return clinicalAttributeRepository.fetchMetaClinicalAttributes(studyIds);
  }

  @Override
  public List<ClinicalAttributeCount> getClinicalAttributeCountsBySampleIds(
      List<String> studyIds, List<String> sampleIds) {

    return clinicalAttributeRepository.getClinicalAttributeCountsBySampleIds(studyIds, sampleIds);
  }

  @Override
  public List<ClinicalAttributeCount> getClinicalAttributeCountsBySampleListId(
      String sampleListId) {

    return clinicalAttributeRepository.getClinicalAttributeCountsBySampleListId(sampleListId);
  }

  @Override
  public List<ClinicalAttribute> getClinicalAttributesByStudyIdsAndAttributeIds(
      List<String> studyIds, List<String> attributeIds) {

    return clinicalAttributeRepository.getClinicalAttributesByStudyIdsAndAttributeIds(
        studyIds, attributeIds);
  }
}
