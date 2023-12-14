package vn.com.greencraze.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.com.greencraze.auth.dto.response.role.GetListRoleResponse;
import vn.com.greencraze.auth.dto.response.role.GetOneRoleResponse;
import vn.com.greencraze.auth.entity.Role;
import vn.com.greencraze.auth.mapper.RoleMapper;
import vn.com.greencraze.auth.repository.RoleRepository;
import vn.com.greencraze.auth.repository.specification.RoleSpecification;
import vn.com.greencraze.auth.service.IRoleService;
import vn.com.greencraze.commons.api.ListResponse;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    private static final String RESOURCE_NAME = "Role";
    private static final List<String> SEARCH_FIELDS = List.of("name");

    @Override
    public RestResponse<ListResponse<GetListRoleResponse>> getListRole(
            Integer page, Integer size, Boolean isSortAscending,
            String columnName, String search, Boolean all, Boolean status
    ) {
        RoleSpecification roleSpecification = new RoleSpecification();
        Specification<Role> sortable = roleSpecification.sortable(isSortAscending, columnName);
        Specification<Role> searchable = roleSpecification.searchable(SEARCH_FIELDS, search);
        Specification<Role> filterableByStatus = roleSpecification.filterable(status);
        Pageable pageable = all ? Pageable.unpaged() : PageRequest.of(page - 1, size);
        Page<GetListRoleResponse> responses = roleRepository
                .findAll(sortable.and(searchable).and(filterableByStatus), pageable)
                .map(roleMapper::roleToGetListRoleResponse);
        return RestResponse.ok(ListResponse.of(responses));
    }

    @Override
    public RestResponse<GetOneRoleResponse> getOneRole(String id) {
        return roleRepository.findById(id)
                .map(roleMapper::roleToGetOneRoleResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }

}
