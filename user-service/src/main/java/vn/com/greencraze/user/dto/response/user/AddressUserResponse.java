package vn.com.greencraze.user.dto.response.user;

public record AddressUserResponse(
        String receiver,
        String phone,
        String email,
        String street,
        Boolean isDefault,
        ProvinceResponse province,
        DistrictResponse district,
        WardResponse ward
) {

    public record ProvinceResponse(
            String name,
            String code
    ) {}

    public record DistrictResponse(
            String name,
            String code
    ) {}

    public record WardResponse(
            String name,
            String code
    ) {}

}
