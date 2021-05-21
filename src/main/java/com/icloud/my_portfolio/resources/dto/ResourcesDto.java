package com.icloud.my_portfolio.resources.dto;

import com.icloud.my_portfolio.resources.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResourcesDto {

    private Long id;
    private String resourceName;
    private ResourceType resourceType;
    private HttpMethod httpMethod;
    private int orderNum;

    private List<String> roleList = new ArrayList<>();
}
