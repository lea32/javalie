package ru.leasoft.javalie.samples;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SampleEntity {

    @NotNull
    private String stringField1;

    @Size(max = 3)
    private String stringField2 = "test";

    @NotNull
    private NestedEntity getNestedEntityField1;

    @Valid
    private NestedEntity nestedEntityField2 = new NestedEntity();

    @AssertTrue
    public boolean isSomethingTrue() {
        return false;
    }

    public String getStringField1() {
        return stringField1;
    }

    public void setStringField1(String stringField1) {
        this.stringField1 = stringField1;
    }

    public String getStringField2() {
        return stringField2;
    }

    public void setStringField2(String stringField2) {
        this.stringField2 = stringField2;
    }

    public NestedEntity getGetNestedEntityField1() {
        return getNestedEntityField1;
    }

    public void setGetNestedEntityField1(NestedEntity getNestedEntityField1) {
        this.getNestedEntityField1 = getNestedEntityField1;
    }

    public NestedEntity getNestedEntityField2() {
        return nestedEntityField2;
    }

    public void setNestedEntityField2(NestedEntity nestedEntityField2) {
        this.nestedEntityField2 = nestedEntityField2;
    }
}
