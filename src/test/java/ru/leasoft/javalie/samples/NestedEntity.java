package ru.leasoft.javalie.samples;

import javax.validation.constraints.NotNull;

public class NestedEntity {

    @NotNull
    private String nestedEntityStringField;

    private Integer nestedIntegerField;

    public int nonGetter() {
        return 0;
    }

    public Integer getNestedIntegerField() {
        return nestedIntegerField;
    }

    public void setNestedIntegerField(Integer nestedIntegerField) {
        this.nestedIntegerField = nestedIntegerField;
    }

    public String getNestedEntityStringField() {
        return nestedEntityStringField;
    }

    public void setNestedEntityStringField(String nestedEntityStringField) {
        this.nestedEntityStringField = nestedEntityStringField;
    }
}
