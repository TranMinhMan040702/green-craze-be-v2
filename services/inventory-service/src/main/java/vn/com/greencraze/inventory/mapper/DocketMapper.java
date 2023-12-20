package vn.com.greencraze.inventory.mapper;

import org.mapstruct.Mapper;
import vn.com.greencraze.inventory.dto.response.GetListDocketByProductResponse;
import vn.com.greencraze.inventory.entity.Docket;

@Mapper
public interface DocketMapper {

    GetListDocketByProductResponse docketToGetListDocketByProductResponse(Docket docket);

}
