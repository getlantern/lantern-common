package org.lantern;

import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.lantern.SemanticVersion;


public class LanternVersion extends SemanticVersion {
    private final static transient Logger LOG = Logger
            .getLogger("LanternVersion");

    protected Date releaseDate;

    protected String infoUrl;

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

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("infoUrl", getInfoUrl());
        map.put("releaseDate", DateSerializer.formattedDate(getReleaseDate()));
        return map;
    }
}
