package com.shaileshkarwa.Userservice.projection;

import com.shaileshkarwa.Commonservice.model.CardDetails;
import com.shaileshkarwa.Commonservice.model.User;
import com.shaileshkarwa.Commonservice.queries.GetUserPaymentDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserProjection {

    @QueryHandler
    public User getUserPaymentDetails(GetUserPaymentDetailsQuery getUserPaymentDetailsQuery) {
        CardDetails cardDetails = CardDetails.builder()
                .name("Shailesh Karwa")
                .cardNumber("1234567890")
                .validUntilMonth(9)
                .validUntilYear(2026)
                .cvv(123)
                .build();
        User user = User.builder()
                .userId(getUserPaymentDetailsQuery.getUserId())
                .firstName("Shailesh")
                .lastName("Karwa")
                .cardDetails(cardDetails)
                .build();
        return user;
    }
}
