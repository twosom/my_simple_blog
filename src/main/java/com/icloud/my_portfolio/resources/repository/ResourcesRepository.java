package com.icloud.my_portfolio.resources.repository;

import com.icloud.my_portfolio.resources.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpMethod;

public interface ResourcesRepository extends JpaRepository<Resources, Long> {

    Resources findByResourceNameAndHttpMethod(String resourceName, HttpMethod httpMethod);
}
