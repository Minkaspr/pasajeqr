package com.mk.pasajeqr.admin;

import com.mk.pasajeqr.admin.request.AdminCreateRQ;
import com.mk.pasajeqr.admin.request.AdminUpdateRQ;
import com.mk.pasajeqr.admin.response.AdminDetailRS;
import com.mk.pasajeqr.admin.response.AdminsRS;
import com.mk.pasajeqr.utils.ChangePasswordRQ;
import com.mk.pasajeqr.utils.UserStatusRS;
import com.mk.pasajeqr.utils.BulkDeleteRS;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {
    AdminsRS getAllPaged(Pageable pageable);
    AdminDetailRS create(AdminCreateRQ request);
    AdminDetailRS getById(Long id);
    AdminDetailRS update(Long id, AdminUpdateRQ request);
    void changePassword(Long id, ChangePasswordRQ request);
    UserStatusRS setUserStatus(Long id, boolean active);
    void delete(Long id);
    BulkDeleteRS deleteBulk(List<Long> ids);
}
