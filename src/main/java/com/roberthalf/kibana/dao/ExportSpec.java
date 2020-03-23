package com.roberthalf.kibana.dao;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExportSpec {
    
    private List<String> type;
    private boolean includeReferencesDeep;

}
