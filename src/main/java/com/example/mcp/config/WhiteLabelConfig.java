package com.example.mcp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "info.app")
public class WhiteLabelConfig {
    
    private String name;
    private String description;
    private String version;
    private String author;
    private String company;
    private String website;
    private String copyright;
    private Support support = new Support();
    private Logo logo = new Logo();
    private Theme theme = new Theme();
    private String termsUrl;
    private String privacyUrl;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getCompany() {
        return company;
    }
    
    public void setCompany(String company) {
        this.company = company;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public String getCopyright() {
        return copyright;
    }
    
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
    
    public Support getSupport() {
        return support;
    }
    
    public void setSupport(Support support) {
        this.support = support;
    }
    
    public Logo getLogo() {
        return logo;
    }
    
    public void setLogo(Logo logo) {
        this.logo = logo;
    }
    
    public Theme getTheme() {
        return theme;
    }
    
    public void setTheme(Theme theme) {
        this.theme = theme;
    }
    
    public String getTermsUrl() {
        return termsUrl;
    }
    
    public void setTermsUrl(String termsUrl) {
        this.termsUrl = termsUrl;
    }
    
    public String getPrivacyUrl() {
        return privacyUrl;
    }
    
    public void setPrivacyUrl(String privacyUrl) {
        this.privacyUrl = privacyUrl;
    }
    
    public static class Support {
        private String email;
        private String phone;
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getPhone() {
            return phone;
        }
        
        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
    
    public static class Logo {
        private String url;
        
        public String getUrl() {
            return url;
        }
        
        public void setUrl(String url) {
            this.url = url;
        }
    }
    
    public static class Theme {
        private Primary primary = new Primary();
        private Secondary secondary = new Secondary();
        
        public Primary getPrimary() {
            return primary;
        }
        
        public void setPrimary(Primary primary) {
            this.primary = primary;
        }
        
        public Secondary getSecondary() {
            return secondary;
        }
        
        public void setSecondary(Secondary secondary) {
            this.secondary = secondary;
        }
        
        public static class Primary {
            private String color;
            
            public String getColor() {
                return color;
            }
            
            public void setColor(String color) {
                this.color = color;
            }
        }
        
        public static class Secondary {
            private String color;
            
            public String getColor() {
                return color;
            }
            
            public void setColor(String color) {
                this.color = color;
            }
        }
    }
}