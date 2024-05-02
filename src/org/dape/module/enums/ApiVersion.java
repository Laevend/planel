package org.dape.module.enums;

public enum ApiVersion 
{
    V1_17_R1(JavaVersion.JAVA_16),
    V1_17_R2(JavaVersion.JAVA_16),
    V1_18_R1(JavaVersion.JAVA_17),
    V1_18_R2(JavaVersion.JAVA_17),
    V1_19_R1(JavaVersion.JAVA_17),
    CUSTOM(JavaVersion.CUSTOM)
    ;

    private final JavaVersion minSupportedVersion;

    private ApiVersion(JavaVersion minSupportedVersion)
    {
        this.minSupportedVersion = minSupportedVersion;
    }

    public JavaVersion getMinimumJavaVersion()
    {
        return this.minSupportedVersion;
    }

    public boolean isSupported(JavaVersion version)
    {
        if (version == null)
            return false;

        if (version.equals(JavaVersion.CUSTOM))
            return true;
        if (this.equals(ApiVersion.CUSTOM))
            return true;

        return version.getSpec() >= this.minSupportedVersion.getSpec();
    }
}
