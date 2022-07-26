package dev.brassboard.module.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.Material;
import org.bukkit.plugin.PluginLoadOrder;

import dev.brassboard.module.enums.ApiVersion;
import dev.brassboard.module.enums.JavaVersion;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BrassModule
{
	String name();
	String description() default "none";
	String version() default "0.1.0";
	String[] author() default "unknown";
	Material icon() default Material.GRASS_BLOCK;
	String[] dependencies() default { };
	String[] externalLibs() default { };
	JavaVersion jdk() default JavaVersion.MINIMUM_SUPPORTED_FOR_API_VERSION;
	ApiVersion[] api();
	PluginLoadOrder load() default PluginLoadOrder.STARTUP;
}
