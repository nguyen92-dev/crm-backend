package vn.io.nguyen32.crm.appuser.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static top.nguyennd.restsqlbackend.abstraction.cache.CommonKey.ENTITY_KEY;
import static vn.io.nguyen32.crm.common.AppConstant.DEFAULT_ROLE;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import top.nguyennd.restsqlbackend.abstraction.exception.BusinessException;
import vn.io.nguyen32.crm.appuser.AppUserFactory;
import vn.io.nguyen32.crm.appuser.AppUserRepository;
import vn.io.nguyen32.crm.appuser.dto.AppUserBaseResDto;
import vn.io.nguyen32.crm.appuser.dto.AppUserReqDto;
import vn.io.nguyen32.crm.appuser.dto.UserMapper;
import vn.io.nguyen32.crm.appuser.entity.AppUser;
import vn.io.nguyen32.crm.rediscache.IRedisCacheService;

@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {

    @Mock
    private AppUserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IRedisCacheService cacheService;

    @InjectMocks
    private AppUserServiceImpl appUserService;

    private AppUserReqDto reqDto;
    private AppUser savedEntity;

    @BeforeEach
    void setUp() {
        reqDto = AppUserFactory.appUserReqDto();
        savedEntity = AppUserFactory.appUser();
    }

    @Test
    void createUser_ShouldWorkCorrectly() {
        // Mock mapper toEntity
        when(mapper.toEntity(reqDto)).thenReturn(
            AppUser.builder()
                .username(reqDto.username())
                .password(reqDto.password())
                .build()
        );

        // Mock passwordEncoder
        when(passwordEncoder.encode(reqDto.password())).thenReturn(
            "encoded_password"
        );
        // Mock redis set cache
        doNothing().when(cacheService).setCache(any(), any(), any(), any());

        // Mock repository save
        when(repository.save(any())).thenAnswer(invocation -> {
            AppUser u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        // Mock mapper toResDto
        when(mapper.toResDto(any())).thenAnswer(invocationOnMock -> {
            AppUser u = invocationOnMock.getArgument(0);
            return AppUserBaseResDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .build();
        });

        // Execute
        AppUserBaseResDto result = appUserService.createUser(reqDto);

        // Verify
        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
        verify(repository).save(captor.capture());

        AppUser savedUser = captor.getValue();

        ArgumentCaptor<String> cacheKeyCaptor = ArgumentCaptor.forClass(
            String.class
        );
        verify(cacheService).setCache(
            cacheKeyCaptor.capture(),
            eq(result),
            any(),
            any()
        );
        assertNotNull(cacheKeyCaptor.getValue());

        String cacheKey = cacheKeyCaptor.getValue();
        assertEquals(
            ENTITY_KEY.formatted(AppUser.class.getSimpleName(), result.id()),
            cacheKey
        );

        verify(repository, atMost(2)).findAll(
            (Specification<AppUser>) isNotNull()
        );

        assertEquals("encoded_password", savedUser.getPassword());
        assertEquals(DEFAULT_ROLE, savedUser.getRoleName());

        assertEquals(savedUser.getUsername(), result.username());
    }

    @Test
    void createUser_ConflictUser_ShouldThrowException() {
        when(repository.findAll(any(Specification.class))).thenReturn(
            List.of(savedEntity)
        );

        // Execute
        BusinessException businessException = assertThrows(
            BusinessException.class,
            () -> appUserService.createUser(reqDto)
        );
        assertEquals(HttpStatus.CONFLICT, businessException.getStatusCode());

        // Verify
        verify(repository, atLeast(1)).findAll(
            (Specification<AppUser>) isNotNull()
        );
        verify(repository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
        verify(cacheService, never()).setCache(any(), any(), any(), any());
        verify(mapper, never()).toResDto(any());
    }

    @Test
    void getUserById_cacheExist_ShouldNotCallRepository() {
        // Mock response
        var res = AppUserFactory.appUserResDto();
        var cacheKey = AppUserFactory.cacheKey(res.id());
        // Mock cache get
        when(
            cacheService.getCache(cacheKey, AppUserBaseResDto.class)
        ).thenReturn(Optional.of(res));

        AppUserBaseResDto result = appUserService.findUserById(res.id());
        assertEquals(res, result);
        verify(repository, never()).findById(anyLong());
        verify(cacheService, times(1)).getCache(
            cacheKey,
            AppUserBaseResDto.class
        );
        verify(mapper, never()).toResDto(any());
        verify(cacheService, never()).setCache(any(), any(), any(), any());
    }

    @Test
    void getUserById_cacheNotExist_thenShouldCallRepository() {
        String findKey = AppUserFactory.cacheKey(savedEntity.getId());
        // Mock cache get
        when(
            cacheService.getCache(findKey, AppUserBaseResDto.class)
        ).thenReturn(Optional.empty());

        // Mock repository findById
        when(repository.findById(savedEntity.getId())).thenReturn(
            Optional.of(savedEntity)
        );

        // Mock redis set cache
        doNothing().when(cacheService).setCache(any(), any(), any(), any());

        // Mock mapper toResDto
        when(mapper.toResDto(any())).thenAnswer(invocation -> {
            AppUser u = invocation.getArgument(0);
            return AppUserBaseResDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .fullName(u.getFullName())
                .roleName(u.getRoleName())
                .build();
        });

        // Execute
        AppUserBaseResDto result = appUserService.findUserById(
            savedEntity.getId()
        );

        ArgumentCaptor<String> cacheKeyCaptor = ArgumentCaptor.forClass(
            String.class
        );
        verify(cacheService).setCache(
            cacheKeyCaptor.capture(),
            eq(result),
            any(),
            any()
        );
        assertNotNull(cacheKeyCaptor.getValue());

        String cacheKey = cacheKeyCaptor.getValue();
        assertEquals(findKey, cacheKey);

        assertEquals(savedEntity.getId(), result.id());
        assertEquals(savedEntity.getUsername(), result.username());
        assertEquals(savedEntity.getEmail(), result.email());
        assertEquals(savedEntity.getFullName(), result.fullName());
        assertEquals(savedEntity.getRoleName(), result.roleName());

        verify(cacheService, times(1)).setCache(any(), any(), any(), any());
        verify(repository, times(1)).findById(savedEntity.getId());
        verify(cacheService, times(1)).getCache(
            findKey,
            AppUserBaseResDto.class
        );
    }

    @Test
    void updateUser_butNotFound_thenShouldThrowException() {
        var updateReq = AppUserFactory.appUserReqUpdateDto();
        // Mock repository findById
        when(repository.findById(any())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> appUserService.updateUser(1L, updateReq)
        );
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verify(repository, times(1)).findById(any());
        verify(repository, never()).findAll(
            any(Specification.class),
            any(Pageable.class)
        );
        verify(repository, never()).save(any());
        verify(mapper, never()).toResDto(any());
        verify(mapper, never()).updateEntity(any(), any());
        verify(cacheService, never()).setCache(any(), any(), any(), any());
    }
}
