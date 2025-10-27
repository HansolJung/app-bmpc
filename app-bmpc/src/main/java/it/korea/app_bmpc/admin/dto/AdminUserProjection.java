package it.korea.app_bmpc.admin.dto;

import java.time.LocalDateTime;

public interface AdminUserProjection {

    String getUserId();
    String getUserName();
    String getBirth();
    String getGender();
    String getPhone();
    String getEmail();
    LocalDateTime getCreateDate();
    LocalDateTime getUpdateDate();
    String getUseYn();
    String getDelYn();
    String getRoleId();
    String getRoleName();
    int getDeposit();
    int getBalance();
    String getBusinessNo();
}
