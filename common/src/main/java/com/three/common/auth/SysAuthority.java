package com.three.common.auth;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限标识
 *
 */
@Data
public class SysAuthority implements Serializable {

	private static final long serialVersionUID = 280565233032255804L;

	private String id;
	private String parentId;
	private String parentName;
	private String authorityUrl;
	private String authorityName;
	private Integer authorityType;
	private String authorityIcon;
	private Integer sort;
	private String parentIds;
	private String remark;
	private Integer status;
	private Date createDate;
	private Date updateDate;

}
