package vn.com.greencraze.address.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import vn.com.greencraze.address.service.IAddressService;

@Component
@RequiredArgsConstructor
public class TestRunner implements CommandLineRunner {

    private final ApplicationContext context;
    private final IAddressService addressService;

    @Override
    public void run(String... args) throws Exception {
        context.getApplicationName();

        //        var resp = addressService.getListAddressByUserId("358f7ae0-9815-42c4-bd03-6339838d2046");
    }

}
