package vn.io.nguyen32.crm.common.annotation;

import org.mapstruct.Mapping;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Mapping(target = "audit.createdAt", source = "createdAt")
@Mapping(target = "audit.createdBy", source = "createdBy")
@Mapping(target = "audit.updatedAt", source = "updatedAt")
@Mapping(target = "audit.updatedBy", source = "updatedBy")
public @interface MapAuditFields {
}
