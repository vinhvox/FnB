package com.example.fnbapp.Module;

import java.util.List;

public class AreaModule {

    String areaName;
    List<TableModule> tableModules;

    public AreaModule() {
    }

    public AreaModule(String areaName, List<TableModule> tableModules) {
        this.areaName = areaName;
        this.tableModules = tableModules;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public List<TableModule> getTableModules() {
        return tableModules;
    }

    public void setTableModules(List<TableModule> tableModules) {
        this.tableModules = tableModules;
    }
}
