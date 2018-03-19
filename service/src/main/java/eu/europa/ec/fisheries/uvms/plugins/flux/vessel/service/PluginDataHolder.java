/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class PluginDataHolder {

    public static final String PLUGIN_PROPERTIES = "plugin.properties";
    public static final String PROPERTIES = "settings.properties";
    public static final String CAPABILITIES = "capabilities.properties";

    private Properties fluxApplicationProperties;
    private Properties fluxProperties;
    private Properties fluxCapabilities;

    private final ConcurrentHashMap<String, String> settings = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> capabilities = new ConcurrentHashMap<>();

    public ConcurrentMap<String, String> getSettings() {
        return settings;
    }

    public ConcurrentMap<String, String> getCapabilities() {
        return capabilities;
    }


    public Properties getPluginApplicationProperties() {
        return fluxApplicationProperties;
    }

    public void setPluginApplicationProperties(Properties fluxApplicaitonProperties) {
        this.fluxApplicationProperties = fluxApplicaitonProperties;
    }

    public Properties getPluginProperties() {
        return fluxProperties;
    }

    public void setPluginProperties(Properties fluxProperties) {
        this.fluxProperties = fluxProperties;
    }

    public Properties getPluginCapabilities() {
        return fluxCapabilities;
    }

    public void setPluginCapabilities(Properties fluxCapabilities) {
        this.fluxCapabilities = fluxCapabilities;
    }

}