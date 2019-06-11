package com.eleksploded.lavadynamics;

import com.eleksploded.lavadynamics.proxy.CommonProxy;

import net.minecraftforge.common.config.Configuration;

public class Config {
	//-----------Config: to be honest, not to sure how this code works, I just know it does-----------//
	private static final String Volcano = "Volcano Settings";
	private static final String Debug = "Debug Settings";

    public static int volcanoChance;
    public static int volcanoYLevel;
    public static int timeToRise;
    
    public static boolean genVolcanoDebug;
    
    public static void readConfig() {
        Configuration cfg = CommonProxy.config;
        try {
            cfg.load();
            initGeneralConfig(cfg);
        } catch (Exception e1) {
            System.out.println("Problem loading config file! "+ e1);
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    private static void initGeneralConfig(Configuration cfg) {    	
    	volcanoChance = cfg.getInt("volcanoChance", Volcano, 20, 0, 100, "Precent chance of volcano to spawn");
    	volcanoYLevel = cfg.getInt("volcanoYLevel", Volcano, 10, 3, 255, "Approximate Y level of underground volcano lake");
    	timeToRise = cfg.getInt("timeToRise", Volcano, 100, 50, 10000, "Amount of time (in milli seconds) between a new Lava source when growing a volcano");
    	
    	genVolcanoDebug = cfg.getBoolean("genVolcanoDebug", Debug, false, "Debug outputs from Volcano Generation");
    }
}