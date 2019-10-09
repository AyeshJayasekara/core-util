/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * <p>
 * WSO2.Telco Inc. licences this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.core.userprofile.prosser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.user.api.RealmConfiguration;
import org.wso2.carbon.user.core.service.RealmService;

import com.wso2telco.core.userprofile.dto.UserRoleDTO;

public class UserRoleProsser {

	private final Log log = LogFactory.getLog(UserRoleProsser.class);

	public List<String> getRolesByUserName(String userName) {

		PrivilegedCarbonContext carbonContext = PrivilegedCarbonContext.getThreadLocalCarbonContext();
		RealmService realmService = (RealmService) carbonContext.getOSGiService(RealmService.class, null);

		List<String> currentUserRoleList = null;

		try {

			RealmConfiguration realmConfiguration = new RealmConfiguration();
			String[] currentUserRoles = realmService.getUserRealm(realmConfiguration).getUserStoreManager()
					.getRoleListOfUser(userName);

			currentUserRoleList = Arrays.asList(currentUserRoles);
		} catch (org.wso2.carbon.user.api.UserStoreException e) {

			log.error("unable to retrieve user roles for user " + userName + " : ", e);
		}

		if (currentUserRoleList != null && !currentUserRoleList.isEmpty()) {

			return currentUserRoleList;
		} else {

			return Collections.emptyList();
		}
	}

	public UserRoleDTO getUserRoles(String userName) {

		UserRoleDTO userRoleDTO = null;
		
		List<String> currentUserRoleList = getRolesByUserName(userName);

		if (!currentUserRoleList.isEmpty()) {

			String[] userRoles = new String[currentUserRoleList.size()];
			userRoles = currentUserRoleList.toArray(userRoles);
			userRoleDTO = fillUserRoleDTO(userRoles, new UserRoleDTO());
		}

		return userRoleDTO;
	}

	private UserRoleDTO fillUserRoleDTO(String[] userRoles, UserRoleDTO userRoleDTO) {

		userRoleDTO.setUserRoles(userRoles);
		
		return userRoleDTO;
	}
}
