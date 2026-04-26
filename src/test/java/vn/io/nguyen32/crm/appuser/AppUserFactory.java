package vn.io.nguyen32.crm.appuser;

import static top.nguyennd.restsqlbackend.abstraction.cache.CommonKey.ENTITY_KEY;
import static vn.io.nguyen32.crm.common.AppConstant.DEFAULT_ROLE;

import lombok.experimental.UtilityClass;
import vn.io.nguyen32.crm.appuser.dto.AppUserBaseResDto;
import vn.io.nguyen32.crm.appuser.dto.AppUserReqDto;
import vn.io.nguyen32.crm.appuser.dto.AppUserUpdateReqDto;
import vn.io.nguyen32.crm.appuser.entity.AppUser;
import vn.io.nguyen32.crm.common.AppRoles;

@UtilityClass
public class AppUserFactory {

    public static String cacheKey(Long id) {
        return ENTITY_KEY.formatted(AppUser.class.getSimpleName(), id);
    }

    public static AppUser appUser() {
        return AppUser.builder()
            .id(1L)
            .username("testuser")
            .password("encoded_password")
            .email("test@example.com")
            .fullName("Test User")
            .roleName(DEFAULT_ROLE)
            .build();
    }

    private static AppUser appUser(AppUser.AppUserBuilder builder) {
        return builder.build();
    }

    public static AppUser appUser(String username, String password) {
        var appUserBuilder = AppUser.builder()
            .username(username)
            .password(password)
            .roleName(DEFAULT_ROLE);
        return appUser(appUserBuilder);
    }

    public static AppUserReqDto appUserReqDto() {
        return new AppUserReqDto(
            "testuser",
            "password123",
            "test@example.com",
            "Test User"
        );
    }

    public static AppUserBaseResDto appUserResDto() {
        return new AppUserBaseResDto(
            1L,
            "testuser",
            "test@example.com",
            "fullname",
            AppRoles.USER,
            null
        );
    }

    public static AppUserUpdateReqDto appUserReqUpdateDto() {
        return new AppUserUpdateReqDto(
            "test@example.com",
            "fullName",
            AppRoles.USER
        );
    }
}
