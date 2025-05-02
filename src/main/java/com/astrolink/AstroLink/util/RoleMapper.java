package com.astrolink.AstroLink.util;

import com.astrolink.AstroLink.entity.Role;

import java.util.List;

public class RoleMapper {
    private RoleMapper(){}
    public static Role mapToRole(String role){
        Role[] roles = Role.values();
        for(Role ele : roles){
            boolean match =
                    ele.toString().equalsIgnoreCase(role);
            if(match){
                return ele;
            }
        }
        throw new IllegalArgumentException("No matching role found");
    }
}
