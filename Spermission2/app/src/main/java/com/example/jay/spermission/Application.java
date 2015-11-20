package com.example.jay.spermission;

import java.util.ArrayList;
import java.util.List;

public class Application {
	private String label;
	private String name;
	private int versionCode;
	private String versionName;
	private int system;
	private List<Integer> permissions;
	
	public Application(String label, String name, int versionCode, String versionName, int system) {
		this.label = label;
		this.name = name;
		this.versionCode = versionCode;
		this.versionName = versionName;
		this.system = system;
		permissions = new ArrayList<Integer>();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int isSystem() {
		return system;
	}

	public void setSystem(int system) {
		this.system = system;
	}

	public List<Integer> getPermissions() {
		return permissions;
	}
	
	public void addPermission(Integer permissionId) {
		if (!permissions.contains(permissionId))
			permissions.add(permissionId);
	}
	
	
}
