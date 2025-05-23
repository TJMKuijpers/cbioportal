package org.cbioportal.legacy.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.math3.util.Pair;
import org.cbioportal.legacy.model.AlterationCountByGene;
import org.cbioportal.legacy.model.AlterationEnrichment;
import org.cbioportal.legacy.model.AlterationFilter;
import org.cbioportal.legacy.model.EnrichmentType;
import org.cbioportal.legacy.model.MolecularProfileCaseIdentifier;
import org.cbioportal.legacy.model.util.Select;
import org.cbioportal.legacy.service.AlterationCountService;
import org.cbioportal.legacy.service.AlterationEnrichmentService;
import org.cbioportal.legacy.service.util.AlterationEnrichmentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlterationEnrichmentServiceImpl implements AlterationEnrichmentService {

  @Autowired private AlterationCountService alterationCountService;
  @Autowired private AlterationEnrichmentUtil<AlterationCountByGene> alterationEnrichmentUtil;

  @Override
  public List<AlterationEnrichment> getAlterationEnrichments(
      Map<String, List<MolecularProfileCaseIdentifier>> molecularProfileCaseSets,
      EnrichmentType enrichmentType,
      AlterationFilter alterationFilter) {

    Map<String, Pair<List<AlterationCountByGene>, Long>> alterationCountsbyEntrezGeneIdAndGroup =
        getAlterationCountsbyEntrezGeneIdAndGroup(
            molecularProfileCaseSets, enrichmentType, alterationFilter);

    return alterationEnrichmentUtil.createAlterationEnrichments(
        alterationCountsbyEntrezGeneIdAndGroup);
  }

  public Map<String, Pair<List<AlterationCountByGene>, Long>>
      getAlterationCountsbyEntrezGeneIdAndGroup(
          Map<String, List<MolecularProfileCaseIdentifier>> molecularProfileCaseSets,
          EnrichmentType enrichmentType,
          AlterationFilter alterationFilter) {
    return molecularProfileCaseSets.entrySet().stream()
        .collect(
            Collectors.toMap(
                entry -> entry.getKey(), // group name
                entry -> { // group counts
                  if (enrichmentType.equals(EnrichmentType.SAMPLE)) {
                    return alterationCountService.getSampleAlterationGeneCounts(
                        entry.getValue(), Select.all(), true, true, alterationFilter);
                  } else {
                    return alterationCountService.getPatientAlterationGeneCounts(
                        entry.getValue(), Select.all(), true, true, alterationFilter);
                  }
                }));
  }
}
