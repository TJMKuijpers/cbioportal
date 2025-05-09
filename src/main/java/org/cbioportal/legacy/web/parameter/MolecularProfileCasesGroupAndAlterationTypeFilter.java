package org.cbioportal.legacy.web.parameter;

import io.swagger.v3.oas.annotations.media.Schema;
import org.cbioportal.legacy.model.AlterationFilter;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.Size;

public class MolecularProfileCasesGroupAndAlterationTypeFilter implements Serializable {


    private AlterationFilter alterationEventTypes;
    @Size(min = 1, max = PagingConstants.MAX_PAGE_SIZE)
    @Schema
    private List<MolecularProfileCasesGroupFilter> molecularProfileCasesGroupFilter;
 

    public List<MolecularProfileCasesGroupFilter> getMolecularProfileCasesGroupFilter() {
        return this.molecularProfileCasesGroupFilter;
    }

    public void setMolecularProfileCasesGroupFilter(
            List<MolecularProfileCasesGroupFilter> molecularProfileCasesGroupFilter) {
                this.molecularProfileCasesGroupFilter = molecularProfileCasesGroupFilter;
    }

    public AlterationFilter getAlterationEventTypes() {
        return this.alterationEventTypes;
    }

    public void setAlterationEventTypes(AlterationFilter alterationEventTypes) {
        this.alterationEventTypes = alterationEventTypes;
    }
}
