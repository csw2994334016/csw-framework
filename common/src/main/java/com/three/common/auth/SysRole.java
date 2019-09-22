package com.three.common.auth;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色
 */
@Data
public class SysRole implements Serializable {

	private static final long serialVersionUID = -2054359538140713354L;

	private String id;

	private String roleCode;

	private String roleName;

	private Date createDate;

	private Date updateDate;
}
