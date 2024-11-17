package cfa.fishing.fishing_store_app.dto.response;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class CategoryResponse {
    private String id;
    private String name;
}
