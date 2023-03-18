package com.dblab.webservice.domain.user.model.info.impl;

import com.dblab.webservice.domain.user.model.info.OAuth2UserInfo;
import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {
    private String id;
    private String name;
    private String email;
    private String imageUrl;

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    public GoogleOAuth2UserInfo(String id, String name, String email, String imageUrl) {
        super(null);
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }
}
