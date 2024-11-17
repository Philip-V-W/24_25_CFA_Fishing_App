package cfa.fishing.fishing_store_app.dto.request;

import lombok.Data;

@Data
public class UserProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
}