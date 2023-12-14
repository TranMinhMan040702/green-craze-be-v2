package vn.com.greencraze.auth.mapper;

import org.mapstruct.Mapper;
import vn.com.greencraze.auth.dto.response.role.GetListRoleResponse;
import vn.com.greencraze.auth.dto.response.role.GetOneRoleResponse;
import vn.com.greencraze.auth.entity.Role;

@Mapper
public interface RoleMapper {

    GetListRoleResponse roleToGetListRoleResponse(Role role);

    GetOneRoleResponse roleToGetOneRoleResponse(Role role);

}
