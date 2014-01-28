package org.lantern;

import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;


public class LanternVersion extends SemanticVersion {
    private final static transient Logger LOG = Logger
            .getLogger("LanternVersion");

    protected Date releaseDate;
    protected String infoUrl;
    protected String installerBaseUrl;

    public LanternVersion() {
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }

    public String getInstallerBaseUrl() {
        return installerBaseUrl;
    }

    public void setInstallerBaseUrl(String installerBaseUrl) {
        this.installerBaseUrl = installerBaseUrl;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("infoUrl", getInfoUrl());
        map.put("installerBaseUrl", getInstallerBaseUrl());
        map.put("releaseDate", DateSerializer.formattedDate(getReleaseDate()));
        return map;
    }
}
