package org.dape.module.enums;

public enum JavaVersion 
{
    JAVA_8(8), // Assuming at least 8_151
    JAVA_9(9),
    JAVA_10(10),
    JAVA_11(11),
    JAVA_12(12),
    JAVA_13(13),
    JAVA_14(14),
    JAVA_15(15),
    JAVA_16(16),
    JAVA_17(17),
    CUSTOM(-1)
    ;

    private final int specification;

    private JavaVersion(int specification)
    {
        this.specification = specification;
    }

    public int getSpec()
    {
        return this.specification;
    }
}
