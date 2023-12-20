package vn.com.greencraze.auth.service;

import vn.com.greencraze.auth.dto.response.role.GetListRoleResponse;
import vn.com.greencraze.auth.dto.response.role.GetOneRoleResponse;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;

public interface IRoleService {

    RestResponse<ListResponse<GetListRoleResponse>> getListRole(
            Integer page, Integer size, Boolean isSortAscending,
            String columnName, String search, Boolean all, Boolean status);

    RestResponse<GetOneRoleResponse> getOneRole(String id);

}
