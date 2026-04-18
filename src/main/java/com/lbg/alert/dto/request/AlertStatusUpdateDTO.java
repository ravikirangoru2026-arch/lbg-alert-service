package com.lbg.alert.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlertStatusUpdateDTO {

    @NotBlank(message = "Status must not be blank")
    @Pattern(
        regexp = "NEW|UNDER_REVIEW|ESCALATED|CLOSED",
        message = "Status must be one of: NEW, UNDER_REVIEW, ESCALATED, CLOSED"
    )
    private String status;
}