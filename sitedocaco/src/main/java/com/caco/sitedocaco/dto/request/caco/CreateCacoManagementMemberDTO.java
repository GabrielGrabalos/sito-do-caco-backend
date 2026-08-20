package com.caco.sitedocaco.dto.request.caco;

import com.caco.sitedocaco.entity.caco.CacoManagementMember;
import jakarta.validation.constraints.NotNull;

Public record CreateCacoManagementMemberDTO(
    
    UUID cacoManagement
    
    @NotBlank(message="O nome é obrigatorio")
    String name

    @NotNull
    CacoManagementRole role;
    


)
